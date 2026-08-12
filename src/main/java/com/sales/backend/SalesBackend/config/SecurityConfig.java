package com.sales.backend.SalesBackend.config;

import com.sales.backend.SalesBackend.security.JWTAuthenticationFilter;
import com.sales.backend.SalesBackend.security.JwtAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JWTAuthenticationFilter filter;

    @Autowired
    private JwtAuthenticationEntryPoint entryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity security) throws Exception {

        security
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(request -> request

                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers(
                                "/auth/generate-token",
                                "/auth/login-with-google",
                                "/auth/regenerate-token"
                        ).permitAll()

                        .requestMatchers(HttpMethod.GET, "/products/**")
                        .permitAll()

                        .requestMatchers("/products/**")
                        .hasRole(AppConstants.ROLE_ADMIN)

                        .requestMatchers(HttpMethod.GET, "/users/**")
                        .permitAll()

                        .requestMatchers(HttpMethod.POST, "/users")
                        .permitAll()

                        .requestMatchers(HttpMethod.DELETE, "/users/**")
                        .hasRole(AppConstants.ROLE_ADMIN)

                        .requestMatchers(HttpMethod.PUT, "/users/**")
                        .hasAnyRole(
                                AppConstants.ROLE_ADMIN,
                                AppConstants.ROLE_NORMAL
                        )

                        .requestMatchers(HttpMethod.GET, "/categories/**")
                        .permitAll()

                        .requestMatchers("/categories/**")
                        .hasRole(AppConstants.ROLE_ADMIN)

                        .requestMatchers("/auth/**")
                        .authenticated()

                        .anyRequest()
                        .permitAll()
                )

                .exceptionHandling(ex ->
                        ex.authenticationEntryPoint(entryPoint)
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .addFilterBefore(
                        filter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return security.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of("http://localhost:5173")
        );

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                List.of("*")
        );

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }
}