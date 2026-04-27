package com.BiblioTech.repository;

import com.BiblioTech.model.Prestamo;
import java.util.List;

public interface PrestamoRepository extends Repository<Prestamo, Integer> {
    List<Prestamo> buscarPorSocio(int dniSocio);
}