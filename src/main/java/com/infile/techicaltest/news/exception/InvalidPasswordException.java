package com.infile.techicaltest.news.exception;

public class InvalidPasswordException extends GenericException {
    public InvalidPasswordException() {
        super("Contraseña incorrecta");
    }
}