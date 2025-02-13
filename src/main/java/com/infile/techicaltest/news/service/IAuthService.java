package com.infile.techicaltest.news.service;

import com.infile.techicaltest.news.dto.AuthRequest;
import com.infile.techicaltest.news.dto.AuthResponse;
import com.infile.techicaltest.news.dto.RegisterResponse;
import com.infile.techicaltest.news.entity.Usuario;

public interface IAuthService {

    public AuthResponse authenticate(AuthRequest request);

    public RegisterResponse register(Usuario usuario);

}
