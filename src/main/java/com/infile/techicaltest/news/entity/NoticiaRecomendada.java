package com.infile.techicaltest.news.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "noticias_recomendadas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticiaRecomendada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "noticia_id")
    private Noticia noticia;

    @ManyToOne
    @JoinColumn(name = "noticia_relacionada_id")
    private Noticia noticiaRelacionada;

}
