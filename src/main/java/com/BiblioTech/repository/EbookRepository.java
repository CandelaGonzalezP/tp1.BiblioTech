package com.BiblioTech.repository;

import com.BiblioTech.model.Ebook;
import java.util.List;

public interface EbookRepository extends Repository<Ebook, String> {
    List<Ebook> buscarPorTitulo(String titulo);
    List<Ebook> buscarPorAutor(String autor);
    List<Ebook> buscarPorCategoria(String categoria);
}