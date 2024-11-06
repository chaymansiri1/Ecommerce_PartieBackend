package com.securityModel.services.impl;


import com.securityModel.Dtos.response.WishlistResponse;
import com.securityModel.models.Product;
import com.securityModel.models.User;
import com.securityModel.models.Wishlist;
import com.securityModel.repository.ProductDao;
import com.securityModel.repository.UserRepository;
import com.securityModel.repository.WishlistDao;
import com.securityModel.services.WishlistService;
import org.springframework.stereotype.Service;

@Service
public class WishlistServiceIMPL implements WishlistService {

    private final UserRepository userDaoconst;
    private final ProductDao productDaoconst;
    private final WishlistDao wishlistDaoconst;

    public WishlistServiceIMPL(UserRepository userDaoconst, ProductDao productDaoconst, WishlistDao wishlistDaoconst) {
        this.userDaoconst = userDaoconst;
        this.productDaoconst = productDaoconst;
        this.wishlistDaoconst = wishlistDaoconst;
    }

    @Override
    public WishlistResponse addProductToWishlist(Long userId, Long productId) {


            // Récupérer l'utilisateur et le produit à partir des ID
            User user = userDaoconst.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            Product product = productDaoconst.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

            // Récupérer la wishlist de l'utilisateur
            Wishlist wishlist = user.getWishlist();

            // Si l'utilisateur n'a pas encore de wishlist, en créer une
            if (wishlist == null) {
                wishlist = new Wishlist();
                wishlist.setUsers(user);
                user.setWishlist(wishlist);
            }

            // Vérifier si le produit est déjà dans la wishlist
            if (wishlist.getProducts().contains(product)) {
                throw new RuntimeException("Product is already in the wishlist");
            }

            // Ajouter le produit à la wishlist si ce n'est pas le cas
            wishlist.getProducts().add(product);
            Wishlist savedWishlist = wishlistDaoconst.save(wishlist);

            // Retourner la réponse
            return WishlistResponse.fromEntity(savedWishlist);
        }



        @Override
    public WishlistResponse removeProductFromWishlist(Long userId, Long productId) {

            User user = userDaoconst.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            Product product = productDaoconst.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
            Wishlist wishlist = user.getWishlist();

            if (wishlist != null) {
                wishlist.getProducts().remove(product);
                Wishlist savedWishlist = wishlistDaoconst.save(wishlist);
                return WishlistResponse.fromEntity(savedWishlist);
            } else {
                throw new RuntimeException("Wishlist not found for this user");
            }
        }

    }

