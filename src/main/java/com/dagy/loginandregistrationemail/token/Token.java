package com.dagy.loginandregistrationemail.token;

import com.dagy.loginandregistrationemail.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Token {

    @Column(unique = true, nullable = false)
    public String token;
    @Enumerated(EnumType.STRING)
    public TokenType tokenType = TokenType.BEARER;
    public boolean revoked;
    @Column(unique = true, nullable = true)
    public LocalDateTime revokedAt;
    @Column(unique = true, nullable = false)
    public LocalDateTime createdAt;
    public boolean expired;
    @Column(unique = true, nullable = true)
    public LocalDateTime expiresAt;
    public boolean confirmed;
    @Column(unique = true, nullable = true)
    public LocalDateTime confirmedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public User user;
    @SequenceGenerator(
            name = "token_sequence",
            sequenceName = "token_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "token_sequence"
    )
    private Long id;

    public Token(String token, User user) {
        this.token = token;
        this.user = user;
    }
}
