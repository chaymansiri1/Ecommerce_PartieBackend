package com.securityModel.Dtos.response;


import com.securityModel.Dtos.request.CategoryRequest;
import com.securityModel.models.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private String name;
    private String description;

private List<SubcategoryResponse> subcategories =new ArrayList<>();


    public static CategoryResponse fromEntity(Category entity){

        CategoryResponse categoryResponse=new CategoryResponse();
        BeanUtils.copyProperties(entity,categoryResponse);
        if (entity.getSubcategories()!=null){
            categoryResponse.setSubcategories(entity.getSubcategories().stream()
                    .map(SubcategoryResponse::fromEntity)
                    .collect(Collectors.toList()));
        }
        return categoryResponse;
        //l'objet ProductResponse c'est une version "allégée" de l'entité, adaptée pour être renvoyée au client.
    }


    //Elle prend un objet ProductRequest (un autre DTO) et le transforme en une entité Product.
    public static Category toEntity(CategoryRequest categoryResponse){
        Category c =new Category();
        BeanUtils.copyProperties(categoryResponse,c);
        return c;
    }

}

