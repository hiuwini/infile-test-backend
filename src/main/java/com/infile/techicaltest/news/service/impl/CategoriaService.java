package com.infile.techicaltest.news.service.impl;

import com.infile.techicaltest.news.entity.Categoria;
import com.infile.techicaltest.news.repository.CategoriaRepository;
import com.infile.techicaltest.news.service.ICategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService implements ICategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }
}