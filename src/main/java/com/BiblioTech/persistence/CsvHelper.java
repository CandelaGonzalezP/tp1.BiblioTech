package com.BiblioTech.persistence;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CsvHelper {

    public static void escribir(String archivo, List<String> lineas) {
        try {
            Path path = Paths.get(archivo);
            Files.createDirectories(path.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                for (String linea : lineas) {
                    writer.write(linea);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error al escribir CSV: " + e.getMessage());
        }
    }

    public static List<String> leer(String archivo) {
        List<String> lineas = new ArrayList<>();
        Path path = Paths.get(archivo);
        if (!Files.exists(path)) return lineas;
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.isBlank()) lineas.add(linea);
            }
        } catch (IOException e) {
            System.out.println("Error al leer CSV: " + e.getMessage());
        }
        return lineas;
    }
}