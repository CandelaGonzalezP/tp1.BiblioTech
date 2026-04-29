package com.BiblioTech.exception;

public class IsbnDuplicadoException extends BibliotecaException {
    public IsbnDuplicadoException(String isbn) {
        super("Ya existe un recurso con ISBN: " + isbn);
    }
}