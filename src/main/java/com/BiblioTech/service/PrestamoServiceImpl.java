package com.BiblioTech.service;

import com.BiblioTech.exception.*;
import com.BiblioTech.model.*;
import com.BiblioTech.repository.*;
import java.time.LocalDate;
import java.util.List;

public class PrestamoServiceImpl implements PrestamoService {
    private final LibroRepository libroRepository;
    private final SocioRepository socioRepository;
    private final PrestamoRepositoryImpl prestamoRepository;

    public PrestamoServiceImpl(LibroRepository libroRepository,
                               SocioRepository socioRepository,
                               PrestamoRepositoryImpl prestamoRepository) {
        this.libroRepository = libroRepository;
        this.socioRepository = socioRepository;
        this.prestamoRepository = prestamoRepository;
    }

    @Override
    public void realizarPrestamo(String isbn, int dniSocio) throws BibliotecaException {
        Libro libro = libroRepository.buscarPorId(isbn)
                .orElseThrow(() -> new LibroNoEncontradoException(isbn));

        if (!libro.disponible()) {
            throw new LibroNoDisponibleException(isbn);
        }

        Socio socio = socioRepository.buscarPorId(dniSocio)
                .orElseThrow(() -> new SocioNoEncontradoException(dniSocio));

        if (!socio.puedeTomarPrestamo()) {
            throw new SocioSinCupoException(dniSocio);
        }

        libroRepository.guardar(new Libro(
                libro.isbn(), libro.titulo(), libro.autor(),
                libro.anio(), libro.categoria(), false
        ));

        socio.incrementarPrestamos();

        Prestamo prestamo = new Prestamo(
                prestamoRepository.generarId(),
                socio, libro,
                LocalDate.now(),
                LocalDate.now().plusDays(7)
        );
        prestamoRepository.guardar(prestamo);
    }

    @Override
    public void devolverLibro(String isbn, int dniSocio) throws BibliotecaException {
        Libro libro = libroRepository.buscarPorId(isbn)
                .orElseThrow(() -> new LibroNoEncontradoException(isbn));

        Socio socio = socioRepository.buscarPorId(dniSocio)
                .orElseThrow(() -> new SocioNoEncontradoException(dniSocio));

        libroRepository.guardar(new Libro(
                libro.isbn(), libro.titulo(), libro.autor(),
                libro.anio(), libro.categoria(), true
        ));

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