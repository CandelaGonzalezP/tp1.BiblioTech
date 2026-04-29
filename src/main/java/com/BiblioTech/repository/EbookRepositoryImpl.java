package com.BiblioTech.repository;

import com.BiblioTech.model.Ebook;
import java.util.*;
import java.util.stream.Collectors;

public class EbookRepositoryImpl implements EbookRepository {
    private final Map<String, Ebook> storage = new HashMap<>();

    @Override
    public void guardar(Ebook ebook) {
        storage.put(ebook.isbn(), ebook);
    }

    @Override
    public Optional<Ebook> buscarPorId(String isbn) {
        return Optional.ofNullable(storage.get(isbn));
    }

    @Override
    public List<Ebook> buscarTodos() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Ebook> buscarPorTitulo(String titulo) {
        return storage.values().stream()
                .filter(e -> e.titulo().toLowerCase().contains(titulo.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Ebook> buscarPorAutor(String autor) {
        return storage.values().stream()
                .filter(e -> e.autor().toLowerCase().contains(autor.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Ebook> buscarPorCategoria(String categoria) {
        return storage.values().stream()
                .filter(e -> e.categoria().name().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());
    }
}