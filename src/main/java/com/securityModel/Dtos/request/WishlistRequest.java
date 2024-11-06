package com.securityModel.Dtos.request;


import com.securityModel.models.Product;
import com.securityModel.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WishlistRequest {
    private Long id;
    private Product product;
    private User user;
}
