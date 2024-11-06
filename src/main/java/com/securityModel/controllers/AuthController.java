package com.securityModel.controllers;

import java.util.*;


import com.securityModel.Utils.StorageServices;
import com.securityModel.payload.request.LoginRequest;
import com.securityModel.payload.request.SignupRequest;
import com.securityModel.payload.request.TokenRefreshRequest;
import com.securityModel.repository.RoleRepository;
import com.securityModel.repository.UserRepository;
import com.securityModel.security.jwt.JwtUtils;
import com.securityModel.security.services.RefreshTokenService;
import com.securityModel.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;


    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    private UserService userService;


    @Autowired
    private JavaMailSender emailSender;



    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return userService.authenticateUser(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws MessagingException {
        return userService.registerUser(signUpRequest);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        return userService.refreshtoken(request);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        return userService.logoutUser();
    }

    // Méthode pour confirmer le client après l'inscription
    @GetMapping("/confirm")
    public ResponseEntity<?> confirmCustomer(@RequestParam String email) {
        return userService.confirmCustomer(email);
    }


    @PostMapping("/forgot-password")
    public HashMap<String, String> forgetPassword(@RequestParam String email) throws MessagingException {
        return userService.forgetPassword(email);
    }

    @PostMapping("/resetpassword/{passwordResetToken}")
    public HashMap<String, String> resetPassword(@PathVariable String passwordResetToken, String newPassword) throws MessagingException {
        return userService.resetPassword(passwordResetToken, newPassword);
    }
    @PutMapping("/change-password")
    public HashMap<String, String> changePassword(@RequestParam String oldPassword,
                                            @RequestParam String newPassword,
                                            @RequestParam String token) {
        return userService.changePassword(oldPassword,newPassword,token);
    }

    @PutMapping("/changepassword")
    public ResponseEntity<?> changerPassword(@RequestParam String oldPassword,
                                            @RequestParam String newPassword,
                                            @RequestParam String email) {
        return userService.changerPassword(oldPassword, newPassword, email);
    }


}