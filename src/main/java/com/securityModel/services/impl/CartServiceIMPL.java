package com.securityModel.services.impl;


import com.securityModel.Dtos.response.CartResponse;
import com.securityModel.Dtos.response.ProductResponse;
import com.securityModel.models.Cart;
import com.securityModel.models.Product;
import com.securityModel.repository.CartDao;
import com.securityModel.repository.ProductDao;
import com.securityModel.services.CartService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceIMPL implements CartService {
    private final ProductDao productDaoconst;
    private final CartDao cartDaoconst;
    // private final UserDao userDaoconst;

    public CartServiceIMPL(ProductDao productDaoconst, CartDao cartDaoconst) {
        this.productDaoconst = productDaoconst;
        this.cartDaoconst = cartDaoconst;

    }

    @Override
    public HashMap<String, String> addProductToCart(Long productId, int quantity) {
        HashMap response = new HashMap<>();
        Optional<Product> productOpt = productDaoconst.findById(productId);
        //trouver ou creer une carte
        Cart cart = cartDaoconst.findAll().stream().findFirst().orElse(new Cart());
        if (productOpt.isPresent()) {
            Product product = productOpt.get();

            // Vérifier si la quantité est disponible
            if (quantity <= product.getQte_en_stock()) {


                if (!cart.getProducts().contains(product)) {
                    // Si le produit est déjà dans le panier, mettre à jour la quantité
                    cart.getProducts().add(product);
                    product.setQte_user(quantity);
                    cart.setQtetotal(cart.getQtetotal() + quantity);

                    cart.calculateTotalPrice();
                    product.setQte_en_stock(product.getQte_en_stock() - quantity);
                    productDaoconst.save(product);
                    cartDaoconst.save(cart);

                    response.put("status", "Success");
                    response.put("message", "Product added to cart successfully");
                } else {
                    response.put("status", "error");
                    response.put("message", "Quantity insufficient"); }
            }else {
                    response.put("status", "error");
                    response.put("message", "Quantity insufficient");
            }


        } else {
                response.put("status", "error");
                response.put("message", "Product not found");
        }

            return response;


    }
    @Override
    public HashMap<String, String> removeProductFromCart(Long productId) {
        HashMap response = new HashMap<>();
        Optional<Product> productOpt = productDaoconst.findById(productId);

        // Trouver ou créer un panier
        Cart cart = cartDaoconst.findAll().stream().findFirst().orElse(null);
        if (cart == null) {
            response.put("status", "Error");
            response.put("message", "Cart not found");
            return response;
        }

        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            //verifier si le product et dans la carte
            if (cart.getProducts().contains(product)) {

                cart.getProducts().remove(product);
                cart.setQtetotal(cart.getQtetotal() - product.getQte_user());

                productDaoconst.save(product);
                //Verifier si la carte est maintenant vide
                if (cart.getProducts().isEmpty()) {
                    //supprimer la carte si elle est vide
                    cartDaoconst.delete(cart);
                    response.put("status", "Success");
                    response.put("mesaage", "product removed and cart deleted as it was empty");
                } else {
                    //recalculer le prix total de la carte
                    cart.calculateTotalPrice();
                    cartDaoconst.save(cart);

                response.put("status", "Success");
                response.put("message", "Product removed from cart successfully"); }
            } else {
                response.put("status", "error");
                response.put("message", "Product not found in cart");
            }
        } else {
            response.put("status", "error");
            response.put("message", "Product not found");
        }

        return response;
    }

//    @Override
//    public List<ProductResponse> getCartProducts() {
//
//        Cart cart = cartDaoconst.findAll().stream().findFirst()
//                .orElseThrow(() -> new RuntimeException("Cart not found"));
//
//        return cart.getProducts().stream()
//                .map(ProductResponse::fromEntity)
//                .collect(Collectors.toList());
//    }
@Override
public CartResponse getCartProductss() {

    // Récupérer le premier panier disponible
    Cart cart = cartDaoconst.findAll().stream().findFirst()
            .orElseThrow(() -> new RuntimeException("Cart not found"));

    // Convertir l'entité Cart en CartResponse
    return CartResponse.fromEntity(cart);
}

    @Override
    public CartResponse clearCart() {
        Cart cart = cartDaoconst.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // Vider le panier
        cart.getProducts().clear();

        cart.calculateTotalPrice(); // Mettre à jour le prix total après vidage
        Cart savedCart = cartDaoconst.save(cart);
        return CartResponse.fromEntity(savedCart);
    }
}
