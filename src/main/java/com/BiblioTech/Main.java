package com.BiblioTech;

import com.BiblioTech.exception.BibliotecaException;
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
        SocioRepository socioRepo = new SocioRepositoryImpl();
        PrestamoRepositoryImpl prestamoRepo = new PrestamoRepositoryImpl();

        libroService = new LibroServiceImpl(libroRepo);
        socioService = new SocioServiceImpl(socioRepo);
        prestamoService = new PrestamoServiceImpl(libroRepo, socioRepo, prestamoRepo);
        persistencia = new PersistenciaService(libroRepo, socioRepo);
        persistencia.cargarLibros();
        persistencia.cargarSocios();

        int opcion;
        do {
            mostrarMenu();
            opcion = leerInt();
            switch (opcion) {
                case 1 -> registrarLibro();
                case 2 -> registrarSocio();
                case 3 -> realizarPrestamo();
                case 4 -> devolverLibro();
                case 5 -> listarLibros();
                case 6 -> listarSocios();
                case 7 -> listarPrestamos();
                case 8 -> {
                    persistencia.guardarLibros();
                    persistencia.guardarSocios();
                }
                case 0 -> System.out.println("¡Hasta luego!");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private static void mostrarMenu() {
        System.out.println("\n=== BiblioTech ===");
        System.out.println("1. Registrar libro");
        System.out.println("2. Registrar socio");
        System.out.println("3. Realizar préstamo");
        System.out.println("4. Devolver libro");
        System.out.println("5. Listar libros");
        System.out.println("6. Listar socios");
        System.out.println("7. Listar préstamos");
        System.out.println("8. Guardar datos");
        System.out.println("0. Salir");
        System.out.print("Opción: ");
    }

    private static void registrarLibro() {
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        System.out.print("Autor: ");
        String autor = scanner.nextLine();
        System.out.print("Año: ");
        int anio = leerInt();
        System.out.println("Categoría (CIENCIA, HISTORIA, LITERATURA, TECNOLOGIA, ARTE, OTRO): ");
        CategoriaLibro categoria = CategoriaLibro.valueOf(scanner.nextLine().toUpperCase());

        Libro libro = new Libro(isbn, titulo, autor, anio, categoria, true);
        libroService.registrarLibro(libro);
        System.out.println("Libro registrado correctamente.");
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
        List<Libro> libros = libroService.listarTodos();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
            return;
        }
        libros.forEach(l -> System.out.println(
                l.isbn() + " | " + l.titulo() + " | " + l.autor() + " | " + (l.disponible() ? "Disponible" : "Prestado")
        ));
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
                        " | Hasta: " + p.fechaDevolucionEsperada()
        ));
    }

    private static int leerInt() {
        return Integer.parseInt(scanner.nextLine());
    }
}