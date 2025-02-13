package com.infile.techicaltest.news.repository;

import com.infile.techicaltest.news.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
