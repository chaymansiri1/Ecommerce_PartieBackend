package com.securityModel.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "oders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ref;
    private String description;
    private int qte_total;
    private double price_total;
    private boolean state;
    private Date orderDate;



    @ManyToMany
    @JsonIgnoreProperties("orders")
    @JoinTable(name = "Order_product",
    joinColumns = @JoinColumn(name = "order_id"),
    inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products = new ArrayList<>();

    // Méthode pour ajouter un produit
    public void addProduct(Product product) {
        if (this.products == null) {
            this.products = new ArrayList<>();
        }
        this.products.add(product);
    }

    @ManyToOne
    @JsonIgnoreProperties("order")
    @JoinColumn(name = "client_id")
    private Customer customer;

    @ManyToOne
    @JsonIgnoreProperties("order")
    @JoinColumn(name = "driver_id")
    private Driver driver;


    // Relation avec User
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Relation avec Cart
    @OneToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
}
