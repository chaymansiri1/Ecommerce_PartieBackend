package com.securityModel.Dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartRequest {
    private Long id;
    private int qtetotal;
    private double price_total;
    private int qte_user;


}
