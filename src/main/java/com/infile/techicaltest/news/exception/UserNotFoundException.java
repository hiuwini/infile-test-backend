package com.infile.techicaltest.news.exception;

public class UserNotFoundException extends GenericException {
    public UserNotFoundException() {
        super("Usuario no encontrado");
    }
}
