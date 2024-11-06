package com.securityModel.repository;


import com.securityModel.models.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistDao extends JpaRepository<Wishlist,Long> {

}
