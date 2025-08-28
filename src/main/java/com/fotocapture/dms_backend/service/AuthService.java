package com.fotocapture.dms_backend.service;

import com.fotocapture.dms_backend.dto.*;
import com.fotocapture.dms_backend.entity.Role;
import com.fotocapture.dms_backend.entity.User;
import com.fotocapture.dms_backend.repository.RoleRepository;
import com.fotocapture.dms_backend.repository.UserRepository;
import com.fotocapture.dms_backend.security.JwtUtils;
import com.fotocapture.dms_backend.security.UserDetailsServiceImpl;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    // ✅ Constructor
    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtUtils jwtUtils,
                       UserDetailsServiceImpl userDetailsService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    public String registerUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Set<Role> roles = new HashSet<>();
        for (String roleName : request.getRoles()) {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
            roles.add(role);
        }
        user.setRoles(roles);

        userRepository.save(user);
        return "User registered successfully";
    }

    public JwtResponse loginUser(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String token = jwtUtils.generateToken(userDetails.getUsername(), roles);
        String refreshToken = jwtUtils.generateRefreshToken(userDetails.getUsername());

        return new JwtResponse(token, refreshToken, userDetails.getUsername(), roles);
    }

    public JwtResponse refreshToken(TokenRefreshRequest request) {
        String username = jwtUtils.extractUsername(request.getRefreshToken());
        if (!jwtUtils.isTokenValid(request.getRefreshToken(), username)) {
            throw new RuntimeException("Invalid refresh token");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String token = jwtUtils.generateToken(username, roles);
        String newRefreshToken = jwtUtils.generateRefreshToken(username);

        return new JwtResponse(token, newRefreshToken, username, roles);
    }
}
