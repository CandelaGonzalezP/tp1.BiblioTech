package com.BiblioTech.service;

import com.BiblioTech.model.Libro;
import com.BiblioTech.repository.LibroRepository;
import java.util.List;
import java.util.Optional;

public class LibroServiceImpl implements LibroService {
    private final LibroRepository libroRepository;

    public LibroServiceImpl(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @Override
    public void registrarLibro(Libro libro) {
        libroRepository.guardar(libro);
    }

    @Override
    public Optional<Libro> buscarPorIsbn(String isbn) {
        return libroRepository.buscarPorId(isbn);
    }

    @Override
    public List<Libro> buscarPorTitulo(String titulo) {
        return libroRepository.buscarPorTitulo(titulo);
    }

    @Override
    public List<Libro> buscarPorAutor(String autor) {
        return libroRepository.buscarPorAutor(autor);
    }

    @Override
    public List<Libro> buscarPorCategoria(String categoria) {
        return libroRepository.buscarPorCategoria(categoria);
    }

    @Override
    public List<Libro> listarTodos() {
        return libroRepository.buscarTodos();
    }
}