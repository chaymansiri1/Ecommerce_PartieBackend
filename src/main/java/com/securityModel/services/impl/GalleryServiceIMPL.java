package com.securityModel.services.impl;


import com.securityModel.Dtos.request.GalleryRequest;
import com.securityModel.Dtos.response.GalleryResponse;
import com.securityModel.Utils.StorageServices;
import com.securityModel.models.Gallery;
import com.securityModel.models.Product;
import com.securityModel.repository.GalleryDao;
import com.securityModel.repository.ProductDao;
import com.securityModel.services.GalleryService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class GalleryServiceIMPL implements GalleryService {
    private final StorageServices storageService;
    private final ProductDao productDaoconst;
    private final GalleryDao galleryDaoconst;

    public GalleryServiceIMPL(StorageServices storageService, ProductDao productDaoconst, GalleryDao galleryDaoconst) {
        this.storageService = storageService;
        this.productDaoconst = productDaoconst;
        this.galleryDaoconst = galleryDaoconst;
    }

    @Override
    public GalleryResponse creategallery(GalleryRequest gallery) {
        Gallery g= GalleryResponse.toEntity(gallery);
        Gallery savedgallery=galleryDaoconst.save(g);
        return GalleryResponse.fromEntity(savedgallery);
    }

    @Override
    public List<GalleryResponse> addMultiplePhotosToGallery(Long productId, List<MultipartFile> photos) {

        List<GalleryResponse> galleryResponses = new ArrayList<>();

        // Récupérer l'entité Product à partir de son ID
        Product product = productDaoconst.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec cet ID : " + productId));

        for (MultipartFile photo : photos) {
            String storedPhoto = storageService.store(photo);
            Gallery gallery = new Gallery();
            gallery.setUrl_photo(storedPhoto);
            gallery.setProduct(product); // Associer le produit récupéré à la galerie
            Gallery savedGallery = galleryDaoconst.save(gallery);
            galleryResponses.add(GalleryResponse.fromEntity(savedGallery));
        }

        return galleryResponses;
    }


    @Override
    public List<GalleryResponse> allgallery() {
        return galleryDaoconst.findAll().stream()
                .map(GalleryResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public GalleryResponse galleryById(Long id) {
        return galleryDaoconst.findById(id)
                .map(GalleryResponse::fromEntity)
                .orElseThrow(()->new RuntimeException("gallery not found with this id :"+id));
    }

    @Override
    public GalleryResponse updategallery(GalleryRequest galleryRequest, Long id) {
        Gallery gallery =galleryDaoconst.findById(id).orElseThrow(() ->
                new RuntimeException("gallery not found with this id:"+id));
        if(gallery !=null){
            Gallery g=GalleryResponse.toEntity(galleryRequest);
            g.setId(id);
            g.setUrl_photo(g.getUrl_photo()==null ?g.getUrl_photo():g.getUrl_photo());
            Gallery savedgallery=galleryDaoconst.save(g);
            return  GalleryResponse.fromEntity(savedgallery);
        }else {
            return null;
        }
    }

    @Override
    public HashMap<String, String> deletephotofromgallery(Long id) {
        HashMap message=new HashMap<>();

        Gallery gallery=galleryDaoconst.findById(id).orElse(null);
        if (gallery !=null){
            try{
                // Supprimer l'image physiquement du stockage
                storageService.deleteFile(gallery.getUrl_photo());

                // Supprimer l'enregistrement de la base de données
                galleryDaoconst.delete(gallery);
                message.put("Etat","photo supprimer avec succes");

            }catch (Exception e){
                message.put("Etat","Erreur lors de la suppression "+e.getMessage());
            }
        }
        else{

            message.put("Etat","photo not found with this id: "+id);
        }
        return message;
    }

    @Override
    public HashMap<String, String> deletegallery(Long id) {
        HashMap message=new HashMap<>();
        Gallery g=galleryDaoconst.findById(id).orElse(null);
        if (g !=null){
            try{
                galleryDaoconst.delete(g);
                message.put("Etat","gallery deleted successfully");

            }catch (Exception e){
                message.put("Etat",": "+e.getMessage());
            }
        }
        else{

            message.put("Etat","gallery not found with this id: "+id);
        }
        return message;
    }

    @Override
    public HashMap<String, String> deletePhotoFromGalleryProduit(Long productId, Long galleryId) {
        HashMap message=new HashMap<>();
            try {
                // Rechercher la galerie associée au produit
                Gallery gallery = galleryDaoconst.findById(galleryId)
                        .orElseThrow(() -> new RuntimeException("Gallery not found with id: " + galleryId));

                // Afficher les valeurs pour débogage
                System.out.println("Product ID from request: " + productId);
                System.out.println("Product ID from gallery: " + gallery.getProduct().getId());

                // Vérifier si l'ID du produit correspond à l'ID de la galerie
                if (productId != null && gallery.getProduct() != null && productId.equals(gallery.getProduct().getId())) {
                    // Supprimer la photo
                    galleryDaoconst.delete(gallery);
                    message.put("status", "Photo deleted successfully");
                } else {
                    message.put("status", "Product ID does not match the gallery's product ID");
                }
            } catch (Exception e) {
                message.put("status", "Error: " + e.getMessage());
            }

            return message;
        }


    }
