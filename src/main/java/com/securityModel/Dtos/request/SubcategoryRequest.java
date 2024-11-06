package com.securityModel.Dtos.request;


import com.securityModel.models.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubcategoryRequest {
    private String name;
    private  String description;

    private Category category;
}
