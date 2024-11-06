package com.securityModel.Dtos.response;


import com.securityModel.Dtos.request.CartRequest;
import com.securityModel.models.Cart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor


public class CartResponse {
    private Long id;
    private int qtetotal;
    private double price_total;
    private int qte_user;
    private List<ProductResponse> products;

    public static CartResponse fromEntity(Cart entity) {
        CartResponse cartResponse = new CartResponse();
        BeanUtils.copyProperties(entity, cartResponse);

        // Copie manuelle des produits
        if (entity.getProducts() != null) {
            cartResponse.setProducts(
                    entity.getProducts().stream()
                            .map(ProductResponse::fromEntity) // Conversion de chaque produit en ProductResponse
                            .collect(Collectors.toList())
            );
        }

        return cartResponse;
    }

    public static Cart toEntity(CartRequest cartResponse) {
        Cart cart = new Cart();
        BeanUtils.copyProperties(cartResponse, cart);
        return cart;
    }
}
