package com.BiblioTech.service;

import com.BiblioTech.exception.BibliotecaException;
import com.BiblioTech.model.Ebook;
import com.BiblioTech.model.Libro;
import com.BiblioTech.model.Recurso;
import java.util.List;
import java.util.Optional;

public interface LibroService {
    void registrarLibro(Recurso recurso) throws BibliotecaException;
    void registrarEbook(Ebook ebook) throws BibliotecaException;
    Optional<Recurso> buscarPorIsbn(String isbn);
    List<Recurso> buscarPorTitulo(String titulo);
    List<Recurso> buscarPorAutor(String autor);
    List<Recurso> buscarPorCategoria(String categoria);
    List<Recurso> listarTodos();
}