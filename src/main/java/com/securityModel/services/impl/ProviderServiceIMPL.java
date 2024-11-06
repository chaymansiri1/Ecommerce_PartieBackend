package com.securityModel.services.impl;


import com.securityModel.Dtos.request.ProductRequest;
import com.securityModel.Dtos.request.ProviderRequest;
import com.securityModel.Dtos.response.ProductResponse;
import com.securityModel.Dtos.response.ProviderResponse;
import com.securityModel.Utils.StorageServices;
import com.securityModel.models.*;
import com.securityModel.payload.request.LoginRequest;
import com.securityModel.payload.request.SignupRequest;
import com.securityModel.payload.response.JwtResponse;
import com.securityModel.payload.response.MessageResponse;
import com.securityModel.repository.ProviderRepository;
import com.securityModel.repository.RoleRepository;
import com.securityModel.repository.UserRepository;
import com.securityModel.security.jwt.JwtUtils;
import com.securityModel.security.services.RefreshTokenService;
import com.securityModel.security.services.UserDetailsImpl;
import com.securityModel.services.ProviderService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;
@Service
public class ProviderServiceIMPL implements ProviderService {

    private final ProviderRepository providerDaoconst;
    private final UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ProviderRepository providerRepository;
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private StorageServices storageServices;

    public ProviderServiceIMPL(ProviderRepository providerDaoconst, UserRepository userRepository) {
        this.providerDaoconst = providerDaoconst;
        this.userRepository = userRepository;
    }

    @Override
    public ProviderResponse createprovider(ProviderRequest provider) {
        Provider pr = ProviderResponse.toEntity(provider);
        Provider savedprovider = providerDaoconst.save(pr);
        return ProviderResponse.fromEntity(savedprovider);
    }


    @Override
    public List<ProviderResponse> allprovider() {
        return providerDaoconst.findAll().stream()
                .map(ProviderResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public ProviderResponse getById(Long id) {
        return providerDaoconst.findById(id)
                .map(ProviderResponse::fromEntity)
                .orElseThrow(() -> new RuntimeException("product not found with this id :" + id));
    }

    @Override
    public ProviderResponse updateprovider(ProviderRequest providerRequest, Long id) {
        Provider provider = providerDaoconst.findById(id).orElseThrow(() ->
                new RuntimeException("provider not found with this id:" + id));
        if (provider != null) {
            Provider p = ProviderResponse.toEntity(providerRequest);
            p.setId(id);
            p.setCompany(p.getCompany() == null ? provider.getCompany() : p.getCompany());
            Provider savedprovider = providerDaoconst.save(p);
            return ProviderResponse.fromEntity(savedprovider);
        } else {
            return null;
        }
    }

    @Override
    public ProviderResponse updateproviderImg(ProviderRequest providerRequest, Long Id, MultipartFile file) {

        // Trouver le produit par son ID
        Provider p = providerDaoconst.findById(Id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec cet ID: " + Id));


        // Mettre à jour les informations du produit
        p.setUsername(providerRequest.getUsername());
        p.setEmail(providerRequest.getEmail());
        p.setMatricule(providerRequest.getMatricule());
        p.setCompany(providerRequest.getCompany());


        // Si un fichier image est fourni, mettre à jour l'image
        if (file != null && !file.isEmpty()) {
            String img = storageServices.store(file);
            p.setImage(img);  // Mettre à jour l'image du produit
        }

        // Sauvegarder le produit mis à jour
        Provider savedProvider = providerDaoconst.save(p);

        // Retourner la réponse avec les informations du produit mis à jour
        return ProviderResponse.fromEntity(savedProvider);

    }



    @Override
    public HashMap<String, String> deleteprovider(Long id) {
        HashMap message=new HashMap<>();
        Provider p=providerDaoconst.findById(id).orElse(null);
        if (p !=null){
            try{
                providerDaoconst.delete(p);
                message.put("Etat","provider deleted successfully");

            }catch (Exception e){
                message.put("Etat",": "+e.getMessage());
            }
        }
        else{

            message.put("Etat","provider not found with this id: "+id);
        }
        return message;
    }

    @Override
    public ResponseEntity<?> registerUser(SignupRequest signUpRequest, MultipartFile multipartFile) throws MessagingException {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account

        Provider provider=new Provider(signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),signUpRequest.getMatricule(),signUpRequest.getCompany());

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
        Role modRole = roleRepository.findByName(ERole.ROLE_PROVIDER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(modRole);
        provider.setRoles(roles);
        String img=storageServices.store(multipartFile);
        provider.setImage(img);
        providerRepository.save(provider);
        sendConfirmationEmail(provider);
        return ResponseEntity.ok(new MessageResponse("Provider registered successfully! check your email for confirmation"));

    }
    private void sendConfirmationEmail(Provider provider) throws MessagingException {
        //Envoyer un email de confirmation par mailtrap
        String from="admin@gmail.com";
        String to=provider.getEmail();
        MimeMessage message=emailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message);
        helper.setSubject("Complete registration");
        helper.setFrom(from);
        helper.setTo(to);
        helper.setText("<HTML><body><a href=\"http://localhost:8088/api/auth/confirm?email="+provider.getEmail()
                +"\">VERIFY</a></body></HTML>",true);
        emailSender.send(message);
        //Envoyer un email de confirmation par gmail
//        MimeMessage message = emailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message);
//        helper.setSubject("Complete Registration");
//        helper.setFrom("nsirichayma4@gmail.com");
//        helper.setTo(provider.getEmail());
//        String confirmationUrl = "http://localhost:8088/api/auth/confirm?email=" + provider.getEmail();
//        String emailContent = "<html><body><p>Click the link below to complete your registration:</p>" +
//                "<a href=\"" + confirmationUrl + "\">Confirm</a></body></html>";
//
//        helper.setText(emailContent, true);
//        emailSender.send(message);
    }

    public ResponseEntity<?> Signin(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        Optional<Provider> provider = providerRepository.findByUsername(loginRequest.getUsername());

        if (provider.get().isConfirm() == true) {

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            String jwt = jwtUtils.generateJwtToken(userDetails);

            List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

            return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
                    userDetails.getUsername(), userDetails.getEmail(), roles));

        } else {
            throw new RuntimeException("provider not confirm");
        }
    }
}
