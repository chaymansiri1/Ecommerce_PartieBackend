package com.securityModel.services.impl;


import com.securityModel.Dtos.request.CategoryRequest;
import com.securityModel.Dtos.response.CategoryResponse;
import com.securityModel.models.Category;
import com.securityModel.repository.CategoryDao;
import com.securityModel.services.CategoryService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceIMPL implements CategoryService {

    private final CategoryDao categoryDaoconst;

    public CategoryServiceIMPL(CategoryDao categoryDaoconst) {
        this.categoryDaoconst = categoryDaoconst;
    }

    @Override
    public CategoryResponse createcategory(CategoryRequest category) {
        Category c= CategoryResponse.toEntity(category);
        Category savedcategory=categoryDaoconst.save(c);
        return CategoryResponse.fromEntity(savedcategory);
    }

    @Override
    public List<CategoryResponse> allcategory() {
        return categoryDaoconst.findAll().stream()
                .map(CategoryResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse getById(Long id) {
        return categoryDaoconst.findById(id)
                .map(CategoryResponse::fromEntity)
                .orElseThrow(()->new RuntimeException("category not found with this id :"+id));
    }

    @Override
    public CategoryResponse updatecategory(CategoryRequest categoryRequest, Long id) {
        Category category =categoryDaoconst.findById(id).orElseThrow(() ->
                new RuntimeException("category not found with this id:"+id));
        if(category !=null){
            Category c= CategoryResponse.toEntity(categoryRequest);
            c.setId(id);
            c.setName(c.getName()==null ?category.getName():c.getName());
            Category savedcategory=categoryDaoconst.save(c);
            return  CategoryResponse.fromEntity(savedcategory);
        }else {
            return null;
        }
    }

    @Override
    public HashMap<String, String> deletecategory(Long id) {
      HashMap message= new HashMap<>();
      Category c=categoryDaoconst.findById(id).orElse(null);
      if(c!=null){
          try {
              categoryDaoconst.delete(c);
              message.put("Etat", "Category deleted");
          }catch (Exception e){
              message.put("Etat",":"+e.getMessage());
          }

      }else{
          message.put("Etat","Category not found with id:"+id);
      }
      return message;
    }
}

