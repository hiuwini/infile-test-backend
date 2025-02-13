package com.infile.techicaltest.news.repository;

import com.infile.techicaltest.news.entity.Noticia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticiaRepository extends JpaRepository<Noticia, Long> {
}
