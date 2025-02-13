package com.infile.techicaltest.news.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import com.infile.techicaltest.news.entity.Categoria;
import com.infile.techicaltest.news.repository.CategoriaRepository;
import com.infile.techicaltest.news.service.impl.CategoriaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    void listarCategorias_ConDatos_RetornaListaCompleta() {
        // Arrange
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Tecnología");

        when(categoriaRepository.findAll()).thenReturn(List.of(categoria));

        // Act
        List<Categoria> resultado = categoriaService.listarCategorias();

        // Assert
        assertEquals(1, resultado.size());
        assertEquals("Tecnología", resultado.get(0).getNombre());
        verify(categoriaRepository).findAll();
    }

    @Test
    void listarCategorias_SinDatos_RetornaListaVacia() {
        // Arrange
        when(categoriaRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Categoria> resultado = categoriaService.listarCategorias();

        // Assert
        assertTrue(resultado.isEmpty());
        verify(categoriaRepository).findAll();
    }
}