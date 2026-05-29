package org.example.goajaspring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
        http.userDetailsService(userDetailsService);

        http
                .authorizeHttpRequests(auth -> auth
                        // resources + public endpoints
                        .requestMatchers("/login", "/signup", "/api/public/**", "/css/**", "/js/**", "/images/**", "/h2-console/**").permitAll()
                        // driver application endpoints (authenticated users can apply)
                        .requestMatchers("/driver/apply", "/driver/submit").authenticated()
                        // ADMIN may only access layanan pages
                        .requestMatchers("/layanan/**", "/layanan").hasRole("ADMIN")
                        // Orders and Drivers pages restricted to DRIVER role only (not ADMIN)
                        .requestMatchers("/orders/**", "/api/orders/**", "/drivers/**", "/drivers/applications/**").hasRole("DRIVER")
                        // dashboard and other authenticated pages
                        .requestMatchers("/").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**", "/api/**", "/api/orders/**"));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}