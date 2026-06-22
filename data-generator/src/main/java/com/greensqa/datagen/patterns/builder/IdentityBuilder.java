package com.greensqa.datagen.patterns.builder;

import com.greensqa.datagen.model.CompanyIdentity;
import com.greensqa.datagen.model.Identity;
import com.greensqa.datagen.model.PersonIdentity;

/**
 * Patrón de diseño BUILDER.
 *
 * <p>Construye objetos {@link Identity} paso a paso, ocultando la complejidad de
 * decidir la subclase concreta (persona vs empresa) y de fijar muchos atributos.
 * Devuelve un objeto inmutable-en-la-práctica una vez construido.</p>
 */
public class IdentityBuilder {

    private boolean esEmpresa = false;
    private String nombre;
    private String apellido;
    private int edad;
    private String documento;
    private String ciudad;
    private String pais;
    private String idioma;

    public IdentityBuilder empresa(boolean esEmpresa) {
        this.esEmpresa = esEmpresa;
        return this;
    }

    public IdentityBuilder nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public IdentityBuilder apellido(String apellido) {
        this.apellido = apellido;
        return this;
    }

    public IdentityBuilder edad(int edad) {
        this.edad = edad;
        return this;
    }

    public IdentityBuilder documento(String documento) {
        this.documento = documento;
        return this;
    }

    public IdentityBuilder ciudad(String ciudad) {
        this.ciudad = ciudad;
        return this;
    }

    public IdentityBuilder pais(String pais) {
        this.pais = pais;
        return this;
    }

    public IdentityBuilder idioma(String idioma) {
        this.idioma = idioma;
        return this;
    }

    public Identity build() {
        Identity identity = esEmpresa ? new CompanyIdentity() : new PersonIdentity();
        identity.setNombre(nombre);
        // Empresa: apellido siempre en blanco
        identity.setApellido(esEmpresa ? "" : apellido);
        identity.setEdad(edad);
        identity.setDocumento(documento);
        identity.setCiudad(ciudad);
        identity.setPais(pais);
        identity.setIdioma(idioma);
        return identity;
    }
}
