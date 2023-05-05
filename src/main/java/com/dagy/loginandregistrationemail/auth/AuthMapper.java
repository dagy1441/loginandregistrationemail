package com.dagy.loginandregistrationemail.auth;

import com.dagy.loginandregistrationemail.categories.Category;
import com.dagy.loginandregistrationemail.categories.CategoryRequest;
import com.dagy.loginandregistrationemail.categories.CategoryResponse;
import com.dagy.loginandregistrationemail.user.User;

public class AuthMapper {
    public static ResetPasswordResponse fromEntity(User user){
        if (user == null) return null;
        return ResetPasswordResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public static User toEntity(ResetPasswordResponse request){
        if (request == null) return null;
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        return user;
    }
}
