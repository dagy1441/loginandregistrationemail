package com.dagy.loginandregistrationemail.token;


import com.dagy.loginandregistrationemail.exceptions.InvalidTokenException;
import com.dagy.loginandregistrationemail.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    public void saveToken(Token token) {
        tokenRepository.save(token);
    }

    public Optional<Token> getByToken(String token) {
        Token expectedToken = tokenRepository.findByToken(token).get();
        if (expectedToken.isExpired()) {
            throw new InvalidTokenException("Invalid or expired token");
        }
        return Optional.of(expectedToken);
    }

    public int setConfirmedAt(String token) {
        return tokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }

    public List<Token> findAllValidTokenByUser(Long id){
        return tokenRepository.findAllValidTokenByUser(id);
    }

    public List<Token> saveAll(List<Token> tokens) {
        return tokenRepository.saveAll(tokens);
    }

    public void deleteByUser(User user) {
        tokenRepository.deleteByUser(user);
    }

}
