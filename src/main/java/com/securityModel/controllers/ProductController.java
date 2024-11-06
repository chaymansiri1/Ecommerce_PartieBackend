package com.securityModel.controllers;


import com.securityModel.Dtos.request.ProductRequest;
import com.securityModel.Dtos.response.ProductResponse;
import com.securityModel.Utils.StorageServices;
import com.securityModel.models.Product;
import com.securityModel.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
@CrossOrigin("*")
@RestController
@RequestMapping("/products")
public class ProductController {

    public final ProductService productService;

    public ProductController(ProductService productService) {

        this.productService = productService;
    }

    @Autowired
    private StorageServices storage;

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storage.loadFile(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;" +
                        " filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
    //HTTP Content-Disposition informe le navigateur que la réponse contient un fichier à télécharger.
    //attachment; filename="..." indique au navigateur que la réponse doit être traitée comme un fichier téléchargeable,
    // et file.getFilename() spécifie le nom du fichier téléchargé.

    @PostMapping("/save")
    public ProductResponse addproduct(@RequestBody ProductRequest productRequest){
        return productService.createproduct(productRequest);
    }
    @PostMapping("/add/{idSubcategory}/{idProvider}")
    public ProductResponse addProduct(@RequestBody ProductRequest productRequest,
                                      @PathVariable Long idSubcategory,
                                      @PathVariable Long idProvider) {
        return productService.save(productRequest, idSubcategory, idProvider);
    }

@PostMapping("/ajout")
public ProductResponse ajout( ProductRequest productRequest,@RequestParam("file") MultipartFile multipartFile){
        return productService.createProductAvecImage(productRequest,multipartFile);
}
    @PostMapping("/ajoutImgSubPro/{idSubcategory}/{idProvider}")
    public ProductResponse ajoutImgSubPro( ProductRequest productRequest,@PathVariable Long idSubcategory,
                                          @PathVariable Long idProvider,@RequestParam("file")MultipartFile file) {
        return productService.ajoutImgSubPro(productRequest,idSubcategory,idProvider,file);
    }

    @PutMapping("/update/{Id}/{idSub}/{idProv}")
     public ProductResponse updateproductImgSubProv(ProductRequest productRequest,@PathVariable Long Id,@PathVariable Long idSub,@PathVariable Long idProv,@RequestParam("file")MultipartFile file) {

            return productService.updateproductImgSubProv(productRequest,Id,idSub,idProv,file);
    }
    @PutMapping("/updateproductImg/{Id}")
    public ProductResponse updateproductImg(ProductRequest productRequest,@PathVariable Long Id,@RequestParam("file")MultipartFile file) {
        return productService.updateproductImg(productRequest, Id,file);
    }
    @GetMapping("/pagination")
    public Page<Product> pagination(int page, int size){
        return productService.getPaginatedProducts(page,size);
}

    @GetMapping("/all")
    public List<ProductResponse> allproduct(){

        return productService.allproduct();
    }

    @GetMapping("/getbyid/{id}")
    public ProductResponse productById(@PathVariable Long id){
        return productService.productById(id);
    }

    @DeleteMapping("/delete/{id}")
    public HashMap<String, String> deleteproduct(@PathVariable Long id){

        return productService.deleteproduct(id);
    }


    @PutMapping("/update/{id}")
    public ProductResponse updateproduct(@RequestBody ProductRequest productRequest, @PathVariable Long id){
        return productService.updateproduct(productRequest,id);
    }


    // Rechercher par nom
    @GetMapping("/searchByName")
    public List<ProductResponse> searchByName(@RequestParam String name) {
        return productService.findByName(name);
    }

    // Rechercher par prix
    @GetMapping("/searchByPrice")
    public List<ProductResponse> searchByPrice(@RequestParam double minPrice, @RequestParam double maxPrice) {
        return productService.findByPriceBetween(minPrice, maxPrice);
    }

    // Rechercher par nom et prix
    @GetMapping("/searchByNameAndPrice")
    public List<ProductResponse> searchByNameAndPrice(@RequestParam String name, @RequestParam double minPrice, @RequestParam double maxPrice) {
        return productService.findByNameAndPriceBetween(name, minPrice, maxPrice);
    }

}


