package com.sales.backend.SalesBackend.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final Logger logger =
            LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // CORS preflight request
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestHeader =
                request.getHeader("Authorization");

        logger.info(
                "Request: {} {}",
                request.getMethod(),
                request.getRequestURI()
        );

        logger.info(
                "Authorization Header: {}",
                requestHeader
        );

        String username = null;
        String token = null;

        // Check JWT header
        if (requestHeader != null &&
                requestHeader.startsWith("Bearer ")) {

            token = requestHeader.substring(7);

            try {

                username =
                        jwtHelper.getUsernameFromToken(token);

                logger.info(
                        "Token Username: {}",
                        username
                );

            } catch (IllegalArgumentException ex) {

                logger.error(
                        "Unable to get username from JWT: {}",
                        ex.getMessage()
                );

            } catch (ExpiredJwtException ex) {

                logger.error(
                        "JWT token has expired: {}",
                        ex.getMessage()
                );

            } catch (MalformedJwtException ex) {

                logger.error(
                        "Invalid JWT token: {}",
                        ex.getMessage()
                );

            } catch (Exception ex) {

                logger.error(
                        "Unexpected error while processing JWT",
                        ex
                );
            }

        } else {

            // Login/register requests normally don't have JWT
            logger.info(
                    "No Bearer token found for request: {} {}",
                    request.getMethod(),
                    request.getRequestURI()
            );
        }

        // Authenticate user if token is valid
        if (username != null &&
                SecurityContextHolder
                        .getContext()
                        .getAuthentication() == null) {

            try {

                UserDetails userDetails =
                        userDetailsService
                                .loadUserByUsername(username);

                boolean usernameMatches =
                        username.equals(
                                userDetails.getUsername()
                        );

                boolean tokenValid =
                        !jwtHelper.isTokenExpired(token);

                if (usernameMatches && tokenValid) {

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication);

                    logger.info(
                            "JWT authentication successful for: {}",
                            username
                    );

                } else {

                    logger.warn(
                            "JWT validation failed for: {}",
                            username
                    );
                }

            } catch (Exception ex) {

                logger.error(
                        "Error loading user for JWT: {}",
                        username,
                        ex
                );
            }
        }

        filterChain.doFilter(request, response);
    }
}