package com.greensqa.datagen.service;

import com.greensqa.datagen.generator.IdentityGenerator;
import com.greensqa.datagen.model.Identity;
import com.greensqa.datagen.persistence.IdentityRepository;
import com.greensqa.datagen.persistence.RunContext;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;
import java.util.ArrayList;

/**
 * Servicio que orquesta la generación de N identidades y su persistencia.
 *
 * <p>Soporta dos modos:
 * <ul>
 *   <li>Serial: simple y determinista.</li>
 *   <li>Paralelo (Bonus 8): reparte la carga entre varios hilos, cada uno con su
 *       propio {@link RandomGenerator}; la unicidad global se garantiza por el
 *       registro Singleton thread-safe.</li>
 * </ul></p>
 */
public class DataGenerationService {

    private final IdentityGenerator generator;
    private final IdentityRepository repository;

    public DataGenerationService(IdentityGenerator generator, IdentityRepository repository) {
        this.generator = generator;
        this.repository = repository;
    }

    /** Generación serial. */
    public List<Identity> generar(int cantidad, String runId) {
        RunContext.set(runId);
        try {
            RandomGenerator rnd = RandomGeneratorFactory.of("L64X128MixRandom").create();
            List<Identity> resultado = new ArrayList<>(cantidad);
            for (int i = 0; i < cantidad; i++) {
                resultado.add(generator.generar(rnd));
            }
            repository.guardarTodos(resultado);
            return resultado;
        } finally {
            RunContext.clear();
        }
    }

    /**
     * Generación en paralelo (Bonus 8).
     * @param cantidad total de registros
     * @param hilos número de hilos
     * @param runId identificador de la ejecución
     */
    public List<Identity> generarParalelo(int cantidad, int hilos, String runId) {
        ConcurrentLinkedQueue<Identity> cola = new ConcurrentLinkedQueue<>();
        ExecutorService pool = Executors.newFixedThreadPool(hilos);
        try {
            int base = cantidad / hilos;
            int resto = cantidad % hilos;
            List<Future<?>> futuros = new ArrayList<>();

            for (int t = 0; t < hilos; t++) {
                final int aGenerar = base + (t < resto ? 1 : 0);
                futuros.add(pool.submit(() -> {
                    RunContext.set(runId);
                    try {
                        RandomGenerator rnd = RandomGeneratorFactory
                                .of("L64X128MixRandom").create();
                        for (int i = 0; i < aGenerar; i++) {
                            cola.add(generator.generar(rnd));
                        }
                    } finally {
                        RunContext.clear();
                    }
                }));
            }
            for (Future<?> f : futuros) {
                f.get();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error en generación paralela", e);
        } finally {
            pool.shutdown();
            try {
                pool.awaitTermination(30, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        List<Identity> resultado = new ArrayList<>(cola);
        // Persistir con el contexto correcto
        RunContext.set(runId);
        try {
            repository.guardarTodos(resultado);
        } finally {
            RunContext.clear();
        }
        return resultado;
    }
}
