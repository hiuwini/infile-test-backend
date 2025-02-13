package com.infile.techicaltest.news.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.infile.techicaltest.news.dto.AuthRequest;
import com.infile.techicaltest.news.dto.AuthResponse;
import com.infile.techicaltest.news.dto.RegisterResponse;
import com.infile.techicaltest.news.entity.Usuario;
import com.infile.techicaltest.news.exception.InvalidPasswordException;
import com.infile.techicaltest.news.exception.UserNotFoundException;
import com.infile.techicaltest.news.repository.UsuarioRepository;
import com.infile.techicaltest.news.security.JwtService;
import com.infile.techicaltest.news.service.impl.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void authenticate_ValidCredentials_ReturnsAuthResponse() {
        // Arrange
        AuthRequest request = new AuthRequest("test@example.com", "password");
        Usuario user = new Usuario();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        when(usuarioRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("testToken");

        // Act
        AuthResponse response = authService.authenticate(request);

        // Assert
        assertEquals("testToken", response.getToken());
        assertEquals("Autenticación exitosa", response.getMessage());
        verify(usuarioRepository).findByEmail(request.getEmail());
        verify(passwordEncoder).matches(request.getPassword(), user.getPassword());
        verify(jwtService).generateToken(user);
    }

    @Test
    void authenticate_UserNotFound_ThrowsException() {
        // Arrange
        AuthRequest request = new AuthRequest("nonexistent@example.com", "password");
        when(usuarioRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> authService.authenticate(request));
        verify(usuarioRepository).findByEmail(request.getEmail());
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(jwtService);
    }

    @Test
    void authenticate_InvalidPassword_ThrowsException() {
        // Arrange
        AuthRequest request = new AuthRequest("test@example.com", "wrongPassword");
        Usuario user = new Usuario();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        when(usuarioRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidPasswordException.class, () -> authService.authenticate(request));
        verify(passwordEncoder).matches(request.getPassword(), user.getPassword());
        verifyNoInteractions(jwtService);
    }

    @Test
    void register_NewUser_SuccessfullyRegisters() {
        // Arrange
        Usuario newUser = new Usuario();
        newUser.setEmail("new@example.com");
        newUser.setPassword("rawPassword");
        newUser.setNombre("John Doe");

        Usuario savedUser = new Usuario();
        savedUser.setEmail(newUser.getEmail());
        savedUser.setPassword("encodedPassword");
        savedUser.setNombre(newUser.getNombre());
        savedUser.setRol("USER");

        when(usuarioRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(newUser.getPassword())).thenReturn("encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(savedUser);

        // Act
        RegisterResponse response = authService.register(newUser);

        // Assert
        assertEquals("new@example.com", response.getEmail());
        assertEquals("John Doe", response.getNombre());
        assertEquals("USER", response.getRol());
        assertEquals("Creado/Activo", response.getEstado());
        verify(usuarioRepository).findByEmail(newUser.getEmail());
        verify(passwordEncoder).encode("rawPassword");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void register_ExistingEmail_ThrowsException() {
        // Arrange
        Usuario existingUser = new Usuario();
        existingUser.setEmail("existing@example.com");

        when(usuarioRepository.findByEmail(existingUser.getEmail())).thenReturn(Optional.of(existingUser));

        Usuario newUser = new Usuario();
        newUser.setEmail("existing@example.com");

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.register(newUser));
        assertEquals("El email ya está en uso: existing@example.com", exception.getMessage());
        verify(usuarioRepository).findByEmail(existingUser.getEmail());
        verifyNoMoreInteractions(usuarioRepository);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void register_WithCustomRole_ReturnsAssignedRole() {
        // Arrange
        Usuario user = new Usuario();
        user.setEmail("admin@example.com");
        user.setPassword("adminPass");
        user.setRol("ADMIN");

        when(usuarioRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedAdminPass");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(user);

        // Act
        RegisterResponse response = authService.register(user);

        // Assert
        assertEquals("ADMIN", response.getRol());
        verify(usuarioRepository).save(argThat(u -> "ADMIN".equals(u.getRol())));
    }
}