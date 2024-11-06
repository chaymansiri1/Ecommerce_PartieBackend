package com.securityModel.services.impl;


import com.securityModel.Dtos.request.ClientRequest;
import com.securityModel.Dtos.response.ClientResponse;
import com.securityModel.Utils.StorageServices;
import com.securityModel.models.*;
import com.securityModel.payload.request.LoginRequest;
import com.securityModel.payload.request.SignupRequest;
import com.securityModel.payload.response.JwtResponse;
import com.securityModel.payload.response.MessageResponse;
import com.securityModel.repository.CustomerRepository;
import com.securityModel.repository.RoleRepository;
import com.securityModel.repository.UserRepository;
import com.securityModel.security.jwt.JwtUtils;
import com.securityModel.security.services.RefreshTokenService;
import com.securityModel.security.services.UserDetailsImpl;
import com.securityModel.services.ClientService;
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
public class ClientServiceIMPL implements ClientService {
    private final CustomerRepository clientDaoconst;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder encoder;
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

    public ClientServiceIMPL(CustomerRepository clientDaoconst, UserRepository userRepository, RoleRepository roleRepository) {
        this.clientDaoconst = clientDaoconst;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws MessagingException {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        Customer customer = new Customer(signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),signUpRequest.getLocalisation());

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
        Role modRole = roleRepository.findByName(ERole.ROLE_CUSOTMER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(modRole);
        customer.setRoles(roles);

        clientDaoconst.save(customer);
        sendConfirmationEmail(customer);

        return ResponseEntity.ok(new MessageResponse("Customer registered successfully! check your email for confirmation"));
    }

    @Override
    public ResponseEntity<?> signup(SignupRequest signUpRequest, MultipartFile multipartFile) throws MessagingException {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        Customer customer = new Customer(signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),signUpRequest.getLocalisation());

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
        Role modRole = roleRepository.findByName(ERole.ROLE_CUSOTMER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        roles.add(modRole);
        customer.setRoles(roles);
        String img=storageServices.store(multipartFile);
        customer.setImage(img);
        clientDaoconst.save(customer);
        sendConfirmationEmail(customer);

        return ResponseEntity.ok(new MessageResponse("Customer registered successfully! check your email for confirmation"));
    }



    private void sendConfirmationEmail(Customer customer) throws MessagingException {

        //Envoyer un email de confirmation par mailtrap
        String from="admin@gmail.com";
        String to=customer.getEmail();
        MimeMessage message=emailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message);
        helper.setSubject("Complete registration");
        helper.setFrom(from);
        helper.setTo(to);
        helper.setText("<HTML><body><a href=\"http://localhost:8088/api/auth/confirm?email="+customer.getEmail()
                +"\">VERIFY</a></body></HTML>",true);
        emailSender.send(message);

        //Envoyer un email de confirmation par gmail
//        MimeMessage message = emailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message);
//        helper.setSubject("Complete Registration");
//        helper.setFrom("nsirichayma4@gmail.com");
//        helper.setTo(customer.getEmail());
//        String confirmationUrl = "http://localhost:8088/api/auth/confirm?email=" + customer.getEmail();
//        String emailContent = "<html><body><p>Click the link below to complete your registration:</p>" +
//                "<a href=\"" + confirmationUrl + "\">Confirm</a></body></html>";
//
//        helper.setText(emailContent, true);
//        emailSender.send(message);
    }

    public ResponseEntity<?> Signin(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        Optional<Customer> customer = clientDaoconst.findByUsername(loginRequest.getUsername());

        if (customer.get().isConfirm() == true) {

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            String jwt = jwtUtils.generateJwtToken(userDetails);

            List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

            return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
                    userDetails.getUsername(), userDetails.getEmail(), roles));

        } else {
            throw new RuntimeException("customer not confirm");
        }
    }

    @Override
    public ClientResponse createclient(ClientRequest client) {
        Customer cl=ClientResponse.toEntity(client);
        Customer savedclient=clientDaoconst.save(cl);
        return ClientResponse.fromEntity(savedclient);
    }

    @Override
    public List<ClientResponse> allclient() {
        return clientDaoconst.findAll().stream()
                .map(ClientResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public ClientResponse clientById(Long id) {
        return clientDaoconst.findById(id)
                .map(ClientResponse::fromEntity)
                .orElseThrow(()->new RuntimeException("client not found with this id :"+id));
    }

    @Override
    public ClientResponse updateclient(ClientRequest clientRequest, Long id) {
        Customer client =clientDaoconst.findById(id).orElseThrow(() ->
                new RuntimeException("product not found with this id:"+id));
        if(client !=null){
            Customer cl=ClientResponse.toEntity(clientRequest);
            cl.setId(id);
            cl.setLocalisation(cl.getLocalisation()==null ?client.getLocalisation():cl.getLocalisation());
            cl.setUsername(cl.getUsername()==null?client.getUsername():cl.getUsername());
            cl.setUsername(cl.getUsername()==null ?client.getUsername():cl.getUsername());
            cl.setEmail(cl.getEmail()==null? client.getEmail():cl.getEmail());

            Customer savedclient=clientDaoconst.save(cl);
            return  ClientResponse.fromEntity(savedclient);
        }else {
            return null;
        }
    }

    @Override
    public HashMap<String, String> deleteclient(Long id) {
        HashMap message=new HashMap<>();
        Customer cl=clientDaoconst.findById(id).orElse(null);
        if (cl !=null){
            try{
                clientDaoconst.delete(cl);
                message.put("Etat","client deleted successfully");

            }catch (Exception e){
                message.put("Etat",": "+e.getMessage());
            }
        }
        else{

            message.put("Etat","client not found with this id: "+id);
        }
        return message;
    }



    }

