# Generador de datos de prueba LATAM (PARTE 1)

Genera datos de prueba ficticios cumpliendo las reglas de negocio del enunciado,
los persiste en SQLite y los exporta a CSV. Soporta envío por correo y ejecución paralela.

## Reglas de negocio implementadas

- **Nombre** y **Apellido** (apellido en blanco para empresas).
- La combinación **nombre + apellido no se repite**.
- **Edad** entre 11 y 79 (mayor a 10, menor de 80).
- **Documento**:
  - Empresa → inicia por `9`.
  - Menor de edad → a partir de `11000000`.
  - Mayor de edad → entre 9 y 11 dígitos (> 8 y < 12).
  - Los documentos **no se repiten**.
- **Ciudad** y **País** de residencia.
- **Idioma**: si el país ≠ Colombia, el idioma ≠ Español.

## Pilares de la POO

| Pilar | Dónde |
|-------|-------|
| Abstracción | `model/Identity` (clase abstracta + métodos abstractos) |
| Encapsulamiento | atributos privados con getters/setters en `Identity` |
| Herencia | `PersonIdentity` y `CompanyIdentity` extienden `Identity` |
| Polimorfismo | `esValido()` y `getTipoIdentidad()` sobrescritos por tipo |

## Patrones de diseño

| Patrón | Clase | Rol |
|--------|-------|-----|
| Singleton | `patterns/singleton/UniquenessRegistry` | registro global thread-safe de unicidad |
| Strategy | `patterns/strategy/*DocumentStrategy` | algoritmo de documento por tipo/edad |
| Factory Method | `patterns/factory/DocumentStrategyFactory` | crea la estrategia adecuada |
| Builder | `patterns/builder/IdentityBuilder` | construye la identidad paso a paso |

## Principios SOLID (los 5 — bonus completo)

| Principio | Evidencia |
|-----------|-----------|
| SRP | `IdentityGenerator` (genera), `CsvExporter` (exporta), `EmailSender` (envía), repos (persisten): una responsabilidad cada uno. |
| OCP | nuevas reglas de documento = nueva `DocumentStrategy`, sin tocar el generador. |
| LSP | `SQLiteIdentityRepository` sustituye a `IdentityRepository` sin romper clientes. |
| ISP | `IdentityRepository` expone solo operaciones cohesivas de persistencia/gestión. |
| DIP | el servicio depende de la abstracción `IdentityRepository`, no de SQLite. |

## Persistencia y gestión (Bonus 3)

Tabla `identidades` en SQLite con `run_id` por ejecución. Métodos de gestión:
`listarTodos`, `listarPorEjecucion`, `contar`, `eliminarPorEjecucion`, `limpiarTodo`.
Desde la CLI: `--listar` y `--limpiar`.

## Concurrencia (Bonus 8)

`DataGenerationService.generarParalelo()` reparte la carga entre hilos; cada hilo usa
su propio `RandomGenerator` y la unicidad global se garantiza con el Singleton
(`ConcurrentHashMap`-backed). Validado: 0 duplicados tras serial + paralelo.

## Estructura

```
src/main/java/com/greensqa/datagen/
├── model/           Identity, PersonIdentity, CompanyIdentity
├── patterns/
│   ├── singleton/   UniquenessRegistry
│   ├── strategy/    DocumentStrategy + 3 implementaciones
│   ├── factory/     DocumentStrategyFactory
│   └── builder/     IdentityBuilder
├── generator/       IdentityGenerator
├── persistence/     IdentityRepository, SQLiteIdentityRepository, RunContext
├── export/          CsvExporter
├── email/           EmailSender
├── service/         DataGenerationService
├── util/            Catalog
└── Main.java        CLI
```

## Comandos

Ver el README raíz del repositorio para todos los comandos de ejecución.
