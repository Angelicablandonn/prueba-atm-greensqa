package com.greensqa.datagen.patterns.factory;

import com.greensqa.datagen.patterns.strategy.AdultDocumentStrategy;
import com.greensqa.datagen.patterns.strategy.CompanyDocumentStrategy;
import com.greensqa.datagen.patterns.strategy.DocumentStrategy;
import com.greensqa.datagen.patterns.strategy.MinorDocumentStrategy;

/**
 * Patrón de diseño FACTORY METHOD.
 *
 * <p>Encapsula la lógica de selección de la {@link DocumentStrategy} adecuada
 * según el tipo de identidad y la edad, de modo que el resto del sistema pide
 * "una estrategia para este caso" sin conocer las clases concretas.</p>
 */
public final class DocumentStrategyFactory {

    private final DocumentStrategy company = new CompanyDocumentStrategy();
    private final DocumentStrategy minor = new MinorDocumentStrategy();
    private final DocumentStrategy adult = new AdultDocumentStrategy();

    /**
     * @param esEmpresa true si la identidad es empresa
     * @param edad edad de la identidad (relevante solo para personas)
     */
    public DocumentStrategy crear(boolean esEmpresa, int edad) {
        if (esEmpresa) {
            return company;
        }
        return edad < 18 ? minor : adult;
    }
}
