package com.infile.techicaltest.news.config;

import com.infile.techicaltest.news.security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Desactivar CSRF para un API REST
        http.csrf(csrf -> csrf.disable());

        // Configurar las autorizaciones
        http.authorizeHttpRequests(auth -> {
            auth
                    // Permitir acceso sin token a estos endpoints
                    .requestMatchers("/api/auth/**",
                            "/swagger-ui/**",
                            "/v3/api-docs/**")
                    .permitAll()
                    // Cualquier otro endpoint requiere autenticación
                    .anyRequest().authenticated();
        });

        // No usar sesiones (completamente stateless)
        http.sessionManagement(sm ->
                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // Agregar nuestro filtro JWT antes del UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
