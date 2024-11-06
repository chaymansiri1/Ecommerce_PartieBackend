package com.securityModel.Dtos.response;


import com.securityModel.Dtos.request.ProviderRequest;
import com.securityModel.models.Product;
import com.securityModel.models.Provider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProviderResponse {
    private Long id ;

    private String email;
    private String password;
    private String username;
    private String image;
    private String company;
    private String matricule;


    public static ProviderResponse fromEntity(Provider entity){

        ProviderResponse providerResponse =new ProviderResponse();
        BeanUtils.copyProperties(entity,providerResponse);
        return providerResponse;
        //l'objet ProductResponse c'est une version "allégée" de l'entité, adaptée pour être renvoyée au client.
    }


    //Elle prend un objet ProductRequest (un autre DTO) et le transforme en une entité Product.
    public static Provider toEntity(ProviderRequest providerResponse){
        Provider p =new Provider();
        BeanUtils.copyProperties(providerResponse,p);
        return p;
    }
}
