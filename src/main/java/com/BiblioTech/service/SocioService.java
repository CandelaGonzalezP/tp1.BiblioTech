package com.BiblioTech.service;

import com.BiblioTech.exception.BibliotecaException;
import com.BiblioTech.model.Socio;
import java.util.List;
import java.util.Optional;

public interface SocioService {
    void registrarSocio(Socio socio) throws BibliotecaException;
    Optional<Socio> buscarPorDni(int dni);
    List<Socio> listarTodos();
}