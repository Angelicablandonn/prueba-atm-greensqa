# Diseño de Casos de Prueba — Búsqueda de vuelos LATAM

**Módulo Diseño (PARTE 2).** Sitio bajo prueba: `https://www.latamairlines.com/co/es`
Funcionalidad: búsqueda de vuelos desde la página de inicio.

Se identifican 3 casos de prueba que cubren un camino feliz, un caso data-driven
con los datos generados en la PARTE 1, y una validación negativa.

---

## CP01 — Búsqueda de vuelo ida y vuelta (camino feliz)

| Campo | Detalle |
|-------|---------|
| **Objetivo** | Verificar que un pasajero puede buscar vuelos ida y vuelta y obtener resultados. |
| **Prioridad** | Alta (smoke) |
| **Precondición** | El usuario está en la home de LATAM y aceptó cookies. |
| **Datos** | Origen: Bogotá · Destino: Santiago · Ida: hoy+20 · Regreso: hoy+27 · 1 adulto |

**Pasos**
1. Seleccionar tipo de viaje "Ida y vuelta".
2. Ingresar origen "Bogotá" y elegir la sugerencia.
3. Ingresar destino "Santiago" y elegir la sugerencia.
4. Seleccionar fecha de ida y de regreso.
5. Confirmar 1 pasajero adulto.
6. Pulsar "Buscar".

**Resultado esperado:** se muestra el listado de vuelos disponibles para la ruta y fechas.

---

## CP02 — Búsqueda solo ida con datos de prueba generados (data-driven)

| Campo | Detalle |
|-------|---------|
| **Objetivo** | Verificar la búsqueda solo ida usando un pasajero leído del CSV de la PARTE 1, demostrando la integración entre ambos módulos. |
| **Prioridad** | Alta |
| **Precondición** | Existe un CSV de datos generado; el usuario está en la home. |
| **Datos** | Pasajero tomado del CSV (define menor/adulto según su edad) · Origen: Medellín · Destino: Lima · Ida: hoy+15 |

**Pasos**
1. Cargar el primer pasajero (no empresa) del CSV de datos de prueba.
2. Seleccionar tipo de viaje "Solo ida".
3. Ingresar origen "Medellín" y destino "Lima".
4. Seleccionar fecha de ida.
5. Configurar pasajeros según la edad del dato (adulto, o adulto + menor).
6. Pulsar "Buscar".

**Resultado esperado:** se muestran resultados de vuelos para la ruta seleccionada.

---

## CP03 — Validación negativa: origen igual a destino

| Campo | Detalle |
|-------|---------|
| **Objetivo** | Verificar que el sistema no permite buscar vuelos con el mismo origen y destino. |
| **Prioridad** | Media |
| **Precondición** | El usuario está en la home. |
| **Datos** | Origen: Bogotá · Destino: Bogotá |

**Pasos**
1. Seleccionar tipo de viaje "Solo ida".
2. Intentar ingresar origen "Bogotá" y destino "Bogotá".

**Resultado esperado:** el sistema impide continuar (muestra mensaje de validación o no
habilita la búsqueda / no produce resultados).

---

## Trazabilidad con la automatización

| Caso | Escenario Gherkin | Tag |
|------|-------------------|-----|
| CP01 | "CP01 - Búsqueda de vuelo ida y vuelta para un pasajero adulto" | `@caso1` |
| CP02 | "CP02 - Búsqueda solo ida usando datos de prueba generados" | `@caso2 @datos_csv` |
| CP03 | "CP03 - Validación de origen y destino iguales" | `@caso3` |

Archivo: `automation/src/test/resources/features/busqueda_vuelos.feature`.
