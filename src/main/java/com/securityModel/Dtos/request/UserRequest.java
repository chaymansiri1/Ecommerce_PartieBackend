package com.securityModel.Dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    private Long id ;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String password;
    private String username;
    private String image;
}
