# LATAM QA POC — Prueba GreenSQA (Shift Right)

Solución a la prueba técnica de automatización de GreenSQA para el cliente LATAM.
Contiene dos módulos independientes:

| Módulo | Carpeta | Tecnología | Propósito |
|--------|---------|------------|-----------|
| Generador de datos | `data-generator/` | Java 21 + Maven + SQLite | PARTE 1: genera datos de prueba ficticios |
| Automatización UI | `automation/` | Java 17 + Serenity + Cucumber | PARTE 2: automatiza la búsqueda de vuelos |

El CSV producido por el primer módulo alimenta como **dato de entrada** al segundo,
tal como pide el enunciado.

---

## Requisitos

- **JDK 21** (el generador usa características de Java 21; la automatización compila con 17+).
- **Maven 3.8+**.
- **Google Chrome** instalado (la automatización descarga el driver automáticamente).
- Conexión a internet con acceso a Maven Central para descargar dependencias.

```bash
java -version    # 21.x
mvn -version     # 3.8+
```

---

## PARTE 1 — Generador de datos de prueba

### Compilar y probar

```bash
cd data-generator
mvn clean test          # ejecuta las pruebas unitarias de reglas de negocio
mvn clean package       # genera target/data-generator.jar (ejecutable)
```

### Ejecutar

```bash
# Generar 100 registros (serial), exportar CSV y guardar en SQLite
java -jar target/data-generator.jar --cantidad 100

# Generar 1000 registros en paralelo con 4 hilos
java -jar target/data-generator.jar --cantidad 1000 --paralelo --hilos 4

# Especificar salida y base de datos
java -jar target/data-generator.jar --cantidad 200 \
     --salida output/datos_actual.csv --db data/identidades.db

# Enviar el CSV por correo (requiere variables SMTP, ver abajo)
java -jar target/data-generator.jar --cantidad 50 --email

# Gestión de datos almacenados (ejecuciones pasadas)
java -jar target/data-generator.jar --listar     # total en BD
java -jar target/data-generator.jar --limpiar    # borra todo
```

### Configuración de correo (Bonus 7)

El envío usa SMTP con STARTTLS. Configure variables de entorno (no hay credenciales en el código):

```bash
export SMTP_HOST=smtp.gmail.com
export SMTP_PORT=587
export SMTP_USER=tu_correo@gmail.com
export SMTP_PASSWORD=tu_app_password
export MAIL_TO=destinatario@dominio.com
java -jar target/data-generator.jar --cantidad 50 --email
```

### Qué demuestra el desarrollo

- **4 pilares OOP**: abstracción (`Identity`), encapsulamiento, herencia (`PersonIdentity`, `CompanyIdentity`), polimorfismo (`esValido()`).
- **4 patrones de diseño**: Singleton, Strategy, Factory Method, Builder.
- **5 principios SOLID** (bonus completo): SRP, OCP, LSP, ISP, DIP.
- **Persistencia SQLite** con métodos de gestión de ejecuciones pasadas (bonus 3).
- **CSV separado por comas** (requisito 6).
- **Cantidad parametrizable** (requisito 5).
- **Envío por correo** (bonus 7) y **ejecución en paralelo** (bonus 8).

Detalle de la arquitectura en [`data-generator/README.md`](data-generator/README.md).

---

## PARTE 2 — Automatización de búsqueda de vuelos

### Casos de prueba diseñados (Módulo Diseño)

Ver [`docs/casos-de-prueba.md`](docs/casos-de-prueba.md). En resumen:

1. **CP01** — Búsqueda ida y vuelta, 1 adulto (camino feliz).
2. **CP02** — Búsqueda solo ida usando un pasajero leído del CSV de la PARTE 1.
3. **CP03** — Validación negativa: origen = destino.

### Ejecutar

```bash
cd automation

# Por defecto lee el CSV en ../data-generator/output/datos_actual.csv
mvn clean verify

# Indicar otro CSV de datos de prueba
mvn clean verify -Dtestdata.csv=/ruta/al/datos.csv

# Ejecutar con navegador visible (no headless)
mvn clean verify -Dheadless.mode=false

# Cambiar de navegador
mvn clean verify -Dwebdriver.driver=firefox
```

El reporte HTML de Serenity queda en `automation/target/site/serenity/index.html`.

### Arquitectura

Framework basado en el patrón **Screenplay** de Serenity:
`features → step definitions → tasks/questions → page objects (Targets)`.

Detalle en [`automation/README.md`](automation/README.md).

---

## Estructura del repositorio

```
latam-qa-poc/
├── data-generator/        # PARTE 1
│   ├── src/main/java/...   # modelo, patrones, generador, persistencia, export, email
│   ├── src/test/java/...   # pruebas unitarias
│   └── pom.xml
├── automation/            # PARTE 2
│   ├── src/test/java/...   # tasks, questions, ui, stepdefs, runner
│   ├── src/test/resources/features/
│   └── pom.xml
├── docs/
│   └── casos-de-prueba.md
└── README.md
```

---

## Notas

- Ambos módulos están pensados para publicarse en un repositorio público (nota final del enunciado).
- Los selectores de la automatización son representativos y están centralizados en `HomePage`; LATAM usa un SPA dinámico, por lo que ante cambios del sitio solo se ajusta ese archivo.
