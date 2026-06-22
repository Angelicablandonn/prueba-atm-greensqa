package com.greensqa.datagen.export;

import com.greensqa.datagen.model.Identity;
import com.opencsv.CSVWriter;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Exporta identidades a un archivo de texto separado por comas (CSV).
 *
 * <p>Requisito 6 del enunciado. SRP: su única responsabilidad es la exportación.</p>
 */
public class CsvExporter {

    private static final String[] HEADER = {
            "tipo", "nombre", "apellido", "edad", "documento", "ciudad", "pais", "idioma"
    };

    public Path exportar(List<Identity> identidades, Path destino) {
        try {
            if (destino.getParent() != null) {
                Files.createDirectories(destino.getParent());
            }
            try (Writer w = Files.newBufferedWriter(destino, StandardCharsets.UTF_8);
                 CSVWriter csv = new CSVWriter(w)) {
                csv.writeNext(HEADER);
                for (Identity id : identidades) {
                    csv.writeNext(new String[]{
                            id.getTipoIdentidad(),
                            nullSafe(id.getNombre()),
                            nullSafe(id.getApellido()),
                            String.valueOf(id.getEdad()),
                            nullSafe(id.getDocumento()),
                            nullSafe(id.getCiudad()),
                            nullSafe(id.getPais()),
                            nullSafe(id.getIdioma())
                    });
                }
            }
            return destino;
        } catch (IOException e) {
            throw new ExportException("No se pudo exportar el CSV: " + destino, e);
        }
    }

    private String nullSafe(String s) {
        return s == null ? "" : s;
    }

    public static class ExportException extends RuntimeException {
        public ExportException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
