package com.greensqa.datagen.model;

/**
 * Identidad de tipo persona natural.
 *
 * <p>Pilar OOP - HERENCIA: extiende {@link Identity} reutilizando todo el estado
 * y comportamiento común.</p>
 *
 * <p>Pilar OOP - POLIMORFISMO: sobrescribe {@link #esValido()} y
 * {@link #getTipoIdentidad()} con las reglas específicas de una persona
 * (rango de edad 11-79, documento según mayoría/minoría de edad, etc.).</p>
 */
public class PersonIdentity extends Identity {

    @Override
    public String getTipoIdentidad() {
        return "PERSONA";
    }

    public boolean esMenorDeEdad() {
        return getEdad() < 18;
    }

    @Override
    public boolean esValido() {
        // Edad: mayor a 10 y menor de 80 -> rango [11, 79]
        if (getEdad() <= 10 || getEdad() >= 80) {
            return false;
        }
        if (getNombre() == null || getNombre().isBlank()) {
            return false;
        }
        // Una persona SÍ debe tener apellido
        if (getApellido() == null || getApellido().isBlank()) {
            return false;
        }
        if (getDocumento() == null || getDocumento().isBlank()) {
            return false;
        }
        long doc;
        try {
            doc = Long.parseLong(getDocumento());
        } catch (NumberFormatException e) {
            return false;
        }
        if (esMenorDeEdad()) {
            // Menor de edad: documento generado a partir de 11000000
            return doc >= 11_000_000L;
        }
        // Mayor de edad: número de dígitos > 8 y < 12  -> [9, 11] dígitos
        int digitos = getDocumento().length();
        return digitos > 8 && digitos < 12;
    }
}
