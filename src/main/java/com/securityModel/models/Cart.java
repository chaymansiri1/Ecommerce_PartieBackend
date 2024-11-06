package com.securityModel.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "carts")

public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int qtetotal;
    private double price_total;


    // Relation avec Order
    @OneToOne(mappedBy = "cart")
    private Order order;

    @ManyToMany
    @JoinTable(name = "cart_product",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products = new ArrayList<>();
    public void calculateTotalPrice(){
        double totalPrice=this.products.stream()
                .mapToDouble(product ->product.getPrice()*product.getQte_user())
                .sum();
        this.price_total=totalPrice;
    }
    // Méthode pour ajouter un produit au panier
    public void addProduct(Product product) {
        this.products.add(product);
    }
    // Méthode pour supprimer un produit du panier
    public void removeProduct(Product product) {
        this.products.remove(product);
    }
    // Méthode pour vider le panier
    public void clear() {
        this.products.clear();
    }


}
