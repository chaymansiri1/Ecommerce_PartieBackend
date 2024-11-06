package com.securityModel.Dtos.response;


import com.securityModel.Dtos.request.ProductRequest;
import com.securityModel.models.Gallery;
import com.securityModel.models.Product;
import com.securityModel.models.Provider;
import com.securityModel.models.Subcategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class ProductResponse {
    private long id;
    private String name;
    private String ref;
    private String color;
    private int qte;
    private int qte_en_stock;
    private double price;
    private String description;
    private String image;
    private int qte_user;
//    private Gallery gallery;
    private Provider provider;
    private Subcategory subcategory;


// pour transformer une entité en DTO.
//Cette méthode prend un objet Product
    // copie automatiquement les propriétés de l'objet entity vers l'objet productResponse.
    // transformer une entité en DTO.
    //l'objet ProductResponse c'est une version "allégée" de l'entité, adaptée pour être renvoyée au client.


    public static ProductResponse fromEntity(Product entity){
        ProductResponse productResponse=new ProductResponse();
        BeanUtils.copyProperties(entity,productResponse);
        return productResponse;
       }



    //Elle prend un objet ProductRequest DTO et le transforme en une entité Product.
    //copier les propriétés du DTO ProductRequest vers une nouvelle instance de Product.

    public static Product toEntity(ProductRequest productResponse){
        Product p =new Product();
        BeanUtils.copyProperties(productResponse,p);
        return p;
    }


    }


