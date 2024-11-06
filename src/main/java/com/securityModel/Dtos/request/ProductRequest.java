package com.securityModel.Dtos.request;


import com.securityModel.models.Provider;
import com.securityModel.models.Subcategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    private long id;
    private String name;
    private String ref;
    private String color;
    private int qte;
    private int qte_en_stock;
    private double price;
    private String description;
    private String image;
private Provider provider;
private Subcategory subcategory;




}
