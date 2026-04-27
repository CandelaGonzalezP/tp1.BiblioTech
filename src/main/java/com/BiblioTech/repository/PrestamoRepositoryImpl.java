package com.bibliotech.repository;

import com.BiblioTech.model.Prestamo;
import com.BiblioTech.repository.PrestamoRepository;

import java.util.*;
import java.util.stream.Collectors;

public class PrestamoRepositoryImpl implements PrestamoRepository {
    private final Map<Integer, Prestamo> storage = new HashMap<>();
    private int contadorId = 1;

    @Override
    public void guardar(Prestamo prestamo) {
        storage.put(prestamo.id(), prestamo);
    }

    @Override
    public Optional<Prestamo> buscarPorId(Integer id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Prestamo> buscarTodos() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Prestamo> buscarPorSocio(int dniSocio) {
        return storage.values().stream()
                .filter(p -> p.socio().getDni() == dniSocio)
                .collect(Collectors.toList());
    }

    public int generarId() {
        return contadorId++;
    }
}