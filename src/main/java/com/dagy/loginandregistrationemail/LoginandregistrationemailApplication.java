package com.dagy.loginandregistrationemail;

import com.dagy.loginandregistrationemail.auth.AuthenticationService;
import com.dagy.loginandregistrationemail.auth.RegisterRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.dagy.loginandregistrationemail.user.Role.ADMIN;
import static com.dagy.loginandregistrationemail.user.Role.MANAGER;

@SpringBootApplication
public class LoginandregistrationemailApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoginandregistrationemailApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(AuthenticationService authenticationService) {
        return args -> {
            var admin = RegisterRequest.builder()
                    .firstname("Admin")
                    .lastname("Admin")
                    .email("admin@email.com")
                    .password("Password@1234")
                    .confirmPassword("Password@1234")
                    .role(ADMIN)
                    .build();
            System.out.println("admin_token : " + authenticationService.register(admin).getAccessToken());

            var manager = RegisterRequest.builder()
                    .firstname("Manager")
                    .lastname("Manager")
                    .email("manager@email.com")
                    .password("Password@1234")
                    .confirmPassword("Password@1234")
                    .role(MANAGER)
                    .build();
            System.out.println("manager_token : " + authenticationService.register(manager).getAccessToken());
        };
    }

}
