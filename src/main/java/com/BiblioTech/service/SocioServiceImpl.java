package com.BiblioTech.service;

import com.BiblioTech.exception.DniDuplicadoException;
import com.BiblioTech.exception.EmailInvalidoException;
import com.BiblioTech.model.Socio;
import com.BiblioTech.repository.SocioRepository;
import java.util.List;
import java.util.Optional;

public class SocioServiceImpl implements SocioService {
    private final SocioRepository socioRepository;

    public SocioServiceImpl(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    @Override
    public void registrarSocio(Socio socio) throws DniDuplicadoException, EmailInvalidoException {
        if (socioRepository.existeDni(socio.getDni())) {
            throw new DniDuplicadoException(socio.getDni());
        }
        if (!socio.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new EmailInvalidoException(socio.getEmail());
        }
        socioRepository.guardar(socio);
    }

    @Override
    public Optional<Socio> buscarPorDni(int dni) {
        return socioRepository.buscarPorId(dni);
    }

    @Override
    public List<Socio> listarTodos() {
        return socioRepository.buscarTodos();
    }
}