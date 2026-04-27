package com.BiblioTech.repository;

import com.BiblioTech.model.Socio;

public interface SocioRepository extends Repository<Socio, Integer> {
    boolean existeDni(int dni);
    boolean existeEmail(String email);
}