package com.greensqa.automation.model;

/** Representa una fila del CSV de datos de prueba generado en PARTE 1. */
public class TestPerson {
    private final String tipo, nombre, apellido, ciudad, pais, idioma, documento;
    private final int edad;

    public TestPerson(String tipo, String nombre, String apellido, int edad,
                      String documento, String ciudad, String pais, String idioma) {
        this.tipo = tipo; this.nombre = nombre; this.apellido = apellido;
        this.edad = edad; this.documento = documento; this.ciudad = ciudad;
        this.pais = pais; this.idioma = idioma;
    }
    public String getTipo() { return tipo; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public int getEdad() { return edad; }
    public String getDocumento() { return documento; }
    public String getCiudad() { return ciudad; }
    public String getPais() { return pais; }
    public String getIdioma() { return idioma; }
    public boolean esMenor() { return edad < 18; }
    public boolean esEmpresa() { return "EMPRESA".equalsIgnoreCase(tipo); }

    @Override public String toString() {
        return nombre + " " + apellido + " (" + edad + ", " + pais + ")";
    }
}
