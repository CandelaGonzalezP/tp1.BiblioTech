package com.BiblioTech.service;

import com.BiblioTech.exception.BibliotecaException;
import com.BiblioTech.exception.IsbnDuplicadoException;
import com.BiblioTech.model.Ebook;
import com.BiblioTech.model.Libro;
import com.BiblioTech.model.Recurso;
import com.BiblioTech.repository.EbookRepository;
import com.BiblioTech.repository.LibroRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LibroServiceImpl implements LibroService {
    private final LibroRepository libroRepository;
    private final EbookRepository ebookRepository;

    public LibroServiceImpl(LibroRepository libroRepository, EbookRepository ebookRepository) {
        this.libroRepository = libroRepository;
        this.ebookRepository = ebookRepository;
    }

    @Override
    public void registrarLibro(Recurso recurso) throws BibliotecaException {
        if (recurso instanceof Libro libro) {
            if (libroRepository.buscarPorId(libro.isbn()).isPresent() ||
                    ebookRepository.buscarPorId(libro.isbn()).isPresent()) {
                throw new IsbnDuplicadoException(libro.isbn());
            }
            libroRepository.guardar(libro);
        }
    }

    @Override
    public void registrarEbook(Ebook ebook) throws BibliotecaException {
        if (libroRepository.buscarPorId(ebook.isbn()).isPresent() ||
                ebookRepository.buscarPorId(ebook.isbn()).isPresent()) {
            throw new IsbnDuplicadoException(ebook.isbn());
        }
        ebookRepository.guardar(ebook);
    }

    @Override
    public Optional<Recurso> buscarPorIsbn(String isbn) {
        Optional<Libro> libro = libroRepository.buscarPorId(isbn);
        if (libro.isPresent()) return Optional.of(libro.get());
        return ebookRepository.buscarPorId(isbn).map(e -> (Recurso) e);
    }

    @Override
    public List<Recurso> buscarPorTitulo(String titulo) {
        List<Recurso> resultados = new ArrayList<>(libroRepository.buscarPorTitulo(titulo));
        resultados.addAll(ebookRepository.buscarPorTitulo(titulo));
        return resultados;
    }

    @Override
    public List<Recurso> buscarPorAutor(String autor) {
        List<Recurso> resultados = new ArrayList<>(libroRepository.buscarPorAutor(autor));
        resultados.addAll(ebookRepository.buscarPorAutor(autor));
        return resultados;
    }

    @Override
    public List<Recurso> buscarPorCategoria(String categoria) {
        List<Recurso> resultados = new ArrayList<>(libroRepository.buscarPorCategoria(categoria));
        resultados.addAll(ebookRepository.buscarPorCategoria(categoria));
        return resultados;
    }

    @Override
    public List<Recurso> listarTodos() {
        List<Recurso> todos = new ArrayList<>(libroRepository.buscarTodos());
        todos.addAll(ebookRepository.buscarTodos());
        return todos;
    }
}