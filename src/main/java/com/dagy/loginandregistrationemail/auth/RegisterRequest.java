package com.dagy.loginandregistrationemail.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
  @Email
  private String email;

  @NotNull(message = "Le password est obligatoire")
  @NotEmpty(message = "Le password est obligatoire")
  private String password;
}
