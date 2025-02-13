package com.infile.techicaltest.news.controller;

import com.infile.techicaltest.news.dto.NoticiaDTO;
import com.infile.techicaltest.news.entity.Noticia;
import com.infile.techicaltest.news.service.impl.NoticiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/noticias")
public class NoticiaController {

    @Autowired
    private NoticiaService noticiaService;

    /**
     * 1) Listado de noticias (GET /api/noticias)
     * Protegido por JWT
     */
    @GetMapping
    public List<NoticiaDTO> listarNoticias() {
        return noticiaService.listarNoticias();
    }

    /**
     * 2) Detalles de una noticia (GET /api/noticias/{id})
     * Protegido por JWT
     */
    @GetMapping("/{id}")
    public Noticia obtenerNoticia(@PathVariable Long id) {
        return noticiaService.obtenerNoticiaPorId(id);
    }

    /**
     * 3) Noticias recomendadas (GET /api/noticias/{id}/recomendadas)
     * Protegido por JWT
     */
    @GetMapping("/{id}/recomendadas")
    public List<Noticia> obtenerNoticiasRecomendadas(@PathVariable Long id) {
        return noticiaService.obtenerNoticiasRecomendadas(id);
    }
}