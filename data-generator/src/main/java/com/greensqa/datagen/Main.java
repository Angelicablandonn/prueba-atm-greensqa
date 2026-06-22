package com.greensqa.datagen;

import com.greensqa.datagen.email.EmailSender;
import com.greensqa.datagen.export.CsvExporter;
import com.greensqa.datagen.generator.IdentityGenerator;
import com.greensqa.datagen.model.Identity;
import com.greensqa.datagen.persistence.IdentityRepository;
import com.greensqa.datagen.persistence.SQLiteIdentityRepository;
import com.greensqa.datagen.service.DataGenerationService;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * Punto de entrada CLI del generador de datos de prueba LATAM.
 *
 * <p>Uso:</p>
 * <pre>
 *   java -jar data-generator.jar --cantidad 100 [opciones]
 *
 *   Opciones:
 *     --cantidad N        Cantidad de registros a generar (requisito 5). Default 50.
 *     --salida RUTA       Ruta del CSV de salida. Default output/datos_&lt;timestamp&gt;.csv
 *     --db RUTA           Ruta de la base de datos SQLite. Default data/identidades.db
 *     --paralelo          Activa generación en paralelo (Bonus 8).
 *     --hilos N           Número de hilos si --paralelo. Default = nº de procesadores.
 *     --email             Envía el CSV por correo (Bonus 7, requiere variables SMTP).
 *     --listar            Muestra el total almacenado y termina (Bonus 3 - gestión).
 *     --limpiar           Borra todos los registros almacenados y termina (gestión).
 * </pre>
 */
public class Main {

    public static void main(String[] args) {
        Args a = Args.parse(args);

        Path dbPath = Path.of(a.db);
        dbPath.toAbsolutePath().getParent().toFile().mkdirs();

        try (IdentityRepository repo = new SQLiteIdentityRepository(a.db)) {
            repo.inicializar();

            // ---- Modos de gestión (Bonus 3) ----
            if (a.listar) {
                System.out.println("Total de registros almacenados: " + repo.contar());
                return;
            }
            if (a.limpiar) {
                repo.limpiarTodo();
                System.out.println("Se eliminaron todos los registros almacenados.");
                return;
            }

            // ---- Generación ----
            String runId = "run-" + UUID.randomUUID().toString().substring(0, 8);
            IdentityGenerator generator = new IdentityGenerator();
            DataGenerationService service = new DataGenerationService(generator, repo);

            long ini = System.currentTimeMillis();
            List<Identity> datos;
            if (a.paralelo) {
                System.out.printf("Generando %d registros en paralelo (%d hilos)...%n",
                        a.cantidad, a.hilos);
                datos = service.generarParalelo(a.cantidad, a.hilos, runId);
            } else {
                System.out.printf("Generando %d registros (serial)...%n", a.cantidad);
                datos = service.generar(a.cantidad, runId);
            }
            long dur = System.currentTimeMillis() - ini;

            // ---- Exportación CSV (requisito 6) ----
            Path salida = Path.of(a.salida != null ? a.salida : defaultCsvPath());
            new CsvExporter().exportar(datos, salida);

            System.out.printf("OK -> %d registros generados en %d ms (run=%s)%n",
                    datos.size(), dur, runId);
            System.out.println("CSV: " + salida.toAbsolutePath());
            System.out.println("Total acumulado en BD: " + repo.contar());

            // ---- Envío por correo (Bonus 7) ----
            if (a.email) {
                EmailSender sender = new EmailSender();
                if (sender.estaConfigurado()) {
                    sender.enviarCsv(salida, datos.size());
                    System.out.println("Correo enviado correctamente.");
                } else {
                    System.out.println("AVISO: --email solicitado pero SMTP no configurado. "
                            + "Defina SMTP_HOST, SMTP_USER, SMTP_PASSWORD y MAIL_TO.");
                }
            }
        }
    }

    private static String defaultCsvPath() {
        String ts = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return "output/datos_" + ts + ".csv";
    }

    /** Pequeño parser de argumentos de línea de comandos. */
    private static final class Args {
        int cantidad = 50;
        String salida = null;
        String db = "data/identidades.db";
        boolean paralelo = false;
        int hilos = Runtime.getRuntime().availableProcessors();
        boolean email = false;
        boolean listar = false;
        boolean limpiar = false;

        static Args parse(String[] argv) {
            Args a = new Args();
            for (int i = 0; i < argv.length; i++) {
                switch (argv[i]) {
                    case "--cantidad" -> a.cantidad = Integer.parseInt(argv[++i]);
                    case "--salida" -> a.salida = argv[++i];
                    case "--db" -> a.db = argv[++i];
                    case "--paralelo" -> a.paralelo = true;
                    case "--hilos" -> a.hilos = Integer.parseInt(argv[++i]);
                    case "--email" -> a.email = true;
                    case "--listar" -> a.listar = true;
                    case "--limpiar" -> a.limpiar = true;
                    case "--help", "-h" -> { printHelp(); System.exit(0); }
                    default -> System.err.println("Opción desconocida: " + argv[i]);
                }
            }
            if (a.cantidad <= 0 && !a.listar && !a.limpiar) {
                throw new IllegalArgumentException("--cantidad debe ser > 0");
            }
            return a;
        }

        static void printHelp() {
            System.out.println("""
                    Generador de datos de prueba LATAM (GreenSQA)
                    Uso: java -jar data-generator.jar [opciones]
                      --cantidad N    Registros a generar (default 50)
                      --salida RUTA   CSV de salida
                      --db RUTA       BD SQLite (default data/identidades.db)
                      --paralelo      Generación en paralelo
                      --hilos N       Hilos (con --paralelo)
                      --email         Enviar CSV por correo (vars SMTP)
                      --listar        Mostrar total almacenado
                      --limpiar       Borrar todos los registros
                    """);
        }
    }
}
