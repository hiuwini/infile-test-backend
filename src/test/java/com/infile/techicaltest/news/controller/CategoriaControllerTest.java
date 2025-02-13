package com.infile.techicaltest.news.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import com.infile.techicaltest.news.entity.Categoria;
import com.infile.techicaltest.news.service.impl.CategoriaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoriaControllerTest {

    @Mock
    private CategoriaService categoriaService;

    @InjectMocks
    private CategoriaController categoriaController;

    @Test
    void listarCategorias_ConRegistros_RetornaListaCompleta() {
        // Arrange
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Deportes");

        when(categoriaService.listarCategorias()).thenReturn(List.of(categoria));

        // Act
        List<Categoria> resultado = categoriaController.listarCategorias();

        // Assert
        assertEquals(1, resultado.size());
        assertEquals("Deportes", resultado.get(0).getNombre());
        verify(categoriaService).listarCategorias();
    }

    @Test
    void listarCategorias_SinRegistros_RetornaListaVacia() {
        // Arrange
        when(categoriaService.listarCategorias()).thenReturn(Collections.emptyList());

        // Act
        List<Categoria> resultado = categoriaController.listarCategorias();

        // Assert
        assertTrue(resultado.isEmpty());
        verify(categoriaService).listarCategorias();
    }
}