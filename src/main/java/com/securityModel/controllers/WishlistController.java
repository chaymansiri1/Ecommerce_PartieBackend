package com.securityModel.controllers;


import com.securityModel.Dtos.response.WishlistResponse;
import com.securityModel.services.WishlistService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishlists")
public class WishlistController {
    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping("/addProduct")
    public WishlistResponse addProductToWishlist(@RequestParam Long userId, @RequestParam Long productId) {
        return wishlistService.addProductToWishlist(userId, productId);
    }

    @DeleteMapping("/removeProduct")
    public WishlistResponse removeProductFromWishlist(@RequestParam Long userId, @RequestParam Long productId) {
        return wishlistService.removeProductFromWishlist(userId, productId);
    }
}
