package com.securityModel.services;


import com.securityModel.Dtos.request.ClientRequest;
import com.securityModel.Dtos.response.ClientResponse;
import com.securityModel.payload.request.LoginRequest;
import com.securityModel.payload.request.SignupRequest;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

public interface ClientService {
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws MessagingException;
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signUpRequest, MultipartFile multipartFile) throws MessagingException;

    public ResponseEntity<?> Signin(@Valid @RequestBody LoginRequest loginRequest);
    ClientResponse createclient(ClientRequest client);
    List<ClientResponse> allclient();

    ClientResponse clientById(Long id);

    ClientResponse updateclient(ClientRequest clientRequest,Long id);

    HashMap<String,String> deleteclient(Long id);
}
