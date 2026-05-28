package org.example.goajaspring.security;

import org.example.goajaspring.model.User;
import org.example.goajaspring.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userService.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        String role = (u.getRole() != null && !u.getRole().isBlank()) ? u.getRole() : "USER";
        List<GrantedAuthority> auth = List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));

        return org.springframework.security.core.userdetails.User
                .withUsername(u.getEmail())
                .password(u.getPassword())
                .authorities(auth)
                .build();
    }
}