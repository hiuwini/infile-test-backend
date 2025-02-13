package com.infile.techicaltest.news.service.impl;

import com.infile.techicaltest.news.dto.NoticiaDTO;
import com.infile.techicaltest.news.entity.Noticia;
import com.infile.techicaltest.news.entity.NoticiaRecomendada;
import com.infile.techicaltest.news.exception.GenericException;
import com.infile.techicaltest.news.repository.NoticiaRecomendadaRepository;
import com.infile.techicaltest.news.repository.NoticiaRepository;
import com.infile.techicaltest.news.service.INoticiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoticiaService implements INoticiaService {

    @Autowired
    private NoticiaRepository noticiaRepository;

    @Autowired
    private NoticiaRecomendadaRepository noticiaRecomendadaRepository;

    public List<NoticiaDTO> listarNoticias() {
        // Convertir Noticia a NoticiaDto
        return noticiaRepository.findAll().stream()
                .map(n -> NoticiaDTO.builder()
                        .id(n.getId())
                        .titulo(n.getTitulo())
                        .imagen(n.getUrlImagen())
                        .descripcion(n.getDescripcion())
                        .build())
                .collect(Collectors.toList());
    }

    public Noticia obtenerNoticiaPorId(Long id) {
        return noticiaRepository.findById(id)
                .orElseThrow(() -> new GenericException("Noticia no encontrada"));
    }

    public List<Noticia> obtenerNoticiasRecomendadas(Long noticiaId) {
        Noticia noticiaBase = obtenerNoticiaPorId(noticiaId);
        List<NoticiaRecomendada> recomendadas =
                noticiaRecomendadaRepository.findByNoticia(noticiaBase);

        return recomendadas.stream()
                .map(NoticiaRecomendada::getNoticiaRelacionada)
                .collect(Collectors.toList());
    }
}