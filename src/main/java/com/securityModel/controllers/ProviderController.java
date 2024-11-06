package com.securityModel.controllers;

import com.securityModel.Dtos.request.ProductRequest;
import com.securityModel.Dtos.request.ProviderRequest;
import com.securityModel.Dtos.response.ProductResponse;
import com.securityModel.Dtos.response.ProviderResponse;
import com.securityModel.payload.request.LoginRequest;
import com.securityModel.payload.request.SignupRequest;
import com.securityModel.repository.ProviderRepository;
import com.securityModel.repository.RoleRepository;
import com.securityModel.repository.UserRepository;
import com.securityModel.services.ProviderService;
import com.securityModel.services.impl.ProviderServiceIMPL;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/providers")
public class ProviderController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    ProviderRepository providerRepository;
    @Autowired
    ProviderService providerService;


    @PostMapping("/save")
    public ProviderResponse addprovider(@RequestBody ProviderRequest providerRequest) {
        return providerService.createprovider(providerRequest);
    }


    @GetMapping("/all")
    public List<ProviderResponse> allprovider() {
        return providerService.allprovider();
    }


    @GetMapping("/getbyid/{id}")
    public ProviderResponse providerById(@PathVariable Long id) {
        return providerService.getById(id);
    }

    @DeleteMapping("/delete/{id}")
    public HashMap<String, String> deleteprovider(@PathVariable Long id) {

        return providerService.deleteprovider(id);
    }


    @PutMapping("/update/{id}")
    public ProviderResponse updateprovider(@RequestBody ProviderRequest providerRequest, @PathVariable Long id) {
        return providerService.updateprovider(providerRequest, id);
    }
    @PutMapping("/updateproviderImg/{Id}")
    public ProviderResponse updateproviderImg(ProviderRequest providerRequest, @PathVariable Long Id, @RequestParam("file") MultipartFile file){
        return providerService.updateproviderImg(providerRequest, Id,file);
    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid SignupRequest signUpRequest,@RequestParam("file") MultipartFile multipartFile) throws MessagingException {
        return providerService.registerUser(signUpRequest, multipartFile);
    }
    @PostMapping("/signin")
    public ResponseEntity<?> Signin(@Valid @RequestBody LoginRequest loginRequest) {
        return providerService.Signin(loginRequest);
    }
}
