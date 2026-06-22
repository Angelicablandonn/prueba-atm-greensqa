package com.greensqa.datagen.model;

/**
 * Clase base abstracta que representa una identidad generada.
 *
 * <p>Pilar OOP - ABSTRACCIÓN: define el contrato común (qué es una identidad)
 * sin exponer cómo cada tipo concreto resuelve sus particularidades. Los métodos
 * {@link #getTipoIdentidad()} y {@link #esValido()} son abstractos: cada subtipo
 * decide su comportamiento.</p>
 *
 * <p>Pilar OOP - ENCAPSULAMIENTO: todos los atributos son privados y solo se
 * exponen mediante getters/setters controlados. El estado interno no se manipula
 * directamente desde fuera de la jerarquía.</p>
 */
public abstract class Identity {

    private long id;                 // PK interna en BD
    private String nombre;
    private String apellido;         // vacío para empresas
    private int edad;
    private String documento;
    private String ciudad;
    private String pais;
    private String idioma;

    protected Identity() {
        // Constructor protegido: solo las subclases / builders crean instancias.
    }

    // ---- Métodos abstractos (ABSTRACCIÓN) ----

    /** Cada subtipo identifica su naturaleza ("PERSONA" o "EMPRESA"). */
    public abstract String getTipoIdentidad();

    /**
     * Validación polimórfica de reglas de negocio propias de cada tipo.
     * (POLIMORFISMO: el mismo mensaje produce comportamientos distintos.)
     */
    public abstract boolean esValido();

    // ---- Encapsulamiento: acceso controlado al estado ----

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }

    /** Llave lógica usada para evitar repetición de nombre + apellido. */
    public String claveNombreCompleto() {
        return (nombre + "|" + (apellido == null ? "" : apellido)).toLowerCase();
    }

    @Override
    public String toString() {
        return "%s{nombre=%s, apellido=%s, edad=%d, doc=%s, ciudad=%s, pais=%s, idioma=%s}"
                .formatted(getTipoIdentidad(), nombre, apellido, edad, documento, ciudad, pais, idioma);
    }
}
