package com.greensqa.datagen.patterns.strategy;

import java.util.random.RandomGenerator;

/**
 * Estrategia de documento para MAYORES de edad: número de dígitos > 8 y < 12,
 * es decir entre 9 y 11 dígitos. Para no chocar con menores (que arrancan en
 * 11.000.000 con 8 dígitos), aquí siempre se generan 9, 10 u 11 dígitos.
 */
public class AdultDocumentStrategy implements DocumentStrategy {
    @Override
    public String generar(RandomGenerator rnd) {
        int digitos = 9 + rnd.nextInt(3); // 9, 10 u 11
        StringBuilder sb = new StringBuilder();
        // primer dígito no nulo y distinto de 9 (9 lo reservamos a empresas)
        sb.append(1 + rnd.nextInt(8)); // 1..8
        for (int i = 1; i < digitos; i++) {
            sb.append(rnd.nextInt(10));
        }
        return sb.toString();
    }
}
