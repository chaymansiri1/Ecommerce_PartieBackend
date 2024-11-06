package com.securityModel.Dtos.response;


import com.securityModel.Dtos.request.OrderRequest;
import com.securityModel.models.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String ref;
    private String description;
    private int qte_total;
    private double price_total;
    private boolean state;
    private Date orderDate;
    private UserResponse user;
    private CartResponse cart;
    //private Product product;
    private List<ProductResponse> products;

    public static OrderResponse fromEntity(Order entity){

        OrderResponse orderResponse=new OrderResponse();
        BeanUtils.copyProperties(entity,orderResponse);
        if (entity.getProducts() != null) {
            orderResponse.setProducts(entity.getProducts().stream()
                    .map(ProductResponse::fromEntity)
                    .collect(Collectors.toList()));
        }
        return orderResponse;
        //l'objet ProductResponse c'est une version "allégée" de l'entité, adaptée pour être renvoyée au client.
    }


    //Elle prend un objet ProductRequest (un autre DTO) et le transforme en une entité Product.
    public static Order toEntity(OrderRequest orderResponse){
        Order o =new Order();
        BeanUtils.copyProperties(orderResponse,o);
        return o;
    }

}
