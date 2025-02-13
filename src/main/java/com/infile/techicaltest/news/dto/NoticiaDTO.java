package com.infile.techicaltest.news.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticiaDTO {

    private Long id;
    private String titulo;
    private String imagen;
    private String descripcion;

}
