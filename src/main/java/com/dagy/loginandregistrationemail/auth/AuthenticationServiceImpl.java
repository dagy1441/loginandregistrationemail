package com.dagy.loginandregistrationemail.auth;

import com.dagy.loginandregistrationemail.email.EmailSender;
import com.dagy.loginandregistrationemail.email.NotificationEmail;
import com.dagy.loginandregistrationemail.exceptions.EntityAllReadyExistException;
import com.dagy.loginandregistrationemail.exceptions.IncorrectPasswordException;
import com.dagy.loginandregistrationemail.exceptions.InvalidTokenException;
import com.dagy.loginandregistrationemail.security.jwt.JwtService;
import com.dagy.loginandregistrationemail.token.Token;
import com.dagy.loginandregistrationemail.token.TokenService;
import com.dagy.loginandregistrationemail.token.TokenType;
import com.dagy.loginandregistrationemail.user.User;
import com.dagy.loginandregistrationemail.user.UserRepository;
import com.dagy.loginandregistrationemail.user.UserService;
import com.dagy.loginandregistrationemail.validators.ObjectsValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final static String USER_NOT_FOUND_MSG = "user with email %s not found";
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailSender emailSender;
    private final ObjectsValidator<RegisterRequest> registerValidator;
    private final ObjectsValidator<AuthenticationRequest> authenticationValidator;
    private final ObjectsValidator<ChangePasswordRequest> changePasswordRequestValidator;
    private final ObjectsValidator<ForgotPasswordRequest> forgotPasswordRequestValidator;

    public AuthenticationResponse register(RegisterRequest request) {
        registerValidator.validate(request);
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IncorrectPasswordException("Les mots de passes ne correspondent pas");
        }

        Optional<User> verifiedUser = userRepository.findByEmail(request.getEmail());

        if (verifiedUser.isPresent()) {
            log.warn("The user with email {}  already exist in BD", request.getEmail());
            throw new EntityAllReadyExistException(
                    "L'email "
                            + request.getEmail() +
                            " existe dèjà.");
        }

        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .createdAt(LocalDateTime.now())
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);

        String link = "http://localhost:8080/api/v1/auth/registration/confirm?token=" + jwtToken;
        String emailContent = "Thank you for registering. Please click on the below link to activate your account:";
        String header = "Confirm your email";

        emailSender.sendMail(new NotificationEmail(
                "Please Activate your Account",
                user.getEmail(),
                buildEmail(user.getFirstName(), header, emailContent, link)
        ));

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        log.info("Validate authentication request");
        authenticationValidator.validate(request);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )

        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new EntityNotFoundException("Email incorrect."));

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        log.info("Activation email sent!!");
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public String confirmToken(String token) {
        Token confirmationToken = tokenService
                .getByToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        System.out.println("getConfirmedAt" + confirmationToken.getConfirmedAt());

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        tokenService.setConfirmedAt(token);
        userService.enableAppUser(
                confirmationToken.getUser().getEmail());
        return "Votre compte est activé.";
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .revoked(false)
                .revokedAt(LocalDateTime.now().plusMinutes(15))
                .createdAt(LocalDateTime.now())
                .build();
        tokenService.saveToken(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenService.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenService.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    @Override
    @Transactional()
    public void changePassword(ChangePasswordRequest request) {

        changePasswordRequestValidator.validate(request);
        if (!(request.getOldPassword().equals(request.getNewPassword()))) {
            throw new IncorrectPasswordException("Les mots de passes ne correspondent pas");
        }
        User currentUser = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new EntityNotFoundException("user not found"));
        currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(currentUser);

    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        forgotPasswordRequestValidator.validate(request);

//    Optional<User> verifiedUser = userRepository.findByEmail(request.getEmail());
//
//    if (verifiedUser.isEmpty()){
//      log.warn("The user with email {}  already exist in BD", request.getEmail());
//      throw new UsernameNotFoundException(
//              "L'email "
//                      +request.getEmail()+
//                      " est incorrecte ou n'existe pas");
//    }
//
//    if (!request.getPassword().equals(request.getConfirmPassword()) ) {
//      throw new IncorrectPasswordException("Les mots de passes ne correspondent pas");
//    }
//
//    verifiedUser.get().setPassword(passwordEncoder.encode(request.getPassword()));
//    userRepository.save(verifiedUser.get());

        // TODO récuper l'email du user à patir de la requette
        // TODO verifier si le user existe dans la base de données
        // TODO Envoyer un mail qui contient l'address pour changer son mot de pass
        //TODO Changer son mot de passe
        // TODO Enregister son nouveau mot de passe
    }

    @Override
    public void createPasswordResetToken(User user) {
        String token = jwtService.generateToken(user);
        saveUserToken(user, token);
        String resetPasswordLink = "http://localhost:8080/api/v1/auth/reset-password?token=" + token;
        String emailContent = "Please click the following link to reset your password: ";
        String header = "Reset your password";

        emailSender.sendMail(new NotificationEmail(
                "Reset password ",
                user.getEmail(),
                buildEmail(user.getFirstName(), header, emailContent, resetPasswordLink)
        ));

    }

    @Override
    public ResetPasswordResponse validatePasswordResetToken(String token) {
//    try {
//
//    } catch (Exception e){
//
//    }catch (){
//
//    }
        Token passwordResetToken = tokenService.getByToken(token).get();
        if (passwordResetToken.isExpired()) {
            throw new InvalidTokenException("Invalid or expired token");
        }
        return AuthMapper.fromEntity(
                passwordResetToken.getUser()
        );
    }

    public void resetPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        tokenService.deleteByUser(user);
    }

    @Transactional()
    public User getCurrentUser() {

        System.out.println();

        User authentication = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String currentUserName = authentication.getUsername();

        User userExist = (User) userService.loadUserByUsername(currentUserName);
        if (userExist == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return userExist;
    }


    private String buildEmail(String name, String header, String content, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">" + header + "</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">" + content + "</p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

}