package com.greensqa.datagen.patterns.strategy;

import java.util.random.RandomGenerator;

/**
 * Patrón de diseño STRATEGY.
 *
 * <p>Define una familia de algoritmos intercambiables para generar el documento de
 * identificación. Cada regla del enunciado (empresa, menor de edad, mayor de edad)
 * se implementa como una estrategia concreta, y el generador elige la estrategia
 * adecuada en tiempo de ejecución sin condicionales acoplados.</p>
 *
 * <p>Soporta también el principio OCP (Open/Closed): para una nueva regla de
 * documento basta agregar una nueva estrategia, sin modificar las existentes.</p>
 */
public interface DocumentStrategy {

    /**
     * Genera un documento candidato.
     * @param rnd fuente de aleatoriedad (inyectada para ser thread-safe)
     */
    String generar(RandomGenerator rnd);
}
