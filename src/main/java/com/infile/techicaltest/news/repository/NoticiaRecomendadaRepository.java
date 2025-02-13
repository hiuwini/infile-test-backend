package com.infile.techicaltest.news.repository;

import com.infile.techicaltest.news.entity.Noticia;
import com.infile.techicaltest.news.entity.NoticiaRecomendada;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticiaRecomendadaRepository extends JpaRepository<NoticiaRecomendada, Long> {

    List<NoticiaRecomendada> findByNoticia(Noticia noticia);

}
