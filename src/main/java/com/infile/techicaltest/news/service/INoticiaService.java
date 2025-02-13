package com.infile.techicaltest.news.service;

import com.infile.techicaltest.news.dto.NoticiaDTO;
import com.infile.techicaltest.news.entity.Noticia;

import java.util.List;

public interface INoticiaService {

    public List<NoticiaDTO> listarNoticias();

    public Noticia obtenerNoticiaPorId(Long id);

    public List<Noticia> obtenerNoticiasRecomendadas(Long noticiaId);

}
