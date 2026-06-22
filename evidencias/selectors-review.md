# Revisión de selectores — LATAM Airlines (co/es)

**Fecha:** 2026-06-22  
**URL inspeccionada:** https://www.latamairlines.com/co/es  
**Archivo de referencia:** `automation/src/test/java/com/greensqa/automation/ui/HomePage.java`

## Contexto de la inspección

Durante la validación se detectó que el sitio aplica **protección anti-bot (Akamai)**. En modo headless, Chrome recibe una página `Access Denied` sin el buscador de vuelos (evidencia: `evidencias/latam-page-source.html`). En modo headed, la carga es lenta e intermitente; los selectores del SPA pueden no estar disponibles de inmediato.

## Cambios documentados

| Elemento | Selector anterior | Selector actual / ampliado | Motivo |
|----------|-------------------|----------------------------|--------|
| Cookies | `#cookies-politics-button` | `#cookies-politics-button, button[data-testid='cookies-politics-button'], #onetrust-accept-btn-handler` | OneTrust es el banner activo en varias regiones |
| Ida y vuelta | `data-testid='roundtrip-tab'` | XPath con `roundtrip-tab`, `trip-type-selector-button-roundtrip`, `aria-label` y texto | Tolerancia a variantes del SPA |
| Solo ida | `data-testid='oneway-tab'` | XPath con `oneway-tab`, `trip-type-selector-button-oneway`, `aria-label` y texto | Mismo criterio |
| Origen | `#txtInputOrigin` | `#origin-input, #txtInputOrigin, input[data-testid='origin-input'], input[name='origin']` | IDs alternativos observados en versiones del widget |
| Destino | `#txtInputDestination` | `#destination-input, #txtInputDestination, input[data-testid='destination-input'], input[name='destination']` | IDs alternativos |
| Buscar | `button[data-testid='search-button']` | `button[data-testid='search-button'], #btnSearchCTA, button[aria-label*='Buscar']` | CTA con distintos atributos |
| Pestaña vuelos | *(no existía)* | `a[href*='oferta-vuelos'], button[data-testid='tab-flight'], #tabFlight, [data-testid='fsb-tab-flights']` | Fallback si el buscador no está en la home |
| Resultados | `.flight-information` | `[data-testid='flight-card'], .flight-information, section[aria-label*='vuelo']` | Tarjetas dinámicas post-búsqueda |
| Errores | `.error-message` | `[data-testid='error-message'], .error-message, [role='alert']` | Validación negativa CP03 |

## Comportamiento de tipo de viaje

| Acción | Cambio |
|--------|--------|
| "Ida y vuelta" | **No hace clic** — es la opción por defecto en la home |
| "Solo ida" | Clic en `TIPO_VIAJE_SOLO_IDA` |

## Integración CSV

- Ruta por defecto: `../data-generator/output/datos_actual.csv`
- Override: `-Dtestdata.csv=<ruta>`
- Archivo validado: 500 registros, 0 duplicados (`evidencias/data-generator-validation.txt`)

## Bloqueo externo identificado

No fue posible confirmar selectores en vivo de forma estable por:

1. **Access Denied** en headless (Akamai).
2. **Timeout de carga** en headed (>60 s en algunos intentos).
3. **SPA dinámico** con `data-testid` que cambian entre despliegues.

**Recomendación:** Ejecutar revisión manual con `-Dheadless.mode=false` desde red residencial/VPN permitida, o usar entorno de staging si LATAM lo provee.
