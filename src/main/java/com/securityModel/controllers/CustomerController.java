package com.securityModel.controllers;


import com.securityModel.Dtos.request.ClientRequest;
import com.securityModel.Dtos.response.ClientResponse;
import com.securityModel.payload.request.LoginRequest;
import com.securityModel.payload.request.SignupRequest;
import com.securityModel.repository.CustomerRepository;
import com.securityModel.repository.RoleRepository;
import com.securityModel.repository.UserRepository;
import com.securityModel.services.ClientService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/customers",produces = MediaType.APPLICATION_JSON_VALUE)

public class CustomerController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;
   @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private ClientService clientService;


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws MessagingException {
    return clientService.registerUser(signUpRequest);
    }

    @PostMapping("/registerImg")
    public ResponseEntity<?> signupImg(@Valid SignupRequest signUpRequest,@RequestParam("file") MultipartFile multipartFile) throws MessagingException {
        return clientService.signup(signUpRequest, multipartFile);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> Signin(@Valid @RequestBody LoginRequest loginRequest){
        return clientService.Signin(loginRequest);
}
    @GetMapping("/all")
    public List<ClientResponse> allclient(){
        return clientService.allclient();
    }

    @GetMapping("/getbyid/{id}")
    public ClientResponse getclientybyid(@PathVariable Long id){
        return clientService.clientById(id);
    }

    @PutMapping("/update/{id}")
    public ClientResponse updateclient(@RequestBody ClientRequest clientRequest, @PathVariable Long id){
        return clientService.updateclient(clientRequest,id);
    }
    @DeleteMapping("/delete/{id}")
    public HashMap<String, String> deleteclient(@PathVariable Long id){

        return clientService.deleteclient(id);
    }

}
