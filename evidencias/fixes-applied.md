# Correcciones aplicadas — Prueba técnica GreenSQA

**Fecha:** 2026-06-22

## 1. Cucumber — paso duplicado

| Campo | Detalle |
|-------|---------|
| **Archivo** | `BusquedaVuelosStepDefinitions.java` |
| **Problema** | `@Y` duplicado en `seleccionaTipoViaje` → `DuplicateStepDefinitionException` |
| **Corrección** | Eliminada anotación duplicada |
| **Evidencia** | Build compila y Cucumber inicia escenarios |

## 2. Serenity 4.x — API obsoleta

| Campo | Detalle |
|-------|---------|
| **Archivos** | `MensajeDeValidacion.java`, `ResultadosDeVuelos.java` |
| **Problema** | `TheTarget.theCountOf()` no existe en Serenity 4.x |
| **Corrección** | Uso de `HomePage.*.resolveFor(actor).isPresent()` |

## 3. Runner Cucumber JUnit Platform

| Campo | Detalle |
|-------|---------|
| **Archivos** | `pom.xml`, `CucumberTestSuite.java` |
| **Problema** | Falta engine Cucumber; glue `hooks` inexistente |
| **Corrección** | Dependencia `cucumber-junit-platform-engine`; glue solo en `stepdefinitions` |

## 4. Selectores y flujo UI

| Campo | Detalle |
|-------|---------|
| **Archivos** | `HomePage.java`, `SeleccionarTipoViaje.java`, `AbrirHomeLatam.java` |
| **Problema** | Tabs de viaje no encontrados; cookies OneTrust; origen no visible al cargar |
| **Corrección** | Selectores ampliados; skip clic en "Ida y vuelta"; aceptar cookies; tab vuelos fallback; detección Access Denied |

## 5. Configuración WebDriver

| Campo | Detalle |
|-------|---------|
| **Archivo** | `serenity.conf` |
| **Problema** | Headless bloqueado por Akamai; timeout de carga; `pageLoadStrategy` mal ubicado en `goog:chromeOptions` |
| **Corrección** | Args anti-detección (`AutomationControlled`, `excludeSwitches`); `pageLoadStrategy=eager` a nivel capabilities; `pageLoadTimeout=60s`; timeout implícito 15s |

## 6. Ejecución Maven

| Campo | Detalle |
|-------|---------|
| **Archivo** | `pom.xml` (failsafe) |
| **Problema** | Paralelismo (`threadCount=2`) genera múltiples sesiones contra anti-bot |
| **Corrección** | `<parallel>none</parallel>` — escenarios secuenciales |

## 7. Compilación JDK

| Campo | Detalle |
|-------|---------|
| **Problema** | Entorno con JDK 17, no JDK 21 |
| **Corrección** | `mvn ... -Dmaven.compiler.release=17` en todas las ejecuciones Maven |

## Resultado post-correcciones

| Modo | Tests | Resultado |
|------|-------|-----------|
| Headless (`-Dheadless.mode=true`) | 3 escenarios | **0 pass, 3 errors** — `IllegalStateException`: Access Denied Akamai (~983 s) |
| Headed (`-Dheadless.mode=false`) | 3 escenarios | **0 pass, 1 failure, 2 errors** — timeout carga / origen no visible (~910 s) |

Evidencias: `evidencias/automation-verify.txt`, `evidencias/automation-verify-headed.txt`

## Bloqueo no resuelto en código

**Protección anti-bot LATAM (Akamai):** impide acceso estable automatizado desde este entorno, especialmente en headless. Requiere red/navegador no bloqueado o herramientas especializadas (no incluidas en el alcance de la POC).
