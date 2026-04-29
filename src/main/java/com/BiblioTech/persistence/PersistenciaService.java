package com.BiblioTech.persistence;

import com.BiblioTech.model.*;
import com.BiblioTech.repository.*;

import java.util.List;
import java.util.stream.Collectors;

public class PersistenciaService {

    private static final String LIBROS_CSV = "data/libros.csv";
    private static final String EBOOKS_CSV = "data/ebooks.csv";
    private static final String SOCIOS_CSV = "data/socios.csv";

    private final LibroRepository libroRepository;
    private final EbookRepository ebookRepository;
    private final SocioRepository socioRepository;

    public PersistenciaService(LibroRepository libroRepository, EbookRepository ebookRepository, SocioRepository socioRepository) {
        this.libroRepository = libroRepository;
        this.ebookRepository = ebookRepository;
        this.socioRepository = socioRepository;
    }

    public void guardarLibros() {
        List<String> lineas = libroRepository.buscarTodos().stream()
                .map(l -> l.isbn() + "," + l.titulo() + "," + l.autor() + "," + l.anio() + "," + l.categoria() + "," + l.disponible())
                .collect(Collectors.toList());
        CsvHelper.escribir(LIBROS_CSV, lineas);
        System.out.println("Libros guardados.");
    }

    public void cargarLibros() {
        List<String> lineas = CsvHelper.leer(LIBROS_CSV);
        for (String linea : lineas) {
            String[] p = linea.split(",");
            Libro libro = new Libro(p[0], p[1], p[2], Integer.parseInt(p[3]), CategoriaLibro.valueOf(p[4]), Boolean.parseBoolean(p[5]));
            libroRepository.guardar(libro);
        }
    }

    public void guardarEbooks() {
        List<String> lineas = ebookRepository.buscarTodos().stream()
                .map(e -> e.isbn() + "," + e.titulo() + "," + e.autor() + "," + e.anio() + "," + e.categoria() + "," + e.formatoArchivo())
                .collect(Collectors.toList());
        CsvHelper.escribir(EBOOKS_CSV, lineas);
        System.out.println("Ebooks guardados.");
    }

    public void cargarEbooks() {
        List<String> lineas = CsvHelper.leer(EBOOKS_CSV);
        for (String linea : lineas) {
            String[] p = linea.split(",");
            Ebook ebook = new Ebook(p[0], p[1], p[2], Integer.parseInt(p[3]), CategoriaLibro.valueOf(p[4]), p[5]);
            ebookRepository.guardar(ebook);
        }
    }

    public void guardarSocios() {
        List<String> lineas = socioRepository.buscarTodos().stream()
                .map(s -> s.getDni() + "," + s.getNombre() + "," + s.getEmail() + "," + s.getTipo())
                .collect(Collectors.toList());
        CsvHelper.escribir(SOCIOS_CSV, lineas);
        System.out.println("Socios guardados.");
    }

    public void cargarSocios() {
        List<String> lineas = CsvHelper.leer(SOCIOS_CSV);
        for (String linea : lineas) {
            String[] p = linea.split(",");
            Socio socio = p[3].equals("DOCENTE")
                    ? new Docente(Integer.parseInt(p[0]), p[1], p[2])
                    : new Estudiante(Integer.parseInt(p[0]), p[1], p[2]);
            socioRepository.guardar(socio);
        }
    }
}