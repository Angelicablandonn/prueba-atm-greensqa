package com.greensqa.datagen.patterns.strategy;

import java.util.random.RandomGenerator;

/**
 * Estrategia de documento para EMPRESAS: el número debe iniciar por "9".
 * Genera un NIT-like de 9 dígitos: 9 + 8 dígitos.
 */
public class CompanyDocumentStrategy implements DocumentStrategy {
    @Override
    public String generar(RandomGenerator rnd) {
        StringBuilder sb = new StringBuilder("9");
        for (int i = 0; i < 8; i++) {
            sb.append(rnd.nextInt(10));
        }
        return sb.toString();
    }
}
