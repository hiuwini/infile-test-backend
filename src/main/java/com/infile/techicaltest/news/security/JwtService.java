package com.infile.techicaltest.news.security;

import com.infile.techicaltest.news.entity.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;      // Debe ser una cadena en Base64 o suficientemente larga para HS256
    @Value("${jwt.expiration}")
    private long jwtExpiration;    // Tiempo de expiración en milisegundos

    /**
     * Genera un token JWT basado en la entidad Usuario.
     * Aquí usaremos el email como subject.
     */
    public String generateToken(Usuario usuario) {
        return buildToken(usuario.getEmail(), jwtExpiration);
    }

    /**
     * Verifica si el token es válido para un usuario concreto
     * (coincide el email y no está expirado).
     */
    public boolean isTokenValid(String token, Usuario usuario) {
        final String email = extractEmail(token);
        return (email != null
                && email.equals(usuario.getEmail())
                && !isTokenExpired(token));
    }

    /**
     * Extrae el email (subject) desde el token.
     */
    public String extractEmail(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(getSignInKey()) // La clave para verificar la firma
                    .build()
                    .parseClaimsJws(token)
                    .getPayload()
                    .getSubject();                // Subject = email del usuario
        } catch (Exception e) {
            return null; // Error al parsear: token inválido, expirado, etc.
        }
    }

    // ------------------------------------------------------------
    // Métodos privados de ayuda
    // ------------------------------------------------------------

    private String buildToken(String subject, long expiration) {
        return Jwts.builder()
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        return expiration != null && expiration.before(new Date());
    }

    private Date extractExpiration(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getPayload()
                    .getExpiration();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Obtiene la clave criptográfica a partir de la cadena 'secretKey'.
     * Si tu secretKey está en Base64, decodificamos así.
     * Si fuera texto plano, usaríamos:
     *   Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8))
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}