package com.securityModel.controllers;


import com.securityModel.Dtos.request.GalleryRequest;
import com.securityModel.Dtos.response.GalleryResponse;
import com.securityModel.services.GalleryService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/galleries")
public class GalleryController {


    public final GalleryService galleryService;

    public GalleryController(GalleryService galleryService) {
        this.galleryService = galleryService;
    }

    @PostMapping("/save")
    public GalleryResponse addgallery(@RequestBody GalleryRequest galleryRequest){
        return galleryService.creategallery(galleryRequest);
    }

    @PostMapping("/addMultiplePhotos")
    public List<GalleryResponse> addMultiplePhotos(@RequestParam Long productId, @RequestParam List<MultipartFile> photos) {
        return galleryService.addMultiplePhotosToGallery(productId, photos);
    }

    @GetMapping("/all")
    public List<GalleryResponse> allgallery(){
        return galleryService.allgallery();
    }
    @GetMapping("/getbyid/{id}")
    public GalleryResponse getgalleryybyid(@PathVariable Long id){
        return galleryService.galleryById(id);
    }

    @PutMapping("/update/{id}")
    public GalleryResponse updategallery(@RequestBody GalleryRequest galleryRequest , @PathVariable Long id){
        return galleryService.updategallery(galleryRequest,id);
    }
    @DeleteMapping("/delete/{id}")
    public HashMap<String, String> deletegallery(@PathVariable Long id){

        return galleryService.deletegallery(id);
    }
    @DeleteMapping("/deletephoto/{id}")
    public HashMap<String,String> deletephoto(@PathVariable Long id){
        return galleryService.deletephotofromgallery(id);
    }
    @DeleteMapping("/deletePhoto")
    public HashMap<String, String> deletePhotoFromGallery(@RequestParam Long productId, @RequestParam Long galleryId) {
        return galleryService.deletePhotoFromGalleryProduit(productId, galleryId);
    }

}

