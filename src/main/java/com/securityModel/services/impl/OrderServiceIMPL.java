package com.securityModel.services.impl;


import com.securityModel.Dtos.request.OrderRequest;
import com.securityModel.Dtos.response.OrderResponse;
import com.securityModel.models.*;
import com.securityModel.repository.*;
import com.securityModel.services.OrderService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceIMPL implements OrderService {
    private final OrderDao orderDaoconst;
    private final CustomerRepository clientDaoconst;
    private final ProductDao productDaoconst;
    private final CartDao cartDaoconst;
    private final UserRepository userDaoconst;

    public OrderServiceIMPL(OrderDao orderDaoconst, CustomerRepository clientDaoconst, ProductDao productDaoconst, CartDao cartDaoconst, UserRepository userDaoconst) {
        this.orderDaoconst = orderDaoconst;
        this.clientDaoconst = clientDaoconst;
        this.productDaoconst = productDaoconst;
        this.cartDaoconst = cartDaoconst;
        this.userDaoconst = userDaoconst;
    }

    @Override
    public OrderResponse createorder(OrderRequest order) {
        Order o = OrderResponse.toEntity(order);
        Order savedorder = orderDaoconst.save(o);
        return OrderResponse.fromEntity(savedorder);
    }

    @Override
    public OrderResponse save(OrderRequest orderRequest, Long clientId) {

        // Récupérer le client à partir de l'ID fourni
        Customer client = clientDaoconst.findById(clientId).orElseThrow(() ->
                new RuntimeException("Client non trouvé avec cet identifiant : " + clientId));

        Order order = new Order();
        orderRequest.setClient(client); // Associer le client


        // Récupérer chaque produit à partir de la liste d'IDs fournie et les ajouter à la commande
         /* for (Long productId : productIds) {
                Product product = productDaoconst.findById(productId).orElseThrow(() ->
                        new RuntimeException("Produit non trouvé avec cet identifiant : " + productId));
                order.addProduct(product); // Associer le produit
                System.out.println("Produit ajouté : " + product.getId()); // Ajouter des logs
            }

          */

        // Sauvegarder la commande dans la base de données
        Order savedOrder = orderDaoconst.save(order);
        System.out.println("Commande sauvegardée avec ID : " + savedOrder.getId()); // Ajouter des logs
        // Retourner la réponse sous forme d'OrderResponse
        return OrderResponse.fromEntity(savedOrder);
    }


    @Override
    public List<OrderResponse> allorder() {
        return orderDaoconst.findAll().stream()
                .map(OrderResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse orderById(Long id) {

        return orderDaoconst.findById(id)
                .map(OrderResponse::fromEntity)
                .orElseThrow(() -> new RuntimeException("order not found with this id :" + id));

    }

    @Override
    public OrderResponse updateorder(OrderRequest orderRequest, Long id) {
        Order order = orderDaoconst.findById(id).orElseThrow(() ->

                new RuntimeException("product not found with this id:" + id));
        if (order != null) {
            Order o = OrderResponse.toEntity(orderRequest);
            o.setId(id);
            o.setDescription(o.getDescription() == null ? order.getDescription() : o.getDescription());
            Order savedorder = orderDaoconst.save(o);
            return OrderResponse.fromEntity(savedorder);
        } else {
            return null;
        }


    }

    @Override
    public HashMap<String, String> deleteorder(Long id) {
        HashMap message = new HashMap<>();
        Order o = orderDaoconst.findById(id).orElse(null);
        if (o != null) {
            try {
                orderDaoconst.delete(o);
                message.put("Etat", "order deleted successfully");

            } catch (Exception e) {
                message.put("Etat", ": " + e.getMessage());
            }
        } else {

            message.put("Etat", "order not found with this id: " + id);
        }
        return message;
    }

    @Override
    public OrderResponse passerOrder(Long customerId, Long cartId) {

        // Obtenir Customer par son ID
        Customer customer  = clientDaoconst.findById(customerId).orElseThrow(() ->
                new RuntimeException("Client non trouvé avec cet identifiant : " + customerId));
        Cart cart = cartDaoconst.findById(cartId).orElseThrow(() ->
                new RuntimeException("Cart non trouvé avec cet identifiant : " + cartId));


        //cart.calculateTotalPrice();

        // Créer l'ordre avec l'utilisateur et le panier
            Order order = new Order();
            order.setCustomer(customer); // Associe le customer à la commande
            order.setCart(cart);
            order.setQte_total(cart.getQtetotal());
            order.setPrice_total(cart.getPrice_total()); // Associer le prix total à la commande
            order.setOrderDate(new Date()); // Ajouter la date de la commande


        // Initialiser la variable pour la quantité totale
        int qteTotal = 0;

        // Parcourir tous les produits dans le panier
        for (Product product : cart.getProducts()) {
            int orderedQuantity = product.getQte_user();  // Quantité commandée
            if (product.getQte_en_stock() >= orderedQuantity) {
                // Décrémenter la quantité en stock du produit
                product.setQte_en_stock(product.getQte_en_stock() - orderedQuantity);
                // Sauvegarder les modifications du produit
                productDaoconst.save(product);
                // Ajouter le produit à la commande
                order.addProduct(product);
            } else {
                throw new RuntimeException("Quantité en stock insuffisante pour le produit : " + product.getName());
            }
        }

            // Sauvegarder l'ordre
            Order savedOrder = orderDaoconst.save(order);
        cartDaoconst.deleteById(cartId);

            // Retourner la réponse d'ordre
            return OrderResponse.fromEntity(savedOrder);
        }

    public List<Order> getOrdersByCustomerId(Long customerId) {
        return orderDaoconst.findByCustomerId(customerId);
    }


}