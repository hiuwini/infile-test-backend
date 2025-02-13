package com.infile.techicaltest.news.controller;

import com.infile.techicaltest.news.entity.Categoria;
import com.infile.techicaltest.news.service.impl.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    /**
     * 4) Listado de categorías (GET /api/categorias)
     * Protegido por JWT
     */
    @GetMapping
    public List<Categoria> listarCategorias() {
        return categoriaService.listarCategorias();
    }
}