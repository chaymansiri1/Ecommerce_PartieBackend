package com.securityModel.Dtos.request;

import ch.qos.logback.core.net.server.Client;

import com.securityModel.models.Customer;
import com.securityModel.models.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    public static Order toEntity;
    private String ref;
    private String description;
    private int qte_total;
    private double price_total;
    private boolean state;
    private Customer client;


}
