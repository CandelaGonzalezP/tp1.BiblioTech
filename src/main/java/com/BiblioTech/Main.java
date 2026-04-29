package com.BiblioTech;

import com.BiblioTech.exception.BibliotecaException;
import com.BiblioTech.exception.CategoriaInvalidaException;
import com.BiblioTech.model.*;
import com.BiblioTech.repository.*;
import com.BiblioTech.service.*;
import com.BiblioTech.persistence.PersistenciaService;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static LibroService libroService;
    private static SocioService socioService;
    private static PrestamoService prestamoService;
    private static PersistenciaService persistencia;

    public static void main(String[] args) {
        LibroRepository libroRepo = new LibroRepositoryImpl();
        EbookRepository ebookRepo = new EbookRepositoryImpl();
        SocioRepository socioRepo = new SocioRepositoryImpl();
        PrestamoRepositoryImpl prestamoRepo = new PrestamoRepositoryImpl();

        libroService = new LibroServiceImpl(libroRepo, ebookRepo);
        socioService = new SocioServiceImpl(socioRepo);
        prestamoService = new PrestamoServiceImpl(libroRepo, ebookRepo, socioRepo, prestamoRepo);
        persistencia = new PersistenciaService(libroRepo, ebookRepo, socioRepo);
        persistencia.cargarLibros();
        persistencia.cargarEbooks();
        persistencia.cargarSocios();

        int opcion;
        do {
            mostrarMenu();
            opcion = leerInt();
            switch (opcion) {
                case 1 -> registrarLibro();
                case 2 -> registrarEbook();
                case 3 -> registrarSocio();
                case 4 -> realizarPrestamo();
                case 5 -> devolverLibro();
                case 6 -> listarLibros();
                case 7 -> listarSocios();
                case 8 -> listarPrestamos();
                case 9 -> buscarLibro();
                case 10 -> {
                    persistencia.guardarLibros();
                    persistencia.guardarEbooks();
                    persistencia.guardarSocios();
                }
                case 0 -> System.out.println("¡Hasta luego!");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private static void mostrarMenu() {
        System.out.println("\n=== BiblioTech ===");
        System.out.println("1. Registrar libro físico");
        System.out.println("2. Registrar Ebook");
        System.out.println("3. Registrar socio");
        System.out.println("4. Realizar préstamo");
        System.out.println("5. Devolver libro");
        System.out.println("6. Listar libros");
        System.out.println("7. Listar socios");
        System.out.println("8. Listar préstamos");
        System.out.println("9. Buscar libro");
        System.out.println("10. Guardar datos");
        System.out.println("0. Salir");
        System.out.print("Opción: ");
    }

    private static CategoriaLibro leerCategoria() throws CategoriaInvalidaException {
        String input = scanner.nextLine().toUpperCase();
        try {
            return CategoriaLibro.valueOf(input);
        } catch (IllegalArgumentException e) {
            throw new CategoriaInvalidaException(input);
        }
    }

    private static void registrarLibro() {
        try {
            System.out.print("ISBN: ");
            String isbn = scanner.nextLine();
            System.out.print("Título: ");
            String titulo = scanner.nextLine();
            System.out.print("Autor: ");
            String autor = scanner.nextLine();
            System.out.print("Año: ");
            int anio = leerInt();
            System.out.print("Categoría (CIENCIA, HISTORIA, LITERATURA, TECNOLOGIA, ARTE, OTRO): ");
            CategoriaLibro categoria = leerCategoria();

            Libro libro = new Libro(isbn, titulo, autor, anio, categoria, true);
            libroService.registrarLibro(libro);
            System.out.println("Libro registrado correctamente.");
        } catch (BibliotecaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void registrarEbook() {
        try {
            System.out.print("ISBN: ");
            String isbn = scanner.nextLine();
            System.out.print("Título: ");
            String titulo = scanner.nextLine();
            System.out.print("Autor: ");
            String autor = scanner.nextLine();
            System.out.print("Año: ");
            int anio = leerInt();
            System.out.print("Categoría (CIENCIA, HISTORIA, LITERATURA, TECNOLOGIA, ARTE, OTRO): ");
            CategoriaLibro categoria = leerCategoria();
            System.out.print("Formato de archivo (PDF, EPUB, MOBI): ");
            String formato = scanner.nextLine();

            Ebook ebook = new Ebook(isbn, titulo, autor, anio, categoria, formato);
            libroService.registrarEbook(ebook);
            System.out.println("Ebook registrado correctamente.");
        } catch (BibliotecaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void registrarSocio() {
        System.out.print("DNI: ");
        int dni = leerInt();
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Tipo (ESTUDIANTE / DOCENTE): ");
        String tipo = scanner.nextLine().toUpperCase();

        Socio socio = tipo.equals("DOCENTE") ? new Docente(dni, nombre, email) : new Estudiante(dni, nombre, email);

        try {
            socioService.registrarSocio(socio);
            System.out.println("Socio registrado correctamente.");
        } catch (BibliotecaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void realizarPrestamo() {
        System.out.print("ISBN del libro: ");
        String isbn = scanner.nextLine();
        System.out.print("DNI del socio: ");
        int dni = leerInt();

        try {
            prestamoService.realizarPrestamo(isbn, dni);
            System.out.println("Préstamo realizado correctamente.");
        } catch (BibliotecaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void devolverLibro() {
        System.out.print("ISBN del libro: ");
        String isbn = scanner.nextLine();
        System.out.print("DNI del socio: ");
        int dni = leerInt();

        try {
            prestamoService.devolverLibro(isbn, dni);
            System.out.println("Devolución registrada correctamente.");
        } catch (BibliotecaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void listarLibros() {
        List<Recurso> libros = libroService.listarTodos();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
            return;
        }
        libros.forEach(r -> {
            if (r instanceof Libro l) {
                System.out.println("[FÍSICO] " + l.isbn() + " | " + l.titulo() + " | " + l.autor() + " | " + (l.disponible() ? "Disponible" : "Prestado"));
            } else if (r instanceof Ebook e) {
                System.out.println("[EBOOK] " + e.isbn() + " | " + e.titulo() + " | " + e.autor() + " | " + e.formatoArchivo());
            }
        });
    }

    private static void listarSocios() {
        List<Socio> socios = socioService.listarTodos();
        if (socios.isEmpty()) {
            System.out.println("No hay socios registrados.");
            return;
        }
        socios.forEach(s -> System.out.println(
                s.getDni() + " | " + s.getNombre() + " | " + s.getTipo() +
                        (s.estaSancionado() ? " | SANCIONADO hasta: " + s.getFechaFinSancion() : " | Habilitado")
        ));
    }

    private static void listarPrestamos() {
        List<Prestamo> prestamos = prestamoService.listarPrestamos();
        if (prestamos.isEmpty()) {
            System.out.println("No hay préstamos registrados.");
            return;
        }
        prestamos.forEach(p -> System.out.println(
                "ID: " + p.id() + " | " + p.recurso().titulo() + " | " + p.socio().getNombre() +
                        " | Prestado: " + p.fechaPrestamo() +
                        " | Vence: " + p.fechaDevolucionEsperada()
        ));
    }

    private static void buscarLibro() {
        System.out.println("Buscar por:");
        System.out.println("1. Título");
        System.out.println("2. Autor");
        System.out.println("3. Categoría");
        System.out.print("Opción: ");
        int opcion = leerInt();

        List<Recurso> resultados;
        switch (opcion) {
            case 1 -> {
                System.out.print("Título: ");
                resultados = libroService.buscarPorTitulo(scanner.nextLine());
            }
            case 2 -> {
                System.out.print("Autor: ");
                resultados = libroService.buscarPorAutor(scanner.nextLine());
            }
            case 3 -> {
                System.out.print("Categoría (CIENCIA, HISTORIA, LITERATURA, TECNOLOGIA, ARTE, OTRO): ");
                resultados = libroService.buscarPorCategoria(scanner.nextLine().toUpperCase());
            }
            default -> {
                System.out.println("Opción inválida.");
                return;
            }
        }

        if (resultados.isEmpty()) {
            System.out.println("No se encontraron resultados.");
            return;
        }
        resultados.forEach(r -> {
            if (r instanceof Libro l) {
                System.out.println("[FÍSICO] " + l.isbn() + " | " + l.titulo() + " | " + l.autor() + " | " + l.categoria());
            } else if (r instanceof Ebook e) {
                System.out.println("[EBOOK] " + e.isbn() + " | " + e.titulo() + " | " + e.autor() + " | " + e.categoria());
            }
        });
    }

    private static int leerInt() {
        return Integer.parseInt(scanner.nextLine());
    }
}