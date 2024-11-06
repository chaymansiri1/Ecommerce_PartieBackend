package com.securityModel.Dtos.request;


import com.securityModel.models.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GalleryRequest {
    private String url_photo;
    private Product product;
}
