package com.securityModel.Dtos.response;


import com.securityModel.Dtos.request.ClientRequest;
import com.securityModel.models.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String password;
    private String username;
    private String image;

    private String localisation;

    public static ClientResponse fromEntity(Customer entity){

        ClientResponse clientResponse =new ClientResponse();
        BeanUtils.copyProperties(entity,clientResponse);
        return clientResponse;
        //l'objet ClientResponse c'est une version "allégée" de l'entité, adaptée pour être renvoyée au client.
    }


    //Elle prend un objet ClientRequest (un autre DTO) et le transforme en une entité Product.
    public static Customer toEntity(ClientRequest clientResponse){
        Customer client =new Customer();
        BeanUtils.copyProperties(clientResponse,client);
        return client;
    }


}

