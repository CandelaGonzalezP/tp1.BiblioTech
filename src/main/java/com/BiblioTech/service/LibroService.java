package com.BiblioTech.service;

import com.BiblioTech.exception.BibliotecaException;
import com.BiblioTech.model.Libro;
import java.util.List;
import java.util.Optional;

public interface LibroService {
    void registrarLibro(Libro libro);
    Optional<Libro> buscarPorIsbn(String isbn);
    List<Libro> buscarPorTitulo(String titulo);
    List<Libro> buscarPorAutor(String autor);
    List<Libro> buscarPorCategoria(String categoria);
    List<Libro> listarTodos();
}