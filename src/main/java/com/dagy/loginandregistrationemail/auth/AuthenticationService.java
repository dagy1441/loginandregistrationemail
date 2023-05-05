package com.dagy.loginandregistrationemail.auth;

import com.dagy.loginandregistrationemail.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


public interface AuthenticationService {

    public AuthenticationResponse register(RegisterRequest request);

    public AuthenticationResponse authenticate(AuthenticationRequest request);

    public String confirmToken(String token);

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    public void changePassword(ChangePasswordRequest request);

    public void forgotPassword(ForgotPasswordRequest request);

    public void createPasswordResetToken(User user);

    public ResetPasswordResponse validatePasswordResetToken(String token);

    public void resetPassword(User user, String newPassword);


}