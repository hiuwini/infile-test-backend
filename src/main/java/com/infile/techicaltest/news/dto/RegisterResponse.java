package com.infile.techicaltest.news.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponse {

    private String nombre;

    private String email;

    private String rol;

    private String estado;

}
