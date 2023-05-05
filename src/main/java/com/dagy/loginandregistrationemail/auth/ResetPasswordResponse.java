package com.dagy.loginandregistrationemail.auth;

import com.dagy.loginandregistrationemail.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordResponse {
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
}
