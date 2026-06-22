package com.greensqa.automation.utils;

import com.greensqa.automation.model.TestPerson;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Lee el CSV producido por el generador de PARTE 1 y lo expone como datos de
 * entrada de la automatización. La ruta se toma de la propiedad del sistema
 * 'testdata.csv' o, por defecto, de ../data-generator/output/.
 */
public final class TestDataProvider {

    private TestDataProvider() { }

    public static List<TestPerson> cargar(String rutaCsv) {
        List<TestPerson> personas = new ArrayList<>();
        try (Reader r = Files.newBufferedReader(Path.of(rutaCsv), StandardCharsets.UTF_8);
             CSVReader csv = new CSVReader(r)) {
            String[] fila;
            boolean header = true;
            while ((fila = csv.readNext()) != null) {
                if (header) { header = false; continue; }
                if (fila.length < 8) continue;
                personas.add(new TestPerson(
                        fila[0], fila[1], fila[2],
                        parseInt(fila[3]), fila[4], fila[5], fila[6], fila[7]));
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("No se pudo leer el CSV de datos de prueba: " + rutaCsv, e);
        }
        if (personas.isEmpty()) {
            throw new IllegalStateException("El CSV de datos de prueba está vacío: " + rutaCsv);
        }
        return personas;
    }

    public static List<TestPerson> cargarPorDefecto() {
        String ruta = System.getProperty("testdata.csv",
                "../data-generator/output/datos_actual.csv");
        return cargar(ruta);
    }

    private static int parseInt(String s) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return 30; }
    }
}
