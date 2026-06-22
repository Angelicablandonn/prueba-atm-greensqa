package com.greensqa.datagen.patterns.singleton;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Patrón de diseño SINGLETON.
 *
 * <p>Garantiza una única instancia compartida que mantiene el registro global de
 * combinaciones nombre+apellido y documentos ya usados. Esto permite cumplir las
 * reglas "la combinación nombre+apellido no se puede repetir" y "los documentos no
 * se pueden repetir" incluso cuando la generación corre en paralelo (Bonus 8).</p>
 *
 * <p>Se usa el idiom <i>initialization-on-demand holder</i>, que es thread-safe y
 * perezoso sin necesidad de sincronización explícita. Los conjuntos internos son
 * {@link ConcurrentHashMap}-backed para soportar concurrencia.</p>
 */
public final class UniquenessRegistry {

    private final Set<String> nombresCompletosUsados = ConcurrentHashMap.newKeySet();
    private final Set<String> documentosUsados = ConcurrentHashMap.newKeySet();

    private UniquenessRegistry() { }

    private static final class Holder {
        private static final UniquenessRegistry INSTANCE = new UniquenessRegistry();
    }

    public static UniquenessRegistry getInstance() {
        return Holder.INSTANCE;
    }

    /** Reserva la combinación nombre+apellido si está libre. Atómico. */
    public boolean reservarNombreCompleto(String clave) {
        return nombresCompletosUsados.add(clave);
    }

    /** Reserva el documento si está libre. Atómico. */
    public boolean reservarDocumento(String documento) {
        return documentosUsados.add(documento);
    }

    public boolean documentoExiste(String documento) {
        return documentosUsados.contains(documento);
    }

    /** Limpia el estado (útil para pruebas y para re-ejecuciones). */
    public void reset() {
        nombresCompletosUsados.clear();
        documentosUsados.clear();
    }

    public int totalNombres() { return nombresCompletosUsados.size(); }
    public int totalDocumentos() { return documentosUsados.size(); }
}
