package com.greensqa.datagen.generator;

import com.greensqa.datagen.model.Identity;
import com.greensqa.datagen.patterns.builder.IdentityBuilder;
import com.greensqa.datagen.patterns.factory.DocumentStrategyFactory;
import com.greensqa.datagen.patterns.singleton.UniquenessRegistry;
import com.greensqa.datagen.patterns.strategy.DocumentStrategy;
import com.greensqa.datagen.util.Catalog;

import java.util.List;
import java.util.random.RandomGenerator;

/**
 * Generador de una identidad válida.
 *
 * <p>Principio SOLID - SRP (Single Responsibility): esta clase tiene una única
 * responsabilidad: producir una {@link Identity} que cumpla TODAS las reglas de
 * negocio. No persiste, no exporta, no envía correo.</p>
 *
 * <p>Principio SOLID - OCP (Open/Closed): apoyándose en la factory y las estrategias,
 * se pueden añadir nuevas reglas de documento sin modificar esta clase.</p>
 *
 * <p>Es thread-safe: usa un {@link RandomGenerator} por invocación y el registro
 * Singleton para garantizar unicidad global aún en ejecución paralela.</p>
 */
public class IdentityGenerator {

    /** Probabilidad de que una identidad sea empresa. */
    private static final double PROB_EMPRESA = 0.2;

    private final DocumentStrategyFactory documentFactory = new DocumentStrategyFactory();
    private final UniquenessRegistry registry = UniquenessRegistry.getInstance();

    /**
     * Genera una identidad válida y única.
     * @param rnd fuente de aleatoriedad (independiente por hilo)
     */
    public Identity generar(RandomGenerator rnd) {
        // Reintenta hasta lograr nombre+apellido únicos.
        for (int intento = 0; intento < 1000; intento++) {
            boolean esEmpresa = rnd.nextDouble() < PROB_EMPRESA;
            Identity candidato = construirCandidato(esEmpresa, rnd);

            if (!candidato.esValido()) {
                continue;
            }
            // Unicidad nombre+apellido
            if (!registry.reservarNombreCompleto(candidato.claveNombreCompleto())) {
                continue;
            }
            // Unicidad documento
            if (!registry.reservarDocumento(candidato.getDocumento())) {
                // libera la reserva de nombre no es trivial; aceptamos reintento
                continue;
            }
            return candidato;
        }
        throw new IllegalStateException(
                "No fue posible generar una identidad única tras múltiples intentos. "
                        + "Reduzca la cantidad o amplíe los catálogos.");
    }

    private Identity construirCandidato(boolean esEmpresa, RandomGenerator rnd) {
        int edad = 11 + rnd.nextInt(69); // [11, 79]

        // País e idioma respetando la regla Colombia/Español
        String pais = Catalog.PAISES.get(rnd.nextInt(Catalog.PAISES.size()));
        List<String> idiomas = Catalog.PAIS_IDIOMAS.get(pais);
        String idioma = idiomas.get(rnd.nextInt(idiomas.size()));
        List<String> ciudades = Catalog.PAIS_CIUDADES.get(pais);
        String ciudad = ciudades.get(rnd.nextInt(ciudades.size()));

        // Documento según tipo/edad (Factory -> Strategy)
        DocumentStrategy strategy = documentFactory.crear(esEmpresa, edad);
        String documento = strategy.generar(rnd);

        IdentityBuilder builder = new IdentityBuilder()
                .empresa(esEmpresa)
                .edad(edad)
                .documento(documento)
                .ciudad(ciudad)
                .pais(pais)
                .idioma(idioma);

        if (esEmpresa) {
            String razon = Catalog.RAZONES_SOCIALES.get(
                    rnd.nextInt(Catalog.RAZONES_SOCIALES.size()));
            // sufijo para ampliar combinaciones únicas
            builder.nombre(razon + " " + sufijoCorporativo(rnd));
        } else {
            String nombre = Catalog.NOMBRES.get(rnd.nextInt(Catalog.NOMBRES.size()));
            String apellido = Catalog.APELLIDOS.get(rnd.nextInt(Catalog.APELLIDOS.size()))
                    + " " + Catalog.APELLIDOS.get(rnd.nextInt(Catalog.APELLIDOS.size()));
            builder.nombre(nombre).apellido(apellido);
        }
        return builder.build();
    }

    private String sufijoCorporativo(RandomGenerator rnd) {
        String[] sufijos = {"S.A.", "S.A.S.", "Ltda.", "Group", "Holdings", "Corp."};
        return sufijos[rnd.nextInt(sufijos.length)];
    }
}
