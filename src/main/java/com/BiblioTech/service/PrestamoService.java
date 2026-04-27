package com.BiblioTech.service;

import com.BiblioTech.exception.BibliotecaException;
import com.BiblioTech.model.Prestamo;
import java.util.List;

public interface PrestamoService {
    void realizarPrestamo(String isbn, int dniSocio) throws BibliotecaException;
    void devolverLibro(String isbn, int dniSocio) throws BibliotecaException;
    List<Prestamo> listarPrestamos();
    List<Prestamo> listarPorSocio(int dniSocio);
}