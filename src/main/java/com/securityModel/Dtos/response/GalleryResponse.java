package com.securityModel.Dtos.response;


import com.securityModel.Dtos.request.GalleryRequest;
import com.securityModel.models.Gallery;
import com.securityModel.models.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GalleryResponse {
    private String url_photo;
    private Product product;



    public static GalleryResponse fromEntity(Gallery entity){
        GalleryResponse galleryResponse =new GalleryResponse();
        BeanUtils.copyProperties(entity,galleryResponse);
        return galleryResponse;
        //l'objet ProductResponse c'est une version "allégée" de l'entité, adaptée pour être renvoyée au client.
    }


    //Elle prend un objet ProductRequest (un autre DTO) et le transforme en une entité Product.
    public static Gallery toEntity(GalleryRequest galleryResponse){
        Gallery g =new Gallery();
        BeanUtils.copyProperties(galleryResponse,g);
        return g;
    }

}
