import com.greensqa.datagen.generator.IdentityGenerator;
import com.greensqa.datagen.model.*;
import com.greensqa.datagen.patterns.singleton.UniquenessRegistry;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

/**
 * DemoCLI - version ejecutable del generador de datos (PARTE 1) para capturar
 * evidencia real en este entorno. Usa solo el JDK (sin SQLite/OpenCSV), pero
 * ejecuta exactamente la MISMA logica de negocio del proyecto Maven.
 *
 * Uso: java DemoCLI <cantidad> [--paralelo <hilos>]
 */
public class DemoCLI {

    static final String[] HEADER = {"tipo","nombre","apellido","edad","documento","ciudad","pais","idioma"};

    public static void main(String[] args) throws Exception {
        int cantidad = args.length > 0 ? Integer.parseInt(args[0]) : 50;
        boolean paralelo = Arrays.asList(args).contains("--paralelo");
        int hilos = 4;
        for (int i = 0; i < args.length - 1; i++)
            if (args[i].equals("--paralelo")) hilos = Integer.parseInt(args[i+1]);

        System.out.println("============================================================");
        System.out.println(" GENERADOR DE DATOS DE PRUEBA LATAM - GreenSQA (PARTE 1)");
        System.out.println("============================================================");
        System.out.println(" Cantidad solicitada : " + cantidad);
        System.out.println(" Modo                : " + (paralelo ? "PARALELO ("+hilos+" hilos)" : "SERIAL"));
        System.out.println("------------------------------------------------------------");

        UniquenessRegistry.getInstance().reset();
        IdentityGenerator gen = new IdentityGenerator();
        long ini = System.currentTimeMillis();
        List<Identity> datos;

        if (paralelo) {
            ConcurrentLinkedQueue<Identity> cola = new ConcurrentLinkedQueue<>();
            ExecutorService pool = Executors.newFixedThreadPool(hilos);
            int base = cantidad / hilos, resto = cantidad % hilos;
            List<Future<?>> fs = new ArrayList<>();
            for (int t = 0; t < hilos; t++) {
                final int n = base + (t < resto ? 1 : 0);
                fs.add(pool.submit(() -> {
                    RandomGenerator r = RandomGeneratorFactory.of("L64X128MixRandom").create();
                    for (int i = 0; i < n; i++) cola.add(gen.generar(r));
                }));
            }
            for (Future<?> f : fs) f.get();
            pool.shutdown();
            datos = new ArrayList<>(cola);
        } else {
            RandomGenerator r = RandomGeneratorFactory.of("L64X128MixRandom").create();
            datos = new ArrayList<>(cantidad);
            for (int i = 0; i < cantidad; i++) datos.add(gen.generar(r));
        }
        long dur = System.currentTimeMillis() - ini;

        // Exportar CSV
        Path out = Path.of("output/datos_evidencia.csv");
        Files.createDirectories(out.getParent());
        StringBuilder sb = new StringBuilder(String.join(",", HEADER)).append("\n");
        for (Identity id : datos) {
            sb.append(c(id.getTipoIdentidad())).append(',')
              .append(c(id.getNombre())).append(',')
              .append(c(id.getApellido())).append(',')
              .append(id.getEdad()).append(',')
              .append(c(id.getDocumento())).append(',')
              .append(c(id.getCiudad())).append(',')
              .append(c(id.getPais())).append(',')
              .append(c(id.getIdioma())).append('\n');
        }
        Files.writeString(out, sb.toString(), StandardCharsets.UTF_8);

        // Conteos
        long empresas = datos.stream().filter(d -> d instanceof CompanyIdentity).count();
        long menores = datos.stream().filter(d -> d instanceof PersonIdentity && d.getEdad()<18).count();
        long mayores = datos.size() - empresas - menores;

        System.out.printf(" Generados           : %d registros en %d ms%n", datos.size(), dur);
        System.out.printf("   - Empresas        : %d%n", empresas);
        System.out.printf("   - Menores de edad : %d%n", menores);
        System.out.printf("   - Mayores de edad : %d%n", mayores);
        System.out.println(" Archivo CSV         : " + out.toAbsolutePath());
        System.out.println("------------------------------------------------------------");
        System.out.println(" MUESTRA (primeros 8 registros):");
        System.out.println("------------------------------------------------------------");
        List<String> lineas = Files.readAllLines(out);
        for (int i = 0; i < Math.min(9, lineas.size()); i++) System.out.println("  " + lineas.get(i));
        System.out.println("============================================================");
        System.out.println(" Ejecucion finalizada correctamente.");
    }

    static String c(String v) {
        if (v == null) return "";
        return (v.contains(",")||v.contains("\"")) ? '"'+v.replace("\"","\"\"")+'"' : v;
    }
}
