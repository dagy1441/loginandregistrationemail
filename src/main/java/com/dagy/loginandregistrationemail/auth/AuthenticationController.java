package com.dagy.loginandregistrationemail.auth;

import com.dagy.loginandregistrationemail.security.jwt.JwtService;
import com.dagy.loginandregistrationemail.user.User;
import com.dagy.loginandregistrationemail.user.UserService;
import com.dagy.loginandregistrationemail.utilities.helpers.ApiDataResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

import static java.time.LocalDateTime.now;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;
  private final UserService userService;
  private final JwtService jwtService;


  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
          @RequestBody RegisterRequest request
  ) {
    return ResponseEntity.ok(service.register(request));
  }

  @GetMapping("/registration/confirm")
  ResponseEntity<ApiDataResponse> confirm(
          @RequestParam("token") String token
  ) {

    return ResponseEntity.ok(
            ApiDataResponse.builder()
                    .time(now())
                    .message("Activation du compte par mail")
                    .httpStatus(HttpStatus.OK)
                    .statusCode(HttpStatus.OK.value())
                    .data(Map.of("authentication", service.confirmToken(token)))
                    .build()
    );
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
          @RequestBody AuthenticationRequest request
  ) {
    System.out.println("****** Controller ******");
    return ResponseEntity.ok(service.authenticate(request));
  }

  @PostMapping("/refresh-token")
  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    service.refreshToken(request, response);
  }



//  @GetMapping("/currentusername")
  public ResponseEntity<String>  getCurrentUserName(HttpServletRequest req){
    String token = req.getHeader("Authorization").replace("Bearer","");
    var username = jwtService.extractUsername(token);
    return ResponseEntity.ok( username );
  }


  @PatchMapping("/change-password")
  ResponseEntity<ApiDataResponse> changePassword(
          HttpServletRequest req,
          @RequestBody ChangePasswordRequest request
  ) {

    String token = req.getHeader("Authorization").replace("Bearer","");
    var username = jwtService.extractUsername(token);
    request.setEmail(username);
    service.changePassword(request);

    return ResponseEntity.ok(
            ApiDataResponse.builder()
                    .time(now())
                    .message("Mot de passe modifier avec succès")
                    .httpStatus(HttpStatus.OK)
                    .statusCode(HttpStatus.OK.value())
                    .build()
    );
  }


}
