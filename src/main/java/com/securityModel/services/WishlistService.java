package com.securityModel.services;


import com.securityModel.Dtos.response.WishlistResponse;

public interface WishlistService {
    WishlistResponse addProductToWishlist(Long userId, Long productId);
    WishlistResponse removeProductFromWishlist(Long userId, Long productId);
}
