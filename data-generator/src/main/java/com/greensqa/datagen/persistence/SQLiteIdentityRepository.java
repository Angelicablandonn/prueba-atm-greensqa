package com.greensqa.datagen.persistence;

import com.greensqa.datagen.model.CompanyIdentity;
import com.greensqa.datagen.model.Identity;
import com.greensqa.datagen.model.PersonIdentity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de {@link IdentityRepository} sobre SQLite.
 *
 * <p>Cumple LSP (Liskov): puede sustituir a la abstracción sin romper el
 * comportamiento esperado por los clientes.</p>
 */
public class SQLiteIdentityRepository implements IdentityRepository {

    private final String url;
    private Connection connection;

    public SQLiteIdentityRepository(String dbPath) {
        this.url = "jdbc:sqlite:" + dbPath;
    }

    private Connection conn() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url);
                try (Statement s = connection.createStatement()) {
                    s.execute("PRAGMA journal_mode=WAL;");
                    s.execute("PRAGMA busy_timeout=5000;");
                }
            }
            return connection;
        } catch (SQLException e) {
            throw new PersistenceException("No se pudo abrir la conexión", e);
        }
    }

    @Override
    public synchronized void inicializar() {
        String ddl = """
                CREATE TABLE IF NOT EXISTS identidades (
                    id        INTEGER PRIMARY KEY AUTOINCREMENT,
                    run_id    TEXT    NOT NULL,
                    tipo      TEXT    NOT NULL,
                    nombre    TEXT    NOT NULL,
                    apellido  TEXT,
                    edad      INTEGER NOT NULL,
                    documento TEXT    NOT NULL UNIQUE,
                    ciudad    TEXT,
                    pais      TEXT,
                    idioma    TEXT,
                    creado_en TEXT    DEFAULT (datetime('now'))
                );
                """;
        try (Statement s = conn().createStatement()) {
            s.execute(ddl);
        } catch (SQLException e) {
            throw new PersistenceException("Error inicializando esquema", e);
        }
    }

    @Override
    public void guardar(Identity identity) {
        guardarTodos(List.of(identity));
    }

    @Override
    public synchronized void guardarTodos(List<Identity> identidades) {
        String sql = """
                INSERT INTO identidades
                    (run_id, tipo, nombre, apellido, edad, documento, ciudad, pais, idioma)
                VALUES (?,?,?,?,?,?,?,?,?)
                """;
        Connection c = conn();
        try {
            c.setAutoCommit(false);
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                for (Identity id : identidades) {
                    ps.setString(1, runIdOf(id));
                    ps.setString(2, id.getTipoIdentidad());
                    ps.setString(3, id.getNombre());
                    ps.setString(4, id.getApellido());
                    ps.setInt(5, id.getEdad());
                    ps.setString(6, id.getDocumento());
                    ps.setString(7, id.getCiudad());
                    ps.setString(8, id.getPais());
                    ps.setString(9, id.getIdioma());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            c.commit();
        } catch (SQLException e) {
            try { c.rollback(); } catch (SQLException ignored) { }
            throw new PersistenceException("Error guardando identidades", e);
        } finally {
            try { c.setAutoCommit(true); } catch (SQLException ignored) { }
        }
    }

    // run_id se transporta como atributo transitorio vía documento->ThreadLocal alterno;
    // para simplicidad lo tomamos de un campo estático contextual.
    private String runIdOf(Identity id) {
        String r = RunContext.current();
        return r == null ? "default" : r;
    }

    @Override
    public List<Identity> listarTodos() {
        return query("SELECT * FROM identidades ORDER BY id", null);
    }

    @Override
    public List<Identity> listarPorEjecucion(String runId) {
        return query("SELECT * FROM identidades WHERE run_id = ? ORDER BY id", runId);
    }

    private List<Identity> query(String sql, String runId) {
        List<Identity> out = new ArrayList<>();
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            if (runId != null) {
                ps.setString(1, runId);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error consultando identidades", e);
        }
        return out;
    }

    private Identity mapRow(ResultSet rs) throws SQLException {
        String tipo = rs.getString("tipo");
        Identity id = "EMPRESA".equals(tipo) ? new CompanyIdentity() : new PersonIdentity();
        id.setId(rs.getLong("id"));
        id.setNombre(rs.getString("nombre"));
        id.setApellido(rs.getString("apellido"));
        id.setEdad(rs.getInt("edad"));
        id.setDocumento(rs.getString("documento"));
        id.setCiudad(rs.getString("ciudad"));
        id.setPais(rs.getString("pais"));
        id.setIdioma(rs.getString("idioma"));
        return id;
    }

    @Override
    public long contar() {
        try (Statement s = conn().createStatement();
             ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM identidades")) {
            return rs.next() ? rs.getLong(1) : 0L;
        } catch (SQLException e) {
            throw new PersistenceException("Error contando", e);
        }
    }

    @Override
    public int eliminarPorEjecucion(String runId) {
        try (PreparedStatement ps = conn().prepareStatement(
                "DELETE FROM identidades WHERE run_id = ?")) {
            ps.setString(1, runId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Error eliminando ejecución", e);
        }
    }

    @Override
    public void limpiarTodo() {
        try (Statement s = conn().createStatement()) {
            s.executeUpdate("DELETE FROM identidades");
        } catch (SQLException e) {
            throw new PersistenceException("Error limpiando tabla", e);
        }
    }

    @Override
    public synchronized void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error cerrando conexión", e);
        }
    }
}
