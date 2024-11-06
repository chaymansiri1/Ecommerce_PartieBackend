package com.securityModel.services;


import com.securityModel.Dtos.request.GalleryRequest;
import com.securityModel.Dtos.response.GalleryResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

public interface GalleryService {
    GalleryResponse creategallery(GalleryRequest gallery);
    List<GalleryResponse> addMultiplePhotosToGallery(Long productId, List<MultipartFile> photos);

    List<GalleryResponse> allgallery();

    GalleryResponse galleryById (Long id);

    GalleryResponse updategallery(GalleryRequest galleryRequest, Long id);


    HashMap<String,String> deletephotofromgallery(Long id);


    HashMap<String, String> deletegallery(Long id);

    HashMap<String,String> deletePhotoFromGalleryProduit(Long productId,Long galleryId );
}
