package com.securityModel.Dtos.response;


import com.securityModel.Dtos.request.WishlistRequest;
import com.securityModel.models.Product;
import com.securityModel.models.User;
import com.securityModel.models.Wishlist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WishlistResponse {
    private Long id;
    private User user;
    private Product product;


    public static WishlistResponse fromEntity(Wishlist entity){
       WishlistResponse wishlistResponse=new WishlistResponse();
        BeanUtils.copyProperties(entity,wishlistResponse);
        return wishlistResponse;
    }


    public static Wishlist toEntity(WishlistRequest wishlistResponse){
        Wishlist w =new Wishlist();
        BeanUtils.copyProperties(wishlistResponse,w);
        return w;
    }


}
