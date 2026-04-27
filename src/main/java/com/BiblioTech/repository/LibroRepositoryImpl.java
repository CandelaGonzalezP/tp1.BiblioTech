package com.BiblioTech.repository;

import com.BiblioTech.model.Libro;
import java.util.*;
import java.util.stream.Collectors;

public class LibroRepositoryImpl implements LibroRepository {
    private final Map<String, Libro> storage = new HashMap<>();

    @Override
    public void guardar(Libro libro) {
        storage.put(libro.isbn(), libro);
    }

    @Override
    public Optional<Libro> buscarPorId(String isbn) {
        return Optional.ofNullable(storage.get(isbn));
    }

    @Override
    public List<Libro> buscarTodos() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Libro> buscarPorTitulo(String titulo) {
        return storage.values().stream()
                .filter(l -> l.titulo().toLowerCase().contains(titulo.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Libro> buscarPorAutor(String autor) {
        return storage.values().stream()
                .filter(l -> l.autor().toLowerCase().contains(autor.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Libro> buscarPorCategoria(String categoria) {
        return storage.values().stream()
                .filter(l -> l.categoria().name().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());
    }
}