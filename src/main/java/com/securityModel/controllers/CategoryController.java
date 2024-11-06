package com.securityModel.controllers;


import com.securityModel.Dtos.request.CategoryRequest;
import com.securityModel.Dtos.response.CategoryResponse;
import com.securityModel.services.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
@CrossOrigin("*")
@RestController
@RequestMapping("/categories")
public class CategoryController {
    public final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/save")
    public CategoryResponse addcategory(@RequestBody CategoryRequest categoryRequest){
        return categoryService.createcategory(categoryRequest);
    }

    @GetMapping("/all")
    public List<CategoryResponse> allcategory(){
        return categoryService.allcategory();
    }

    @GetMapping("/getById/{id}")
    public CategoryResponse getcategorybyid(@PathVariable Long id){
        return categoryService.getById(id);
    }

    @PutMapping("/update/{id}")
    public CategoryResponse updatecategory(@RequestBody CategoryRequest categoryRequest,@PathVariable Long id){
        return categoryService.updatecategory(categoryRequest,id);
    }

    @DeleteMapping("/delete/{id}")
    public HashMap<String, String> deletecategory(@PathVariable Long id){

        return categoryService.deletecategory(id);
    }

}
