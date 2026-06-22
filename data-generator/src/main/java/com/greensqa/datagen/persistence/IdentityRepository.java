package com.greensqa.datagen.persistence;

import com.greensqa.datagen.model.Identity;

import java.util.List;

/**
 * Contrato de persistencia de identidades.
 *
 * <p>Principio SOLID - DIP (Dependency Inversion): los servicios de alto nivel
 * dependen de esta abstracción y no de una implementación concreta de base de datos.
 * Permite cambiar SQLite por otra BD sin tocar la lógica de generación.</p>
 *
 * <p>Principio SOLID - ISP (Interface Segregation): la interfaz expone solo
 * operaciones cohesivas de persistencia/gestión de datos.</p>
 *
 * <p>Bonus 3: incluye métodos para gestionar (consultar, contar, borrar) los datos
 * almacenados en ejecuciones pasadas.</p>
 */
public interface IdentityRepository extends AutoCloseable {

    void inicializar();

    void guardar(Identity identity);

    void guardarTodos(List<Identity> identidades);

    List<Identity> listarTodos();

    /** Gestión: identidades de una ejecución (batch) específica. */
    List<Identity> listarPorEjecucion(String runId);

    long contar();

    /** Gestión: elimina los registros de una ejecución pasada. */
    int eliminarPorEjecucion(String runId);

    /** Gestión: limpia toda la tabla. */
    void limpiarTodo();

    @Override
    void close();
}
