package com.greensqa.datagen.persistence;

/**
 * Contexto de ejecución por hilo. Transporta el run_id (identificador de batch)
 * sin acoplarlo al modelo de dominio. Compatible con ejecución en paralelo (Bonus 8).
 */
public final class RunContext {
    private static final ThreadLocal<String> RUN_ID = new ThreadLocal<>();
    private RunContext() { }
    public static void set(String runId) { RUN_ID.set(runId); }
    public static String current() { return RUN_ID.get(); }
    public static void clear() { RUN_ID.remove(); }
}
