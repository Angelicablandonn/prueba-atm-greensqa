package com.greensqa.datagen.persistence;

/** Excepción de dominio para errores de persistencia. */
public class PersistenceException extends RuntimeException {
    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
