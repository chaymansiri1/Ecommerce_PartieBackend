package com.securityModel.services.impl;


import com.securityModel.Dtos.request.SubcategoryRequest;
import com.securityModel.Dtos.response.SubcategoryResponse;
import com.securityModel.models.Category;
import com.securityModel.models.Subcategory;
import com.securityModel.repository.CategoryDao;
import com.securityModel.repository.SubcategoryDao;
import com.securityModel.services.SubcategoryService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class SubcategoryServiceIMPL implements SubcategoryService {

    private final CategoryDao categoryDaoconst;
    private final SubcategoryDao subcategoryDaoconst ;

    public SubcategoryServiceIMPL(CategoryDao categoryDaoconst, SubcategoryDao subcategoryDaoconst) {
        this.categoryDaoconst = categoryDaoconst;
        this.subcategoryDaoconst = subcategoryDaoconst;
    }

    @Override
    public SubcategoryResponse createsubcategory(SubcategoryRequest subcategory) {
        Subcategory sub= SubcategoryResponse.toEntity(subcategory);
        Subcategory savedsubcategory=subcategoryDaoconst.save(sub);
        return SubcategoryResponse.fromEntity(savedsubcategory);
    }
    @Override
    public List<SubcategoryResponse> allsubcategory() {
        return subcategoryDaoconst.findAll().stream()
                .map(SubcategoryResponse::fromEntity)
                .collect(Collectors.toList());
    }
    @Override
    public SubcategoryResponse subcategoryById(Long id) {
        return subcategoryDaoconst.findById(id)
                .map(SubcategoryResponse::fromEntity)
                .orElseThrow(()->new RuntimeException("product not found with this id :"+id));
    }

    @Override
    public SubcategoryResponse createSubcategorieCategory(SubcategoryRequest subcategoryRequest, Long categoryId) {
        // Récupérer la catégorie à partir de l'ID fourni

        Category category = categoryDaoconst.findById(categoryId).orElseThrow(() ->
                new RuntimeException("Catégorie non trouvée avec cet identifiant : " + categoryId));

        // Créer une nouvelle sous-catégorie
        Subcategory subcategory = SubcategoryResponse.toEntity(subcategoryRequest);
        subcategory.setCategory(category); // Associer la catégorie

        // Sauvegarder la sous-catégorie dans la base de données
        Subcategory savedSubcategory = subcategoryDaoconst.save(subcategory);

        // Retourner la réponse
        return SubcategoryResponse.fromEntity(savedSubcategory);
    }

    @Override
    public SubcategoryResponse updatesubcategory(SubcategoryRequest subcategoryRequest, Long id) {
        Subcategory subcategory =subcategoryDaoconst.findById(id).orElseThrow(() ->
                new RuntimeException("subcategory not found with this id:"+id));
        if(subcategory !=null){
            Subcategory s= SubcategoryResponse.toEntity(subcategoryRequest);
            s.setId(id);
            s.setName(s.getName()==null ?subcategory.getName():s.getName());
            Subcategory savedsubcategory=subcategoryDaoconst.save(s);
            return  SubcategoryResponse.fromEntity(savedsubcategory);
        }else {
            return null;
        }
    }
    @Override
    public SubcategoryResponse update(SubcategoryRequest subcategoryRequest, Long id, Long idCategory) {
        Subcategory subcategory = subcategoryDaoconst.findById(id).orElseThrow(() ->
                new RuntimeException("subcategory not found with tis id:" + id));
        //idCategory=null :maaneha ma7atite8 fl url /idcat
        if (idCategory != null) {
            Category c = categoryDaoconst.findById(idCategory).orElse(null);
            //chntasty aaleha
            if (c != null) {
                subcategoryRequest.setCategory(c);
            } else {
                new RuntimeException("category not found with this id:" + id);
            }
        } else {
            subcategoryRequest.setCategory(subcategoryRequest.getCategory() == null ? subcategory.getCategory() : subcategoryRequest.getCategory());
        }
        if (subcategory != null) {
            Subcategory subcategory1 = SubcategoryResponse.toEntity(subcategoryRequest);
            subcategory1.setId(id);
            //subcategory1.setName(subcategory1.getName()==null?subcategory.getName():subcategory1.getName());
            Subcategory savedsub = subcategoryDaoconst.save(subcategory1);
            return SubcategoryResponse.fromEntity(savedsub);
        } else {
           throw  new RuntimeException("subcategory not found ");
        }
    }
    @Override
    public HashMap<String, String> deletesubcategory(Long id) {
        HashMap message=new HashMap<>();
        Subcategory s=subcategoryDaoconst.findById(id).orElse(null);
        if (s !=null){
            try{
                subcategoryDaoconst.delete(s);
                message.put("Etat","subcategory deleted successfully");

            }catch (Exception e){
                message.put("Etat",": "+e.getMessage());
            }
        }
        else{

            message.put("Etat","subcategory not found with this id: "+id);
        }
        return message;
    }


}
