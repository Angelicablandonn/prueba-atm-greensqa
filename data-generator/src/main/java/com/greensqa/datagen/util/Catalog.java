package com.greensqa.datagen.util;

import java.util.List;
import java.util.Map;

/**
 * Catálogo de datos de referencia para la generación.
 *
 * <p>Centraliza nombres, apellidos, razones sociales y la relación
 * país -> ciudades / idiomas. La regla de negocio crítica aquí es:
 * "si el país es diferente a Colombia, el idioma no puede ser Español",
 * por lo que para países no-Colombia ningún idioma disponible es Español.</p>
 */
public final class Catalog {

    private Catalog() { }

    public static final List<String> NOMBRES = List.of(
            "Camila", "Mateo", "Valentina", "Santiago", "Isabella", "Sebastián",
            "Sofía", "Nicolás", "Mariana", "Samuel", "Daniela", "Andrés",
            "Lucía", "Tomás", "Gabriela", "Emiliano", "Antonia", "Joaquín",
            "Renata", "Benjamín", "Catalina", "Martín", "Paula", "Diego");

    public static final List<String> APELLIDOS = List.of(
            "García", "Rodríguez", "Martínez", "López", "González", "Pérez",
            "Sánchez", "Ramírez", "Torres", "Flores", "Rivera", "Gómez",
            "Díaz", "Vargas", "Castro", "Romero", "Herrera", "Medina",
            "Cabrera", "Rojas", "Mendoza", "Aguilar", "Ortiz", "Silva");

    public static final List<String> RAZONES_SOCIALES = List.of(
            "Andes Logistics", "Pacifico Tech", "Caribe Foods", "Altiplano Energy",
            "Sur Airlines", "Cordillera Bank", "Litoral Retail", "Origen Pharma",
            "Vertex Mining", "Aurora Media", "Delta Insurance", "Nimbus Cloud",
            "Pampa Agro", "Quantum Labs", "Solaris Power", "Cumbre Travel");

    /**
     * País -> lista de idiomas válidos para ese país.
     * Colombia es el único país donde aparece "Español".
     */
    public static final Map<String, List<String>> PAIS_IDIOMAS = Map.of(
            "Colombia", List.of("Español"),
            "Brasil", List.of("Portugués"),
            "Estados Unidos", List.of("Inglés"),
            "Francia", List.of("Francés"),
            "Italia", List.of("Italiano"),
            "Alemania", List.of("Alemán"),
            "Canadá", List.of("Inglés", "Francés"),
            "Japón", List.of("Japonés"));

    /** País -> ciudades representativas. */
    public static final Map<String, List<String>> PAIS_CIUDADES = Map.of(
            "Colombia", List.of("Bogotá", "Medellín", "Cali", "Barranquilla"),
            "Brasil", List.of("São Paulo", "Río de Janeiro", "Brasilia"),
            "Estados Unidos", List.of("Miami", "Nueva York", "Los Ángeles"),
            "Francia", List.of("París", "Lyon", "Marsella"),
            "Italia", List.of("Roma", "Milán", "Nápoles"),
            "Alemania", List.of("Berlín", "Múnich", "Hamburgo"),
            "Canadá", List.of("Toronto", "Montreal", "Vancouver"),
            "Japón", List.of("Tokio", "Osaka", "Kioto"));

    public static final List<String> PAISES = List.copyOf(PAIS_IDIOMAS.keySet());
}
