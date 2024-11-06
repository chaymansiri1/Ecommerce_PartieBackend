package com.securityModel.services.impl;


import com.securityModel.Dtos.request.UserRequest;
import com.securityModel.Dtos.response.UserResponse;
import com.securityModel.exception.TokenRefreshException;
import com.securityModel.models.ERole;
import com.securityModel.models.RefreshToken;
import com.securityModel.models.Role;
import com.securityModel.models.User;
import com.securityModel.payload.request.LoginRequest;
import com.securityModel.payload.request.SignupRequest;
import com.securityModel.payload.request.TokenRefreshRequest;
import com.securityModel.payload.response.JwtResponse;
import com.securityModel.payload.response.MessageResponse;
import com.securityModel.payload.response.TokenRefreshResponse;
import com.securityModel.repository.RoleRepository;
import com.securityModel.repository.UserRepository;
import com.securityModel.security.jwt.JwtUtils;
import com.securityModel.security.services.RefreshTokenService;
import com.securityModel.security.services.UserDetailsImpl;
import com.securityModel.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceIMPL implements UserService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private UserRepository userRepository;
    public final UserRepository userDaoconst;

    public UserServiceIMPL(UserRepository userDaoconst) {
        this.userDaoconst = userDaoconst;
    }

    @Override
    public UserResponse createuser(UserRequest user) {

        User u = UserResponse.toEntity(user);
        User saveduser = userDaoconst.save(u);
        return UserResponse.fromEntity(saveduser);
    }

    @Override
    public List<UserResponse> alluser() {
        return userDaoconst.findAll().stream()
                .map(UserResponse::fromEntity)
                .collect(Collectors.toList());
    }


    @Override
    public UserResponse userbyid(Long id) {
        return userDaoconst.findById(id)
                .map(UserResponse::fromEntity)
                .orElseThrow(()->new RuntimeException("user not found with this id :"+id));
    }


    @Override
    public UserResponse updateuser(UserRequest userRequest, Long id) {
        User user =userDaoconst.findById(id).orElseThrow(() ->
                new RuntimeException("user not found with this id:"+id));
        if(user !=null){
            User u =UserResponse.toEntity(userRequest);
            u.setId(id);
            u.setUsername(u.getUsername()==null ?user.getUsername():u.getUsername());
            User saveduser=userDaoconst.save(u);
            return  UserResponse .fromEntity(saveduser);
        }else {
            return null;
        }
    }

    @Override
    public HashMap<String, String> deleteuser(Long id) {
        HashMap message=new HashMap<>();
        User u=userDaoconst.findById(id).orElse(null);
        if (u !=null){
            try{
                userDaoconst.delete(u);
                message.put("Etat","user deleted successfully");

            }catch (Exception e){
                message.put("Etat",": "+e.getMessage());
            }
        }
        else{

            message.put("Etat","user not found with this id: "+id);
        }
        return message;
    }


    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        Optional<User> u = userRepository.findByUsername(loginRequest.getUsername());

        if (u.get().isConfirm() == true) {

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            String jwt = jwtUtils.generateJwtToken(userDetails);

            List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

            return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
                    userDetails.getUsername(), userDetails.getEmail(), roles));

        } else {
            throw new RuntimeException("user not confirm");
        }
    }


    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws MessagingException {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "PROVIDER":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_PROVIDER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "CUSTOMER":
                        Role modRole = roleRepository.findByName(ERole.ROLE_CUSOTMER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);
        sendConfirmationEmail(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully! Check your email for confirmation."));
    }

    private void sendConfirmationEmail(User user) throws MessagingException {
        // Envoyer un email de confirmation par mailtrap
    String from="admin@gmail.com";
    String to=user.getEmail();
    MimeMessage message=emailSender.createMimeMessage();
    MimeMessageHelper helper=new MimeMessageHelper(message);
        helper.setSubject("Complete registration");
        helper.setFrom(from);
        helper.setTo(to);
        helper.setText("<HTML><body><a href=\"http://localhost:8088/api/auth/confirm?email="+user.getEmail()
                +"\">VERIFY</a></body></HTML>",true);
        emailSender.send(message);

//        MimeMessage message = emailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message);
//        helper.setSubject("Complete Registration");
//        helper.setFrom("nsirichayma4@gmail.com");
//        helper.setTo(user.getEmail());
//        String confirmationUrl = "http://localhost:8088/api/auth/confirm?email=" + user.getEmail();
//        String emailContent = "<html><body><p>Click the link below to complete your registration:</p>" +
//                "<a href=\"" + confirmationUrl + "\">Confirm</a></body></html>";
//
//        helper.setText(emailContent, true);
//        emailSender.send(message);
    }



    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    public ResponseEntity<?> logoutUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        refreshTokenService.deleteByUserId(userId);
        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
    }

    public ResponseEntity<?> confirmCustomer(@RequestParam String email) {
        User user = userRepository.findByEmail(email);

        if (user.isConfirm()) {
            return ResponseEntity.badRequest().body(new MessageResponse("user is already confirmed."));
        }

        // Passer la valeur de confirm à true
        user.setConfirm(true);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("user confirmed successfully!"));
    }

    public HashMap<String, String> forgetPassword(@RequestParam String email) throws MessagingException {

        HashMap<String, String> response = new HashMap<>();
// Rechercher l'utilisateur par son email
        User user = userRepository.findByEmail(email);
        if (user == null) {
            response.put("message", "Aucun utilisateur trouvé avec cet email");
            return response;
        }
        UUID forget = UUID.randomUUID();
        user.setPasswordResetToken(forget.toString());
        user.setId(user.getId());


        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setSubject("Réinitialisation de mot de passe");
        helper.setFrom("nsirichayma4@gmail.com");
        helper.setTo(user.getEmail());

        helper.setText("Votre code est  : " + user.getPasswordResetToken(), true);
        emailSender.send(message);
        userRepository.saveAndFlush(user);

        response.put("message", "Un email de réinitialisation a été envoyé à " + email);
        return response;
    }

    public HashMap<String, String> resetPassword(@PathVariable String passwordResetToken, String newPassword) throws MessagingException {
        User user = userRepository.findByPasswordResetToken(passwordResetToken);
        HashMap response = new HashMap<>();

        if (user != null) {
            user.setId((user.getId()));
            user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
            userRepository.save(user);
            response.put("reset Password", "proccessed");
            return response;
        } else {

            response.put("reset Password", "failed ");
            return response;
        }

    }


    public ResponseEntity<?> changerPassword(@RequestParam String oldPassword,
                                             @RequestParam String newPassword,
                                             @RequestParam String email) {
        // Rechercher l'utilisateur par son email
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Aucun utilisateur trouvé avec cet email.");
        }

        // Vérifier si l'ancien mot de passe est correct
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("L'ancien mot de passe est incorrect.");
        }

        // Si l'ancien mot de passe est correct, mettre à jour le mot de passe
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Réponse réussie
        return ResponseEntity.ok("Mot de passe changé avec succès.");
    }


    public HashMap<String, String> changePassword(@RequestParam String oldPassword,
                                                  @RequestParam String newPassword,
                                                  @RequestParam String token) {
        HashMap<String, String> response = new HashMap<>();

        // Trouver l'utilisateur par le token
        User user = userRepository.findByPasswordResetToken(token);
        if (user == null) {
            response.put("message", "Token invalide");
            return response;
        }

        // Vérifier si l'ancien mot de passe est correct
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            response.put("message", "L'ancien mot de passe est incorrect");
            return response;
        }

        // Si tout est correct, mettre à jour le mot de passe
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null); // Invalider le token après le changement de mot de passe
        userRepository.save(user);

        response.put("message", "Mot de passe changé avec succès");
        return response;
    }
}
