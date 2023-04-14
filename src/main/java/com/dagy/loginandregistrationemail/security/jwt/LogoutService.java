package com.dagy.loginandregistrationemail.security.jwt;

import com.dagy.loginandregistrationemail.token.TokenRepository;
import com.dagy.loginandregistrationemail.token.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;
    private final TokenService tokenService;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        var storedToken = tokenService.getByToken(jwt)
                .orElse(null);
        if (storedToken != null) {
            storedToken.setCreatedAt(LocalDateTime.now());
            storedToken.setExpired(true);
            storedToken.setExpiresAt(LocalDateTime.now());
            storedToken.setRevoked(true);
            storedToken.setRevokedAt(LocalDateTime.now());
            tokenService.saveToken(storedToken);
            SecurityContextHolder.clearContext();
        }
    }
}
