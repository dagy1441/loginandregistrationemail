package com.dagy.loginandregistrationemail.user;

import com.dagy.loginandregistrationemail.token.TokenService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

//    private final PasswordEncoder passwordEncoder;

    private final static String USER_NOT_FOUND_MSG =
            "user with email %s not found";

    private final UserRepository userRepository;
    private final TokenService confirmationTokenService;

    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG, email)));
    }

    public User findUserByEmail(String email) {
        System.out.println("***** EMAIL ***** " + email);
        System.out.println("*********** USERS **************" + userRepository.findByEmail(email).get());

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                String.format(USER_NOT_FOUND_MSG, email)));
    }

    public int enableAppUser(String email) {
        return userRepository.enableAppUser(email);
    }


}
