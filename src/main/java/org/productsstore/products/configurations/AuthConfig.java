package org.productsstore.products.configurations;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;

@Configuration
public class AuthConfig {

    @Bean
    // Provides a BCryptPasswordEncoder bean used for hashing and verifying user passwords.
    // BCrypt is a strong, adaptive hashing algorithm recommended for password storage.
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    // Configures the Spring Security filter chain.
    // Defines how incoming HTTP requests are secured and authorized.
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Disables CSRF protection.
                // Typically done for stateless APIs (e.g., token-based authentication).
                .csrf(csrf -> csrf.disable())
                // Allows all incoming requests without authentication.
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .build();
    }

    @Bean
    // Generates and exposes a SecretKey bean used for signing JWT tokens.
    // Uses HMAC SHA-256 (HS256) algorithm for token signing and verification.
    public SecretKey secretKey() {
        MacAlgorithm algorithm = Jwts.SIG.HS256;
        // Builds a secure secret key based on the chosen algorithm
        SecretKey secretKey = algorithm.key().build();
        return secretKey;
    }
}


//CORS =  CROSS ORIGIN (Domain) RESOURCE (IMG/VIDEO) SHARING
//<img src ="geeksforgeeks.com/img/biehfhweohfwoeh" >
