package com.securityModel.services;



import com.securityModel.Dtos.request.SubcategoryRequest;
import com.securityModel.Dtos.response.SubcategoryResponse;

import java.util.HashMap;
import java.util.List;

public interface SubcategoryService {
   SubcategoryResponse createsubcategory(SubcategoryRequest subcategory );

   SubcategoryResponse createSubcategorieCategory(SubcategoryRequest subcategoryRequest, Long id);
    List<SubcategoryResponse> allsubcategory();
    SubcategoryResponse subcategoryById(Long id);
    SubcategoryResponse updatesubcategory(SubcategoryRequest subcategoryRequest, Long id);
    SubcategoryResponse update(SubcategoryRequest subcategoryRequest,Long id,Long idCategory);
    HashMap<String,String> deletesubcategory(Long id);
}
