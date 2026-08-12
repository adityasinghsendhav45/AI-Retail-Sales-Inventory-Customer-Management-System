package com.sales.backend.SalesBackend.controllers;

import com.sales.backend.SalesBackend.dtos.JwtRequest;
import com.sales.backend.SalesBackend.dtos.JwtResponse;
import com.sales.backend.SalesBackend.dtos.UserDto;
import com.sales.backend.SalesBackend.entities.User;
import com.sales.backend.SalesBackend.security.JwtHelper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Logger logger =
            LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/generate-token")
    public ResponseEntity<JwtResponse> login(
            @RequestBody JwtRequest request) {

        logger.info("Login attempt for email: {}", request.getEmail());

        User user;

        try {

            user = (User) userDetailsService
                    .loadUserByUsername(request.getEmail());

        } catch (Exception ex) {

            logger.error(
                    "User not found for email: {}",
                    request.getEmail()
            );

            throw new BadCredentialsException(
                    "Invalid Username and Password !!"
            );
        }

        boolean passwordMatches =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword()
                );

        logger.info(
                "Password matches database hash: {}",
                passwordMatches
        );

        if (!passwordMatches) {

            logger.error("BCrypt password does NOT match");

            throw new BadCredentialsException(
                    "Invalid Username and Password !!"
            );
        }

        try {

            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getEmail(),
                                    request.getPassword()
                            )
                    );

            logger.info(
                    "Authentication successful: {}",
                    authentication.isAuthenticated()
            );

        } catch (BadCredentialsException ex) {

            logger.error(
                    "AuthenticationManager rejected credentials"
            );

            throw new BadCredentialsException(
                    "Invalid Username and Password !!"
            );
        }

        String token = jwtHelper.generateToken(user);

        JwtResponse response =
                JwtResponse.builder()
                        .token(token)
                        .user(
                                modelMapper.map(
                                        user,
                                        UserDto.class
                                )
                        )
                        .build();

        logger.info(
                "Login successful for email: {}",
                request.getEmail()
        );

        return ResponseEntity.ok(response);
    }
}