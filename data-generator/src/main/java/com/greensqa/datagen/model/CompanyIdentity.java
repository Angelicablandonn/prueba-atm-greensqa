package com.greensqa.datagen.model;

/**
 * Identidad de tipo empresa.
 *
 * <p>HERENCIA + POLIMORFISMO: reusa {@link Identity} pero impone sus propias
 * reglas: apellido en blanco y documento que inicia por "9".</p>
 */
public class CompanyIdentity extends Identity {

    @Override
    public String getTipoIdentidad() {
        return "EMPRESA";
    }

    @Override
    public boolean esValido() {
        if (getEdad() <= 10 || getEdad() >= 80) {
            return false;
        }
        if (getNombre() == null || getNombre().isBlank()) {
            return false;
        }
        // Empresa: el campo apellido DEBE ir en blanco
        if (getApellido() != null && !getApellido().isBlank()) {
            return false;
        }
        // Documento debe iniciar por "9"
        return getDocumento() != null && getDocumento().startsWith("9");
    }
}
