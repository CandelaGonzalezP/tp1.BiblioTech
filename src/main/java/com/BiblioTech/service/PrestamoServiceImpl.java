package com.BiblioTech.service;

import com.BiblioTech.exception.*;
import com.BiblioTech.model.*;
import com.BiblioTech.repository.*;
import java.time.LocalDate;
import java.util.List;

public class PrestamoServiceImpl implements PrestamoService {
    private final LibroRepository libroRepository;
    private final EbookRepository ebookRepository;
    private final SocioRepository socioRepository;
    private final PrestamoRepositoryImpl prestamoRepository;

    public PrestamoServiceImpl(LibroRepository libroRepository,
                               EbookRepository ebookRepository,
                               SocioRepository socioRepository,
                               PrestamoRepositoryImpl prestamoRepository) {
        this.libroRepository = libroRepository;
        this.ebookRepository = ebookRepository;
        this.socioRepository = socioRepository;
        this.prestamoRepository = prestamoRepository;
    }

    @Override
    public void realizarPrestamo(String isbn, int dniSocio) throws BibliotecaException {
        Recurso recurso = libroRepository.buscarPorId(isbn)
                .map(l -> (Recurso) l)
                .or(() -> ebookRepository.buscarPorId(isbn).map(e -> (Recurso) e))
                .orElseThrow(() -> new LibroNoEncontradoException(isbn));

        if (recurso instanceof Libro libro && !libro.disponible()) {
            throw new LibroNoDisponibleException(isbn);
        }

        Socio socio = socioRepository.buscarPorId(dniSocio)
                .orElseThrow(() -> new SocioNoEncontradoException(dniSocio));

        if (!socio.puedeTomarPrestamo()) {
            if (socio.estaSancionado()) {
                throw new BibliotecaException("El socio está sancionado hasta: " + socio.getFechaFinSancion());
            }
            throw new SocioSinCupoException(dniSocio);
        }

        if (recurso instanceof Libro libro) {
            libroRepository.guardar(new Libro(
                    libro.isbn(), libro.titulo(), libro.autor(),
                    libro.anio(), libro.categoria(), false
            ));
        }

        socio.incrementarPrestamos();

        Prestamo prestamo = new Prestamo(
                prestamoRepository.generarId(),
                socio, recurso,
                LocalDate.now(),
                LocalDate.now().plusDays(7)
        );
        prestamoRepository.guardar(prestamo);
    }

    @Override
    public void devolverLibro(String isbn, int dniSocio) throws BibliotecaException {
        Recurso recurso = libroRepository.buscarPorId(isbn)
                .map(l -> (Recurso) l)
                .or(() -> ebookRepository.buscarPorId(isbn).map(e -> (Recurso) e))
                .orElseThrow(() -> new LibroNoEncontradoException(isbn));

        Socio socio = socioRepository.buscarPorId(dniSocio)
                .orElseThrow(() -> new SocioNoEncontradoException(dniSocio));

        Prestamo prestamoActivo = prestamoRepository.buscarTodos().stream()
                .filter(p -> p.recurso().isbn().equals(isbn) && p.socio().getDni() == dniSocio)
                .findFirst()
                .orElseThrow(() -> new BibliotecaException("No se encontró préstamo activo."));

        LocalDate hoy = LocalDate.now();
        if (hoy.isAfter(prestamoActivo.fechaDevolucionEsperada())) {
            long diasRetraso = prestamoActivo.fechaDevolucionEsperada().until(hoy).getDays();
            socio.sancionar((int) diasRetraso);
            System.out.println("Devolución con " + diasRetraso + " días de retraso. Socio sancionado hasta: " + socio.getFechaFinSancion());
        }

        if (recurso instanceof Libro libro) {
            libroRepository.guardar(new Libro(
                    libro.isbn(), libro.titulo(), libro.autor(),
                    libro.anio(), libro.categoria(), true
            ));
        }

        socio.decrementarPrestamos();
    }

    @Override
    public List<Prestamo> listarPrestamos() {
        return prestamoRepository.buscarTodos();
    }

    @Override
    public List<Prestamo> listarPorSocio(int dniSocio) {
        return prestamoRepository.buscarPorSocio(dniSocio);
    }
}