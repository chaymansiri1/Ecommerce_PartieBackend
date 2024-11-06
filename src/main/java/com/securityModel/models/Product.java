package com.securityModel.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "products")
@Setter @Getter @NoArgsConstructor @AllArgsConstructor

public class Product {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    private String name;
    private String ref;
    private String color;
    private int qte;
    private int qte_en_stock;
    private double price;
    private String description;
    private String image;
    private int qte_user;

    @ManyToMany(mappedBy = "products")
    @JsonIgnoreProperties("products")
    private List<Order> orders = new ArrayList<>();


    @ManyToMany (mappedBy = "products")
    @JsonIgnoreProperties("products")
    private List<Wishlist> wishlists;


    @ManyToMany(mappedBy = "products")
    @JsonIgnoreProperties("products")
    private List<Cart> carts;

    @ManyToOne
    @JsonIgnoreProperties("product")
    @JoinColumn(name = "subcategory_id")
    private Subcategory subcategory;

    @OneToMany(mappedBy = "product")
    @JsonIgnoreProperties("product")
    private Collection<Gallery> gallery;

    @ManyToOne
    @JsonIgnoreProperties("product")
    @JoinColumn(name = "provider_id")
    private Provider provider;

}
