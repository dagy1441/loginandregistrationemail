package com.dagy.loginandregistrationemail.auth;

import com.dagy.loginandregistrationemail.exceptions.IncorrectPasswordException;
import com.dagy.loginandregistrationemail.exceptions.InvalidTokenException;
import com.dagy.loginandregistrationemail.security.jwt.JwtService;
import com.dagy.loginandregistrationemail.token.TokenService;
import com.dagy.loginandregistrationemail.user.User;
import com.dagy.loginandregistrationemail.user.UserService;
import com.dagy.loginandregistrationemail.utilities.helpers.ApiDataResponse;
import com.dagy.loginandregistrationemail.validators.ObjectsValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;

import static java.time.LocalDateTime.now;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final UserService userService;
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final ObjectsValidator<ForgotPasswordRequest> forgotPasswordRequestValidator;
    private final ObjectsValidator<PasswordResetRequest> passwordResetRequestValidator;


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
    public ResponseEntity<String> getCurrentUserName(HttpServletRequest req) {
        String token = req.getHeader("Authorization").replace("Bearer", "");
        var username = jwtService.extractUsername(token);
        return ResponseEntity.ok(username);
    }


    @PatchMapping("/change-password")
    ResponseEntity<ApiDataResponse> changePassword(
            HttpServletRequest req,
            @RequestBody ChangePasswordRequest request
    ) {

        String token = req.getHeader("Authorization").replace("Bearer", "");
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

    @PostMapping("/request-password-reset")
    public ResponseEntity<ApiDataResponse> requestPasswordReset(@RequestBody PasswordResetRequest request) {
        try {
            passwordResetRequestValidator.validate(request);
            User user = userService.findUserByEmail(request.getEmail());
            service.createPasswordResetToken(user);
            return ResponseEntity.ok(
                    ApiDataResponse.builder()
                            .time(now())
                            .message("Email de modification de mot de passe envoyé avec succès")
                            .httpStatus(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );
        } catch (NoSuchElementException e) {
            System.out.println(e);

            return ResponseEntity.badRequest().body(
                    ApiDataResponse.builder()
                            .time(now())
                            .message("Email invalide! Veuillez verifier votre email")
                            .httpStatus(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .build()
            );
        }
    }

    @GetMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam("token") String token,
            @RequestBody ForgotPasswordRequest request) {
        try {
            forgotPasswordRequestValidator.validate(request);
            ResetPasswordResponse user = service.validatePasswordResetToken(token);
            if (user == null) {
                return new ResponseEntity<>("Invalid or expired token", HttpStatus.BAD_REQUEST);
            }
            if (!request.getPassword().equals(request.getConfirmPassword())) {
                throw new IncorrectPasswordException("Les mots de passes ne correspondent pas");
            }

            service.resetPassword(AuthMapper.toEntity(user), request.getPassword());
            return new ResponseEntity<>("Password reset successfully", HttpStatus.OK);
        } catch (InvalidTokenException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
