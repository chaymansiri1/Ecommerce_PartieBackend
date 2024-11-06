package com.securityModel.services;


import com.securityModel.Dtos.request.CategoryRequest;
import com.securityModel.Dtos.response.CategoryResponse;

import java.util.HashMap;
import java.util.List;

public interface CategoryService {
    CategoryResponse createcategory(CategoryRequest category);
    List<CategoryResponse> allcategory();

    CategoryResponse getById(Long id);
    CategoryResponse updatecategory(CategoryRequest categoryRequest, Long id);

    HashMap<String,String> deletecategory(Long id);

}
