package com.securityModel.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "subcategories")

public class Subcategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private  String description;


    @ManyToOne
    @JsonIgnoreProperties("subcategories")
    @JoinColumn(name = "category_id")
    private Category category;


    @OneToMany(mappedBy = "subcategory")
    @JsonIgnoreProperties("subcategory")
    private Collection<Product> product;

}
