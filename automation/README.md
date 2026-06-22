# Automatización de búsqueda de vuelos LATAM (PARTE 2)

Framework de automatización UI con **Serenity BDD + Cucumber**, patrón **Screenplay**.
Automatiza los 3 casos de prueba diseñados y consume como dato de entrada el CSV
generado en la PARTE 1.

## Arquitectura (Screenplay)

```
.feature (Gherkin, español)
   ↓
StepDefinitions            ← traduce pasos a interacciones del actor
   ↓
Tasks  /  Questions        ← acciones de negocio  /  verificaciones
   ↓
Page Objects (Targets)     ← locators centralizados (HomePage)
   ↓
Serenity WebDriver
```

### Componentes

| Capa | Clases |
|------|--------|
| Features | `resources/features/busqueda_vuelos.feature` |
| Steps | `stepdefinitions/BusquedaVuelosStepDefinitions` |
| Tasks | `AbrirHomeLatam`, `SeleccionarTipoViaje`, `IngresarRuta`, `SeleccionarFechas`, `ConfigurarPasajeros`, `BuscarVuelos` |
| Questions | `ResultadosDeVuelos`, `MensajeDeValidacion` |
| UI | `ui/HomePage` (Targets) |
| Datos | `utils/TestDataProvider` (lee el CSV de PARTE 1), `model/TestPerson` |
| Runner | `runners/CucumberTestSuite` (JUnit 5 platform suite) |
| Config | `resources/serenity.conf` |

## Integración con PARTE 1

`TestDataProvider.cargarPorDefecto()` lee el CSV indicado por la propiedad
`-Dtestdata.csv` o, por defecto, `../data-generator/output/datos_actual.csv`.
El caso CP02 toma un pasajero del CSV y decide menor/adulto según su edad.

## Por qué Screenplay

- **SRP** a nivel de UI: cada Task/Question tiene una responsabilidad.
- **Reutilización**: las tareas se componen entre escenarios.
- **Mantenibilidad**: los locators viven solo en `HomePage`.
- **Legibilidad**: los escenarios se leen como lenguaje de negocio.

## Ejecución

```bash
mvn clean verify                                  # headless, CSV por defecto
mvn clean verify -Dheadless.mode=false            # navegador visible
mvn clean verify -Dtestdata.csv=/ruta/datos.csv   # otro CSV
mvn clean verify -Dwebdriver.driver=firefox       # otro navegador
```

Reporte: `target/site/serenity/index.html`.

## Ejecución en paralelo

`maven-failsafe-plugin` está configurado con `parallel=methods` y `threadCount=2`,
y el reporter `SerenityReporterParallel` para agregar correctamente los resultados.

## Notas sobre los selectores

LATAM es un SPA con DOM dinámico. Los `Target` de `HomePage` usan selectores
tolerantes (CSS + XPath con alternativas). Ante cambios del sitio, se ajustan
únicamente en ese archivo sin tocar tasks ni steps.
