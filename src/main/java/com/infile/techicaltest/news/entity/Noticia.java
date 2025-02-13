package com.infile.techicaltest.news.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "noticias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Noticia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private String descripcion;

    private String urlImagen;

    private LocalDateTime fechaPublicacion;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

}
