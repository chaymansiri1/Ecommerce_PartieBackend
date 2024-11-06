package com.securityModel.Dtos.response;


import com.securityModel.Dtos.request.SubcategoryRequest;
import com.securityModel.models.Category;
import com.securityModel.models.Subcategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubcategoryResponse {
    private Long id;
    private String name;
    private  String description;
    private Category category;

    public static SubcategoryResponse fromEntity(Subcategory entity){

        SubcategoryResponse subcategoryResponse =new SubcategoryResponse();
        BeanUtils.copyProperties(entity,subcategoryResponse);
        return subcategoryResponse;
        //l'objet ProductResponse c'est une version "allégée" de l'entité, adaptée pour être renvoyée au client.
    }


    //Elle prend un objet ProductRequest (un autre DTO) et le transforme en une entité Product.
    public static Subcategory toEntity(SubcategoryRequest subcategoryResponse ){
        Subcategory sub =new Subcategory();
        BeanUtils.copyProperties(subcategoryResponse,sub);
        return sub;
    }

}
