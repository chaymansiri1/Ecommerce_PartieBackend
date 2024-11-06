package com.securityModel.services;


import com.securityModel.Dtos.request.UserRequest;
import com.securityModel.Dtos.response.UserResponse;
import com.securityModel.payload.request.LoginRequest;
import com.securityModel.payload.request.SignupRequest;
import com.securityModel.payload.request.TokenRefreshRequest;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;

public interface UserService {

    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest);
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest)throws MessagingException;
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request);
    public ResponseEntity<?> logoutUser();
    public ResponseEntity<?> confirmCustomer(@RequestParam String email);
    public HashMap<String, String> forgetPassword(@RequestParam String email) throws MessagingException;
    public HashMap<String, String> resetPassword(@PathVariable String passwordResetToken, String newPassword) throws MessagingException;
    public ResponseEntity<?> changerPassword(@RequestParam String oldPassword,
                                             @RequestParam String newPassword,
                                             @RequestParam String email);
    public HashMap<String, String> changePassword(@RequestParam String oldPassword,
                                                  @RequestParam String newPassword,
                                                  @RequestParam String token);
    UserResponse createuser(UserRequest user);
    List<UserResponse> alluser();

    UserResponse userbyid(Long id);

    UserResponse updateuser(UserRequest userRequest,Long id);

    HashMap<String,String> deleteuser(Long id);




}
