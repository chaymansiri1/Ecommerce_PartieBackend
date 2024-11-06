package com.securityModel.repository;


import com.securityModel.models.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubcategoryDao extends JpaRepository<Subcategory,Long> {
}
