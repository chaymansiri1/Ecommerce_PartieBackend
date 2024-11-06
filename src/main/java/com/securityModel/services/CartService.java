package com.securityModel.services;


import com.securityModel.Dtos.response.CartResponse;
import com.securityModel.Dtos.response.ProductResponse;

import java.util.HashMap;
import java.util.List;

public interface CartService {


HashMap<String,String> addProductToCart(Long productId,int quantity);
    HashMap<String,String> removeProductFromCart(Long productId);

//    List<ProductResponse> getCartProducts();
     CartResponse getCartProductss();
    CartResponse clearCart();




}
