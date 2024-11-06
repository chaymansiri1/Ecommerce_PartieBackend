package com.securityModel.services.impl;



import com.securityModel.Dtos.request.ProductRequest;
import com.securityModel.Dtos.response.ProductResponse;
import com.securityModel.Utils.StorageServices;
import com.securityModel.models.Product;
import com.securityModel.models.Provider;
import com.securityModel.models.Subcategory;
import com.securityModel.repository.CategoryDao;
import com.securityModel.repository.ProductDao;
import com.securityModel.repository.ProviderRepository;
import com.securityModel.repository.SubcategoryDao;
import com.securityModel.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class ProductServiceIMPL implements ProductService {

    private final ProductDao productDaoconst;
    private final StorageServices storageService;
    public ProductServiceIMPL(ProductDao productDaoconst, StorageServices storageService, SubcategoryDao subcategoryDaoconst, CategoryDao categoryDaoconst, ProviderRepository providerDaoconst) {

        this.productDaoconst = productDaoconst;
        this.storageService = storageService;
        this.subcategoryDaoconst = subcategoryDaoconst;
        this.categoryDaoconst = categoryDaoconst;

        this.providerDaoconst = providerDaoconst;
    }

    private final SubcategoryDao subcategoryDaoconst;
    private final CategoryDao categoryDaoconst;
    private final ProviderRepository providerDaoconst;

    @Override
    public ProductResponse createproduct(ProductRequest product) {
        Product p=ProductResponse.toEntity(product);
        p.setQte_en_stock(product.getQte());
        Product savedproduct=productDaoconst.save(p);
        return ProductResponse.fromEntity(savedproduct);
    }
    @Override
    public ProductResponse createProductAvecImage(ProductRequest product, MultipartFile multipartFile) {
        Product p=ProductResponse.toEntity(product);
        p.setName(product.getName());
        p.setRef(product.getRef());
        p.setColor(product.getColor());
        p.setQte(product.getQte());
        p.setQte_en_stock(product.getQte_en_stock());
        p.setPrice(product.getPrice());
        p.setDescription(product.getDescription());

        String img=storageService.store(multipartFile);
        p.setImage(img);
        Product savedproduct=productDaoconst.save(p);
        return ProductResponse.fromEntity(savedproduct);
    }


    @Override
    public ProductResponse save(ProductRequest productRequest, Long idSubcategory, Long idProvider) {

            // Trouver la sous-catégorie par son ID
            Subcategory subcategory = subcategoryDaoconst.findById(idSubcategory)
                    .orElseThrow(() -> new RuntimeException("Subcategory not found with this id: " + idSubcategory));

            // Trouver le fournisseur par son ID
           Provider provider = providerDaoconst.findById(idProvider)
                    .orElseThrow(() -> new RuntimeException("Provider not found with this id: " + idProvider));
        productRequest.setSubcategory(subcategory);  // Associer la sous-catégorie
        productRequest.setProvider(provider);
        // Créer le produit à partir de la requête
            Product product = ProductResponse.toEntity(productRequest);
                // Associer le fournisseur

            // Sauvegarder le produit
            Product savedProduct = productDaoconst.save(product);

            // Retourner la réponse
            return ProductResponse.fromEntity(savedProduct);
        }

    @Override
    public ProductResponse ajoutImgSubPro(ProductRequest productRequest, Long idSubcategory, Long idProvider, MultipartFile file) {
        // Trouver la sous-catégorie par son ID
        Subcategory subcategory = subcategoryDaoconst.findById(idSubcategory)
                .orElseThrow(() -> new RuntimeException("Subcategory not found with this id: " + idSubcategory));

        // Trouver le fournisseur par son ID
        Provider provider = providerDaoconst.findById(idProvider)
                .orElseThrow(() -> new RuntimeException("Provider not found with this id: " + idProvider));


        productRequest.setSubcategory(subcategory);  // Associer la sous-catégorie
        productRequest.setProvider(provider);
        String img=storageService.store(file);
        productRequest.setImage(img);
        // Créer le produit à partir de la requête
        Product product = ProductResponse.toEntity(productRequest);
        // Associer le fournisseur

        // Sauvegarder le produit
        Product savedProduct = productDaoconst.save(product);

        // Retourner la réponse
        return ProductResponse.fromEntity(savedProduct);
    }

    @Override
    public ProductResponse updateproductImgSubProv(ProductRequest productRequest, Long Id, Long idSub, Long idProv, MultipartFile file) {
            // Trouver le produit par son ID
            Product p = productDaoconst.findById(Id)
                    .orElseThrow(() -> new RuntimeException("Produit non trouvé avec cet ID: " + Id));

            // Trouver la sous-catégorie par son ID
            Subcategory sc1 = subcategoryDaoconst.findById(idSub)
                    .orElseThrow(() -> new RuntimeException("Sous-catégorie non trouvée avec cet ID: " + idSub));

            // Trouver le fournisseur par son ID
            Provider pr1 = providerDaoconst.findById(idProv)
                    .orElseThrow(() -> new RuntimeException("Fournisseur non trouvé avec cet ID: " + idProv));

            // Mettre à jour les informations du produit
            p.setName(productRequest.getName());
            p.setRef(productRequest.getRef());
            p.setColor(productRequest.getColor());
            p.setQte(productRequest.getQte());
            p.setQte_en_stock(productRequest.getQte_en_stock());
            p.setPrice(productRequest.getPrice());
            p.setDescription(productRequest.getDescription());
            p.setSubcategory(sc1);  // Associer la nouvelle sous-catégorie
            p.setProvider(pr1);  // Associer le nouveau fournisseur

            // Si un fichier image est fourni, mettre à jour l'image
            if (file != null && !file.isEmpty()) {
                String img = storageService.store(file);
                p.setImage(img);  // Mettre à jour l'image du produit
            }

            // Sauvegarder le produit mis à jour
            Product savedProduct = productDaoconst.save(p);

            // Retourner la réponse avec les informations du produit mis à jour
            return ProductResponse.fromEntity(savedProduct);
        }

    @Override
    public ProductResponse updateproductImg(ProductRequest productRequest, Long Id, MultipartFile file) {
        // Trouver le produit par son ID
        Product p = productDaoconst.findById(Id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec cet ID: " + Id));


        // Mettre à jour les informations du produit
        p.setName(productRequest.getName());
        p.setRef(productRequest.getRef());
        p.setColor(productRequest.getColor());
        p.setQte(productRequest.getQte());
        p.setQte_en_stock(productRequest.getQte_en_stock());
        p.setPrice(productRequest.getPrice());
        p.setDescription(productRequest.getDescription());

        // Si un fichier image est fourni, mettre à jour l'image
        if (file != null && !file.isEmpty()) {
            String img = storageService.store(file);
            p.setImage(img);  // Mettre à jour l'image du produit
        }

        // Sauvegarder le produit mis à jour
        Product savedProduct = productDaoconst.save(p);

        // Retourner la réponse avec les informations du produit mis à jour
        return ProductResponse.fromEntity(savedProduct);

    }

    @Override
    public List<ProductResponse> allproduct() {
        return productDaoconst.findAll().stream()
                .map(ProductResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Product> getPaginatedProducts(int page, int size) {
        Pageable pageable= PageRequest.of(page,size);
        return productDaoconst.findAll(pageable);
    }

    @Override
    public ProductResponse productById(Long id) {
       return productDaoconst.findById(id)
               .map(ProductResponse::fromEntity)
               .orElseThrow(()->new RuntimeException("product not found with this id :"+id));
    }

    @Override
    public ProductResponse updateproduct(ProductRequest productRequest, Long id) {
        Product product=productDaoconst.findById(id).orElseThrow(() ->
            new RuntimeException("product not found with this id:"+id));
        if(product !=null){
            Product p=ProductResponse.toEntity(productRequest);
            p.setId(id);
            p.setName(p.getName()==null ?product.getName():p.getName());
            p.setRef(p.getRef()==null ?product.getRef():p.getRef());
            p.setColor(p.getColor()==null ?product.getColor():p.getColor());
            p.setQte(p.getQte()==0 ?product.getQte():p.getQte());
            p.setQte_en_stock(p.getQte_en_stock()==0 ?product.getQte_en_stock():p.getQte_en_stock());
            p.setPrice(p.getPrice()==0 ?product.getPrice():p.getPrice());
            p.setDescription(p.getDescription()==null ?product.getDescription():p.getDescription());
            Product savedproduct=productDaoconst.save(p);
            return  ProductResponse.fromEntity(savedproduct);
        }else {
            return null;
        }


    }

    @Override
    public HashMap<String, String> deleteproduct(Long id) {
     HashMap message=new HashMap<>();
     Product p=productDaoconst.findById(id).orElse(null);
     if (p !=null){
         try{
             productDaoconst.delete(p);
             message.put("Etat","product deleted successfully");

         }catch (Exception e){
             message.put("Etat",": "+e.getMessage());
         }
     }
     else{

         message.put("Etat","product not found with this id: "+id);
     }
     return message;
    }


    // Filtrer par nom
    public List<ProductResponse> findByName(String name) {
        return productDaoconst.findByName(name).stream()
                .map(ProductResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // Filtrer par prix
    public List<ProductResponse> findByPriceBetween(double minPrice, double maxPrice) {
        return productDaoconst.findByPriceBetween(minPrice, maxPrice).stream()
                .map(ProductResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // Filtrer par nom et prix
    public List<ProductResponse> findByNameAndPriceBetween(String name, double minPrice, double maxPrice) {
        return productDaoconst.findByNameAndPriceBetween(name, minPrice, maxPrice).stream()
                .map(ProductResponse::fromEntity)
                .collect(Collectors.toList());
    }

}
