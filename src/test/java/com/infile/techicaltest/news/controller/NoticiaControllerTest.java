package com.infile.techicaltest.news.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import com.infile.techicaltest.news.dto.NoticiaDTO;
import com.infile.techicaltest.news.entity.Noticia;
import com.infile.techicaltest.news.exception.GenericException;
import com.infile.techicaltest.news.service.impl.NoticiaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class NoticiaControllerTest {

    @Mock
    private NoticiaService noticiaService;

    @InjectMocks
    private NoticiaController noticiaController;

    @Test
    void listarNoticias_ReturnsNewsList() {
        // Arrange
        NoticiaDTO dto = new NoticiaDTO();
        dto.setId(1L);
        dto.setTitulo("Últimas noticias");

        when(noticiaService.listarNoticias()).thenReturn(List.of(dto));

        // Act
        List<NoticiaDTO> resultado = noticiaController.listarNoticias();

        // Assert
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        verify(noticiaService).listarNoticias();
    }

    @Test
    void listarNoticias_EmptyList_ReturnsEmptyResponse() {
        // Arrange
        when(noticiaService.listarNoticias()).thenReturn(Collections.emptyList());

        // Act
        List<NoticiaDTO> resultado = noticiaController.listarNoticias();

        // Assert
        assertTrue(resultado.isEmpty());
    }

    @Test
    void obtenerNoticia_ValidId_ReturnsNews() {
        // Arrange
        Noticia noticia = new Noticia();
        noticia.setId(5L);
        when(noticiaService.obtenerNoticiaPorId(5L)).thenReturn(noticia);

        // Act
        Noticia resultado = noticiaController.obtenerNoticia(5L);

        // Assert
        assertEquals(5L, resultado.getId());
        verify(noticiaService).obtenerNoticiaPorId(5L);
    }

    @Test
    void obtenerNoticia_InvalidId_ThrowsException() {
        // Arrange
        when(noticiaService.obtenerNoticiaPorId(99L))
                .thenThrow(new GenericException("Noticia no encontrada"));

        // Act & Assert
        GenericException exception = assertThrows(
                GenericException.class,
                () -> noticiaController.obtenerNoticia(99L)
        );
        assertEquals("Noticia no encontrada", exception.getMessage());
    }

    @Test
    void obtenerNoticiasRecomendadas_WithRecommendations_ReturnsList() {
        // Arrange
        Noticia recomendada = new Noticia();
        recomendada.setId(2L);
        when(noticiaService.obtenerNoticiasRecomendadas(1L))
                .thenReturn(List.of(recomendada));

        // Act
        List<Noticia> resultado = noticiaController.obtenerNoticiasRecomendadas(1L);

        // Assert
        assertEquals(1, resultado.size());
        assertEquals(2L, resultado.get(0).getId());
        verify(noticiaService).obtenerNoticiasRecomendadas(1L);
    }

    @Test
    void obtenerNoticiasRecomendadas_NoRecommendations_ReturnsEmpty() {
        // Arrange
        when(noticiaService.obtenerNoticiasRecomendadas(3L))
                .thenReturn(Collections.emptyList());

        // Act
        List<Noticia> resultado = noticiaController.obtenerNoticiasRecomendadas(3L);

        // Assert
        assertTrue(resultado.isEmpty());
    }
}