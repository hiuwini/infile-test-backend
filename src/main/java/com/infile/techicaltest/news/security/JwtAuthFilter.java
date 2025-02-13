package com.infile.techicaltest.news.security;

import com.infile.techicaltest.news.entity.Usuario;
import com.infile.techicaltest.news.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            // Extraemos el email (subject) del token
            String email = jwtService.extractEmail(token);
            if (email != null) {
                // Buscamos el usuario en la BD
                Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

                if (usuarioOpt.isPresent()) {
                    Usuario usuario = usuarioOpt.get();
                    // Validamos que el token sea válido y pertenezca a ese usuario
                    if (jwtService.isTokenValid(token, usuario)) {
                        // Creamos el "Authentication" con la entidad Usuario como principal
                        var authorities = List.of(new SimpleGrantedAuthority("ROLE_"));

                        var authToken = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                                usuario,       // principal: la entidad Usuario
                                null,          // no necesitamos credenciales
                                authorities
                        );

                        // Detalles extras para auditoría (IP, sessionId, etc.)
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // Asignamos la autenticación al contexto de seguridad de Spring
                        // para que la solicitud quede autenticada a partir de este punto
                        org.springframework.security.core.context.SecurityContextHolder.getContext()
                                .setAuthentication(authToken);
                    }
                }
            }
        }

        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}