package com.greensqa.datagen.patterns.strategy;

import java.util.random.RandomGenerator;

/**
 * Estrategia de documento para MENORES de edad: debe generarse a partir de
 * 11000000 (>= 11.000.000). Se suma un offset aleatorio amplio para evitar choques.
 */
public class MinorDocumentStrategy implements DocumentStrategy {
    private static final long BASE = 11_000_000L;
    @Override
    public String generar(RandomGenerator rnd) {
        long offset = rnd.nextLong(0, 90_000_000L);
        return String.valueOf(BASE + offset);
    }
}
