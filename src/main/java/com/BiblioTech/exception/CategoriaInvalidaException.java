package com.BiblioTech.exception;

public class CategoriaInvalidaException extends BibliotecaException {
    public CategoriaInvalidaException(String categoria) {
        super("La categoría '" + categoria + "' no es válida. Use: CIENCIA, HISTORIA, LITERATURA, TECNOLOGIA, ARTE, OTRO");
    }
}