package com.securityModel.services;



import com.securityModel.Dtos.request.ProductRequest;
import com.securityModel.Dtos.request.ProviderRequest;
import com.securityModel.Dtos.response.ProductResponse;
import com.securityModel.Dtos.response.ProviderResponse;
import com.securityModel.payload.request.LoginRequest;
import com.securityModel.payload.request.SignupRequest;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

public interface ProviderService {

    ProviderResponse createprovider(ProviderRequest provider);


    List<ProviderResponse> allprovider();

    ProviderResponse getById(Long id);

    ProviderResponse updateprovider(ProviderRequest providerRequest, Long id);

    ProviderResponse updateproviderImg(ProviderRequest providerRequest, @PathVariable Long Id, @RequestParam("file") MultipartFile file);
    HashMap<String, String> deleteprovider(Long id);

    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest, MultipartFile multipartFile) throws MessagingException;

    public ResponseEntity<?> Signin(@Valid @RequestBody LoginRequest loginRequest);
}