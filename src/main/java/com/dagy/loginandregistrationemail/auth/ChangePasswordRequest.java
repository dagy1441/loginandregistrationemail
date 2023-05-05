package com.dagy.loginandregistrationemail.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {

    private String email;

    @NotNull(message = "Le password est obligatoire")
    @NotEmpty(message = "Le password est obligatoire")
    @Pattern(message = "Mot de passe invalide", regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")
    private String oldPassword;

    @NotNull(message = "Le password est obligatoire")
    @NotEmpty(message = "Le password est obligatoire")
    @Pattern(message = "Mot de passe invalide", regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")
    private String newPassword;
}
