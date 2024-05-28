package com.emaflores.spaceships.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] SPRINGDOC_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(SPRINGDOC_WHITELIST).permitAll()
                            .anyRequest().authenticated();
                })
                .httpBasic();

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var userDetailsService = new InMemoryUserDetailsManager();

        var user = User.withUsername("user")
                .password(passwordEncoder().encode("password"))
                .roles("USER")
                .build();
        userDetailsService.createUser(user);

        var admin = User.withUsername("admin")
                .password(passwordEncoder().encode("adminpassword"))
                .roles("ADMIN")
                .build();
        userDetailsService.createUser(admin);

        return userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
