package com.infile.techicaltest.news.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.infile.techicaltest.news.dto.NoticiaDTO;
import com.infile.techicaltest.news.entity.Noticia;
import com.infile.techicaltest.news.entity.NoticiaRecomendada;
import com.infile.techicaltest.news.exception.GenericException;
import com.infile.techicaltest.news.repository.NoticiaRecomendadaRepository;
import com.infile.techicaltest.news.repository.NoticiaRepository;
import com.infile.techicaltest.news.service.impl.NoticiaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NoticiaServiceTest {

    @Mock
    private NoticiaRepository noticiaRepository;

    @Mock
    private NoticiaRecomendadaRepository noticiaRecomendadaRepository;

    @InjectMocks
    private NoticiaService noticiaService;

    @Test
    void listarNoticias_ReturnsListOfDTOs() {
        // Arrange
        Noticia noticia = new Noticia();
        noticia.setId(1L);
        noticia.setTitulo("Título prueba");
        noticia.setDescripcion("Descripción prueba");
        noticia.setUrlImagen("imagen.jpg");

        when(noticiaRepository.findAll()).thenReturn(List.of(noticia));

        // Act
        List<NoticiaDTO> resultado = noticiaService.listarNoticias();

        // Assert
        assertEquals(1, resultado.size());
        NoticiaDTO dto = resultado.get(0);
        assertEquals(1L, dto.getId());
        assertEquals("Título prueba", dto.getTitulo());
        assertEquals("Descripción prueba", dto.getDescripcion());
        assertEquals("imagen.jpg", dto.getUrlImagen());
        verify(noticiaRepository).findAll();
    }

    @Test
    void obtenerNoticiaPorId_WhenExists_ReturnsNoticia() {
        // Arrange
        Noticia noticia = new Noticia();
        noticia.setId(1L);
        when(noticiaRepository.findById(1L)).thenReturn(Optional.of(noticia));

        // Act
        Noticia resultado = noticiaService.obtenerNoticiaPorId(1L);

        // Assert
        assertEquals(1L, resultado.getId());
        verify(noticiaRepository).findById(1L);
    }

    @Test
    void obtenerNoticiaPorId_WhenNotExists_ThrowsException() {
        // Arrange
        when(noticiaRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        GenericException exception = assertThrows(
                GenericException.class,
                () -> noticiaService.obtenerNoticiaPorId(99L)
        );
        assertEquals("Noticia no encontrada", exception.getMessage());
        verify(noticiaRepository).findById(99L);
    }

    @Test
    void obtenerNoticiasRecomendadas_ValidId_ReturnsRecommendations() {
        // Arrange
        Noticia noticiaBase = new Noticia();
        noticiaBase.setId(1L);

        Noticia noticiaRelacionada = new Noticia();
        noticiaRelacionada.setId(2L);

        NoticiaRecomendada recomendacion = new NoticiaRecomendada();
        recomendacion.setNoticiaRelacionada(noticiaRelacionada);

        when(noticiaRepository.findById(1L)).thenReturn(Optional.of(noticiaBase));
        when(noticiaRecomendadaRepository.findByNoticia(noticiaBase))
                .thenReturn(List.of(recomendacion));

        // Act
        List<Noticia> resultado = noticiaService.obtenerNoticiasRecomendadas(1L);

        // Assert
        assertEquals(1, resultado.size());
        assertEquals(2L, resultado.get(0).getId());
        verify(noticiaRecomendadaRepository).findByNoticia(noticiaBase);
    }

    @Test
    void obtenerNoticiasRecomendadas_NoRecommendations_ReturnsEmptyList() {
        // Arrange
        Noticia noticiaBase = new Noticia();
        noticiaBase.setId(1L);

        when(noticiaRepository.findById(1L)).thenReturn(Optional.of(noticiaBase));
        when(noticiaRecomendadaRepository.findByNoticia(noticiaBase))
                .thenReturn(Collections.emptyList());

        // Act
        List<Noticia> resultado = noticiaService.obtenerNoticiasRecomendadas(1L);

        // Assert
        assertTrue(resultado.isEmpty());
    }
}