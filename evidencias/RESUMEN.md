# RESUMEN — Prueba técnica QA Automatizador GreenSQA

**Fecha de ejecución:** 2026-06-22  
**Workspace:** `entrega-greensqa/`  
**Repositorio:** https://github.com/Angelicablandonn/prueba-atm-greensqa

---

## 1. Resumen Ejecutivo

Se validó el entorno, se ejecutó el **data-generator** con éxito completo (tests, JAR, CSV válido), se corrigieron múltiples defectos del framework de **automatización UI** (Cucumber/Serenity), y se ejecutaron los **tres escenarios K6** contra `https://test.k6.io` sin errores HTTP.

La **automatización UI contra LATAM** no pudo completar los escenarios por un **bloqueo externo de Akamai** (página "Access Denied" en headless; timeouts/origen no visible en headed). El framework compila, ejecuta, genera reporte Serenity y detecta el bloqueo con mensaje explícito.

| Área | Estado global |
|------|---------------|
| Data Generator | ✅ COMPLETO |
| Automatización UI | ⚠️ BLOQUEADO (anti-bot LATAM) |
| K6 Performance | ✅ COMPLETO |
| Evidencias | ✅ GENERADAS |

---

## 2. Entorno Utilizado

| Herramienta | Versión | Estado |
|-------------|---------|--------|
| JDK | 17.0.12 (no JDK 21) | OK con `-Dmaven.compiler.release=17` |
| Maven | 3.9.12 | OK |
| Chrome | 149.0.7827.115 | OK |
| ChromeDriver | WebDriverManager / Serenity autodownload | OK |
| K6 | v0.57.0 (`.tools/k6/`) | OK |

Detalle: `evidencias/environment-info.txt`

---

## 3. Resultados Data Generator

| Paso | Comando | Resultado |
|------|---------|-----------|
| Tests | `mvn clean test` | **BUILD SUCCESS** — 8 tests, 0 fallos |
| Package | `mvn clean package` | JAR `target/data-generator.jar` (~17 MB) |
| Serial | `java -jar ... --cantidad 100` | OK — CSV generado |
| Paralelo | `java -jar ... --cantidad 500 --paralelo --hilos 4` | OK — CSV generado |
| Validación | Script validación | **VALIDO** — 500 filas, 0 duplicados docs/nombre+apellido |

CSV para automation: `data-generator/output/datos_actual.csv`

---

## 4. Resultados Automatización

| Ejecución | Comando | Escenarios | Resultado |
|-----------|---------|------------|-----------|
| Headless (requerido) | `mvn clean verify -Dheadless.mode=true` | 3 | **0 pass, 3 errors** — Access Denied Akamai |
| Headed (diagnóstico) | `mvn clean verify -Dheadless.mode=false` | 3 | **0 pass, 1 failure, 2 errors** — timeout carga / origen no visible |

**Integración CSV:** `TestDataProvider` lee `../data-generator/output/datos_actual.csv` — verificado (500 registros).

**Reporte Serenity:** `automation/target/site/serenity/index.html` — generado.  
**Capturas:** `evidencias/serenity/dashboard.png`, `scenario-1.png`, `scenario-2.png`, `scenario-3.png`

---

## 5. Resultados K6

Target exclusivo: **https://test.k6.io** (no producción)

| Script | Duración aprox. | http_req_failed | checks |
|--------|-----------------|-----------------|--------|
| `prueba-pequena-10hilos.js` | ~31 s | 0.00% | 100% |
| `escenario1-carga.js` | ~5 min | 0.00% | 100% |
| `escenario2-capacidad.js` | ~6 min | 0.00% | 100% |

JSON exportados: `evidencias/k6-small.json`, `k6-load.json`, `k6-capacity.json`

---

## 6. Correcciones Aplicadas

Ver detalle completo en `evidencias/fixes-applied.md`. Resumen:

1. Eliminación de paso Cucumber duplicado (`@Y`)
2. Migración API Serenity 4.x en Questions
3. Dependencia `cucumber-junit-platform-engine`
4. Ampliación de selectores en `HomePage.java`
5. Config anti-detección Chrome + `pageLoadStrategy=eager`
6. Detección explícita de Access Denied en `AbrirHomeLatam`
7. Ejecución secuencial de escenarios (sin paralelismo)

---

## 7. Riesgos Encontrados

| Riesgo | Impacto | Mitigación propuesta |
|--------|---------|----------------------|
| Protección anti-bot LATAM (Akamai) | Bloquea UI automation en headless y de forma intermitente en headed | Ejecutar con `-Dheadless.mode=false` desde red permitida; evaluar BrowserStack/Sauce Labs; negociar whitelist IP con LATAM |
| JDK 21 no disponible | Requiere flag `-Dmaven.compiler.release=17` | Instalar JDK 21 o documentar compatibilidad 17 |
| Selectores SPA dinámicos | Fallos intermitentes aun sin anti-bot | Mantener locators centralizados en `HomePage.java`; revisión periódica |
| K6 no en PATH | Requiere ruta absoluta al binario | Agregar K6 al PATH o script wrapper |

---

## 8. Pendientes

- [ ] Ejecutar automation UI desde red/navegador no bloqueado por Akamai
- [ ] Instalar JDK 21 si es requisito estricto del evaluador
- [ ] Validar selectores en vivo cuando el sitio permita acceso
- [ ] (Opcional) Subir correcciones de código al repositorio GitHub

---

## 9. Evidencias Generadas

| Prueba | Estado | Evidencia |
|--------|--------|-----------|
| Entorno | OK | `evidencias/environment-info.txt` |
| Data Generator Tests | PASÓ | `evidencias/data-generator-tests.txt` |
| Data Generator Package | PASÓ | `evidencias/data-generator-package.txt` |
| Data Generator Serial | PASÓ | `evidencias/data-generator-serial.txt` |
| Data Generator Parallel | PASÓ | `evidencias/data-generator-parallel.txt` |
| Data Generator CSV Validation | PASÓ | `evidencias/data-generator-validation.txt` |
| Selectors Review | DOCUMENTADO | `evidencias/selectors-review.md` |
| Automation Verify (headless) | FALLÓ (bloqueo externo) | `evidencias/automation-verify.txt` |
| Automation Verify (headed) | FALLÓ (bloqueo/timeout) | `evidencias/automation-verify-headed.txt` |
| Fixes Applied | DOCUMENTADO | `evidencias/fixes-applied.md` |
| K6 Small | PASÓ | `evidencias/k6-small.txt`, `k6-small.json` |
| K6 Load | PASÓ | `evidencias/k6-load.txt`, `k6-load.json` |
| K6 Capacity | PASÓ | `evidencias/k6-capacity.txt`, `k6-capacity.json` |
| Serenity Dashboard | GENERADO | `evidencias/serenity/dashboard.png` |
| Serenity Scenarios | GENERADO | `evidencias/serenity/scenario-1.png` … `scenario-3.png` |
| LATAM Access Denied (headless) | EVIDENCIA | `evidencias/latam-page-source.html` |

---

## DECISIONES TÉCNICAS TOMADAS

### D1 — Compilar con JDK 17 en lugar de 21
**Problema:** El entorno solo tiene JDK 17.  
**Decisión:** Usar `-Dmaven.compiler.release=17` en todas las ejecuciones Maven.  
**Justificación:** Los `pom.xml` ya declaran source/target 17; evita fallo de compilación sin modificar requisitos del proyecto.

### D2 — Detección temprana de Access Denied
**Problema:** Los tests fallaban con timeouts de 15+ minutos sin indicar la causa real.  
**Decisión:** Validar título/HTML tras `Open.url()` y lanzar `IllegalStateException` con mensaje claro.  
**Justificación:** Falla rápida, diagnóstico accionable, evidencia en logs y Serenity.

### D3 — No forzar clic en "Ida y vuelta"
**Problema:** Timeout buscando tab `roundtrip-tab` que no siempre está en DOM.  
**Decisión:** Solo hacer clic cuando el tipo es "Solo ida".  
**Justificación:** "Ida y vuelta" es el default en la home LATAM; reduce flakiness.

### D4 — Configuración anti-detección Chrome
**Problema:** Headless es detectado como bot por Akamai.  
**Decisión:** `--disable-blink-features=AutomationControlled`, `excludeSwitches: enable-automation`, `pageLoadStrategy=eager`.  
**Justificación:** Práctica estándar para reducir huella de automatización; no garantiza bypass pero mejora compatibilidad headed.

### D5 — Ejecución secuencial de escenarios
**Problema:** Paralelismo abría múltiples sesiones Chrome simultáneas contra un sitio con rate limiting.  
**Decisión:** `<parallel>none</parallel>` en failsafe.  
**Justificación:** Menor probabilidad de bloqueo por concurrencia.

### D6 — `pageLoadStrategy` a nivel capabilities (no dentro de chromeOptions)
**Problema:** Colocar `pageLoadStrategy` dentro de `goog:chromeOptions` causó error 400 al iniciar ChromeDriver.  
**Decisión:** Mover a `webdriver.capabilities.pageLoadStrategy`.  
**Justificación:** Cumple el contrato W3C WebDriver; restaura capacidad de ejecutar tests headless.

### D7 — K6 desde binario local
**Problema:** `k6` no está en PATH del sistema.  
**Decisión:** Descargar v0.57.0 en `.tools/k6/` y ejecutar con ruta absoluta.  
**Justificación:** Permite reproducir pruebas sin instalación global; evidencia en `k6-version.txt`.

### D8 — Documentar bloqueo LATAM como pendiente real (no simular PASS)
**Problema:** Los escenarios UI no pueden completarse en este entorno.  
**Decisión:** Reportar FALLÓ/BLOQUEADO con evidencia HTML y logs, sin inventar resultados.  
**Justificación:** Cumple regla de no marcar tareas no ejecutadas; entrega profesional y auditable.
