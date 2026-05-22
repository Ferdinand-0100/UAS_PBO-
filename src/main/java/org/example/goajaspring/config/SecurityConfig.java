package org.example.goajaspring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Allow the login page and static assets without authentication
                .requestMatchers("/login", "/api/**", "/css/**", "/js/**", "/images/**").permitAll()
                // Everything else requires login
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")          // Use our custom login page
                .defaultSuccessUrl("/", true) // After login, go to dashboard
                .failureUrl("/login?error")   // On failure, back to login with error param
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout") // After logout, go back to login
                .permitAll()
            );

        return http.build();
    }
}
