package com.securityModel.Dtos.response;


import com.securityModel.Dtos.request.UserRequest;
import com.securityModel.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String password;
    private String username;
    private String image;


    public static UserResponse fromEntity(User entity){
        UserResponse userResponse=new UserResponse();
        BeanUtils.copyProperties(entity,userResponse);
        return userResponse;
    }



    //Elle prend un objet ProductRequest DTO et le transforme en une entité Product.
    //copier les propriétés du DTO ProductRequest vers une nouvelle instance de Product.

    public static User toEntity(UserRequest userResponse){
        User u=new User();
        BeanUtils.copyProperties(userResponse,u);
        return u;
    }


}
