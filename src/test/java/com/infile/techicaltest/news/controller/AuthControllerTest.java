package com.infile.techicaltest.news.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.infile.techicaltest.news.dto.AuthRequest;
import com.infile.techicaltest.news.dto.AuthResponse;
import com.infile.techicaltest.news.dto.RegisterResponse;
import com.infile.techicaltest.news.entity.Usuario;
import com.infile.techicaltest.news.exception.InvalidPasswordException;
import com.infile.techicaltest.news.exception.UserNotFoundException;
import com.infile.techicaltest.news.service.impl.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void login_ValidCredentials_ReturnsAuthResponse() {
        // Arrange
        AuthRequest request = new AuthRequest("test@example.com", "password");
        AuthResponse mockResponse = AuthResponse.builder()
                .token("jwt-token")
                .message("Success")
                .build();

        when(authService.authenticate(request)).thenReturn(mockResponse);

        // Act
        ResponseEntity<AuthResponse> response = authController.login(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("jwt-token", response.getBody().getToken());
        verify(authService).authenticate(request);
    }

    @Test
    void login_InvalidUser_ThrowsNotFoundException() {
        // Arrange
        AuthRequest request = new AuthRequest("invalid@example.com", "password");
        when(authService.authenticate(request)).thenThrow(UserNotFoundException.class);

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> authController.login(request));
    }

    @Test
    void login_WrongPassword_ThrowsInvalidPasswordException() {
        // Arrange
        AuthRequest request = new AuthRequest("test@example.com", "wrongpass");
        when(authService.authenticate(request)).thenThrow(InvalidPasswordException.class);

        // Act & Assert
        assertThrows(InvalidPasswordException.class, () -> authController.login(request));
    }

    @Test
    void register_NewUser_ReturnsCreatedResponse() {
        // Arrange
        Usuario newUser = new Usuario();
        newUser.setEmail("new@example.com");

        RegisterResponse mockResponse = RegisterResponse.builder()
                .email("new@example.com")
                .estado("Creado/Activo")
                .build();

        when(authService.register(any(Usuario.class))).thenReturn(mockResponse);

        // Act
        ResponseEntity<RegisterResponse> response = authController.register(newUser);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("new@example.com", response.getBody().getEmail());
        verify(authService).register(newUser);
    }

    @Test
    void register_DuplicateEmail_ThrowsConflict() {
        // Arrange
        Usuario existingUser = new Usuario();
        existingUser.setEmail("exists@example.com");

        when(authService.register(existingUser))
                .thenThrow(new RuntimeException("El email ya está en uso"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authController.register(existingUser));

        assertEquals("El email ya está en uso", exception.getMessage());
    }
}