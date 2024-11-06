package com.securityModel.controllers;


import com.securityModel.Dtos.request.SubcategoryRequest;
import com.securityModel.Dtos.response.SubcategoryResponse;
import com.securityModel.services.SubcategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
@CrossOrigin("*")
@RestController
@RequestMapping("/subcategories")
public class SubcategoryController {
    public final SubcategoryService subcategoryService;

    public SubcategoryController(SubcategoryService subcategoryService) {
        this.subcategoryService = subcategoryService;
    }


    @PostMapping("/save")
    public SubcategoryResponse addsubcategory(@RequestBody SubcategoryRequest productRequest){
        return subcategoryService.createsubcategory(productRequest);
    }

    @PostMapping("/save/{id_cat}")
    public SubcategoryResponse addSubcategoryToCategory(@RequestBody SubcategoryRequest subcategoryRequest, @PathVariable("id_cat") Long categoryId) {
        return subcategoryService.createSubcategorieCategory(subcategoryRequest, categoryId);
    }

    @GetMapping("/all")
    public List<SubcategoryResponse> allsubcategory(){
        return subcategoryService.allsubcategory();
    }


    @GetMapping("/getbyid/{id}")
    public SubcategoryResponse subcategoryById(@PathVariable Long id){
        return subcategoryService.subcategoryById(id);
    }

    @DeleteMapping("/delete/{id}")
    public HashMap<String, String> deletesubcategory(@PathVariable Long id){

        return subcategoryService.deletesubcategory(id);
    }


    @PutMapping("/update/{id}")
    public SubcategoryResponse updatesubcategory (@RequestBody SubcategoryRequest subcategoryRequest , @PathVariable Long id){
        return subcategoryService.updatesubcategory(subcategoryRequest,id);
    }
    @PutMapping(value = {"/modif/{id}", "/modif/{id}/{idCategory}"})
    public SubcategoryResponse modif(@RequestBody SubcategoryRequest subcategoryRequest, @PathVariable Long id,@PathVariable(required = false) Long idCategory){
        return subcategoryService.update(subcategoryRequest,id,idCategory);
    }


}
