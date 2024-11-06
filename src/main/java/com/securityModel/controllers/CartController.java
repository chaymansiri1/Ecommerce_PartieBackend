package com.securityModel.controllers;


import com.securityModel.Dtos.response.CartResponse;
import com.securityModel.Dtos.response.ProductResponse;
import com.securityModel.services.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
@CrossOrigin("*")
@RestController
@RequestMapping("/carts")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }


    @PostMapping("/add/{productId}")
    public HashMap<String, String> addProductToCart( @PathVariable Long productId,int quantity){
        return cartService.addProductToCart(productId,quantity);
    }

    @DeleteMapping("/remove/{productId}")
    public HashMap<String, String> removeProductFromCart(@PathVariable Long productId){
        return cartService.removeProductFromCart(productId);

    }

//    @GetMapping("/products")
//    public List<ProductResponse> getCartProducts() {
//        return cartService.getCartProducts();
//    }
@GetMapping("/products")
    public CartResponse getCartProducts() {
        return cartService.getCartProductss();
    }

    @DeleteMapping("/clear")
    public CartResponse clearCart() {
        return cartService.clearCart();
    }

}
