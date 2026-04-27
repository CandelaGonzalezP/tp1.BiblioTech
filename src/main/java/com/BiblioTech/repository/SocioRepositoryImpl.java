package com.BiblioTech.repository;

import com.BiblioTech.model.Socio;
import java.util.*;

public class SocioRepositoryImpl implements SocioRepository {
    private final Map<Integer, Socio> storage = new HashMap<>();

    @Override
    public void guardar(Socio socio) {
        storage.put(socio.getDni(), socio);
    }

    @Override
    public Optional<Socio> buscarPorId(Integer dni) {
        return Optional.ofNullable(storage.get(dni));
    }

    @Override
    public List<Socio> buscarTodos() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean existeDni(int dni) {
        return storage.containsKey(dni);
    }

    @Override
    public boolean existeEmail(String email) {
        return storage.values().stream()
                .anyMatch(s -> s.getEmail().equalsIgnoreCase(email));
    }
}