package com.dagy.loginandregistrationemail.auth;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordRequest {

  @NotNull(message = "L'email est obligatoire")
  @NotEmpty(message = "L'email est obligatoire")
  @Email(message = "Email invalide", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
  private String email;

  
  @NotNull(message = "Le password est obligatoire")
  @NotEmpty(message = "Le password est obligatoire")
  @Pattern(message = "Mot de passe invalide", regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")
  private String password;

  @NotBlank(message = "Confirmez votre password")
  @NotNull(message = "Le password est obligatoire")
  @NotEmpty(message = "Le password est obligatoire")
  @Pattern(message = "Mot de passe invalide", regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")
  private String confirmPassword;
}
