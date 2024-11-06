package com.securityModel.services;



import com.securityModel.Dtos.request.ProductRequest;
import com.securityModel.Dtos.response.ProductResponse;
import com.securityModel.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

public interface ProductService {


    ProductResponse createproduct(ProductRequest product);
    ProductResponse createProductAvecImage(ProductRequest product, MultipartFile multipartFile);
    ProductResponse save(ProductRequest productRequest,Long idSubcategory,Long idProvider);
    ProductResponse ajoutImgSubPro(ProductRequest productRequest,Long idSubcategory,Long idProvider, MultipartFile file);

    ProductResponse updateproductImgSubProv(ProductRequest productRequest,@PathVariable Long Id,@PathVariable Long idSub,@PathVariable Long idProv,@RequestParam("file") MultipartFile file);
    ProductResponse updateproductImg(ProductRequest productRequest,@PathVariable Long Id,@RequestParam("file") MultipartFile file);
        List<ProductResponse> allproduct();
    Page<Product> getPaginatedProducts(int page, int size);
    ProductResponse productById(Long id);
    ProductResponse updateproduct(ProductRequest productRequest,Long id);

    HashMap<String,String> deleteproduct(Long id);

    List<ProductResponse> findByName(String name);
    List<ProductResponse> findByPriceBetween(double minPrice, double maxPrice);
    List<ProductResponse> findByNameAndPriceBetween(String name, double minPrice, double maxPrice);

}
