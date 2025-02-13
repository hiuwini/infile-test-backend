package com.infile.techicaltest.news.service.impl;

import com.infile.techicaltest.news.dto.AuthRequest;
import com.infile.techicaltest.news.dto.AuthResponse;
import com.infile.techicaltest.news.dto.RegisterResponse;
import com.infile.techicaltest.news.entity.Usuario;
import com.infile.techicaltest.news.exception.InvalidPasswordException;
import com.infile.techicaltest.news.exception.UserNotFoundException;
import com.infile.techicaltest.news.repository.UsuarioRepository;
import com.infile.techicaltest.news.security.JwtService;
import com.infile.techicaltest.news.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtUtils;

    public AuthResponse authenticate(AuthRequest request) {
        Usuario user = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException();
        }

        String token = jwtUtils.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .message("Autenticación exitosa")
                .build();
    }

    public RegisterResponse register(Usuario usuario) {
        // Verificar si ya existe un usuario con ese email
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está en uso: " + usuario.getEmail());
        }

        // Encriptar la contraseña antes de guardarla
        String rawPassword = usuario.getPassword();
        if (rawPassword != null) {
            usuario.setPassword(passwordEncoder.encode(rawPassword));
        }

        // Opcional: Asignar un rol por defecto si no se ha proporcionado
        if (usuario.getRol() == null || usuario.getRol().isBlank()) {
            usuario.setRol("USER");
        }
        Usuario saved = usuarioRepository.save(usuario);
        // Guardar en base de datos
        return RegisterResponse.builder()
                .email(saved.getEmail())
                .rol(saved.getRol())
                .nombre(saved.getNombre())
                .estado("Creado/Activo")
                .build();
    }

}
