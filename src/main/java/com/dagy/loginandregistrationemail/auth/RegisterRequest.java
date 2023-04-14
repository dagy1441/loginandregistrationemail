package com.dagy.loginandregistrationemail.auth;

import jakarta.validation.constraints.Email;
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
public class RegisterRequest {

  @NotNull(message = "Le nom est obligatoire")
  @NotEmpty(message = "Le nom est obligatoire")
  private String firstname;

  @NotNull(message = "Le nom est obligatoire")
  @NotEmpty(message = "Le prénoms est obligatoire")
  private String lastname;

  @NotNull(message = "L'email est obligatoire")
  @NotEmpty(message = "L'email est obligatoire")
  @Email(message = "Email invalide", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
  private String email;


  /**
   * Min 1 uppercase letter.
   * Min 1 lowercase letter.
   * Min 1 special character.
   * Min 1 number.
   * Min 8 characters.
   */
  @NotNull(message = "Le password est obligatoire")
  @NotEmpty(message = "Le password est obligatoire")
  @Pattern(message = "Mot de passe invalide", regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")
  private String password;
}
