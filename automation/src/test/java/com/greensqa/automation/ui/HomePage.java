package com.greensqa.automation.ui;

import net.serenitybdd.screenplay.targets.Target;

/**
 * Object de página (locators) de la home de LATAM para búsqueda de vuelos.
 *
 * NOTA: LATAM usa un SPA con Shadow DOM y atributos data-testid dinámicos. Los
 * selectores aquí son representativos y centralizados; ante cambios del sitio,
 * solo se ajustan en este punto (mantenibilidad - SRP a nivel de UI).
 */
public class HomePage {

    public static final Target TAB_VUELOS = Target.the("pestaña o enlace de vuelos")
            .locatedBy("a[href*='oferta-vuelos'], button[data-testid='tab-flight'], #tabFlight, [data-testid='fsb-tab-flights']");

    public static final Target ACEPTAR_COOKIES = Target.the("botón aceptar cookies")
            .locatedBy("#cookies-politics-button, button[data-testid='cookies-politics-button'], #onetrust-accept-btn-handler");

    public static final Target TIPO_VIAJE_IDA_VUELTA = Target.the("opción ida y vuelta")
            .locatedBy("//*[@data-testid='roundtrip-tab' or @data-testid='trip-type-selector-button-roundtrip' or contains(@aria-label,'Ida y vuelta') or contains(normalize-space(.),'Ida y vuelta')][self::button or self::a or @role='tab' or @role='button']");

    public static final Target TIPO_VIAJE_SOLO_IDA = Target.the("opción solo ida")
            .locatedBy("//*[@data-testid='oneway-tab' or @data-testid='trip-type-selector-button-oneway' or contains(@aria-label,'Solo ida') or contains(normalize-space(.),'Solo ida')][self::button or self::a or @role='tab' or @role='button']");

    public static final Target CAMPO_ORIGEN = Target.the("campo origen")
            .locatedBy("#origin-input, #txtInputOrigin, input[data-testid='origin-input'], input[name='origin']");

    public static final Target CAMPO_DESTINO = Target.the("campo destino")
            .locatedBy("#destination-input, #txtInputDestination, input[data-testid='destination-input'], input[name='destination']");

    public static Target sugerenciaAutocompletar(String ciudad) {
        return Target.the("sugerencia " + ciudad)
                .locatedBy("//*[contains(@class,'autocomplete') or contains(@role,'option')]//*[contains(text(),'" + ciudad + "')]");
    }

    public static final Target BOTON_BUSCAR = Target.the("botón buscar vuelos")
            .locatedBy("button[data-testid='search-button'], #btnSearchCTA, button[aria-label*='Buscar']");

    public static final Target SELECTOR_PASAJEROS = Target.the("selector de pasajeros")
            .locatedBy("[data-testid='pax-selector'], #pax-selector");

    public static final Target RESULTADOS = Target.the("contenedor de resultados de vuelos")
            .locatedBy("[data-testid='flight-card'], .flight-information, section[aria-label*='vuelo']");

    public static final Target MENSAJE_ERROR = Target.the("mensaje de validación")
            .locatedBy("[data-testid='error-message'], .error-message, [role='alert']");
}
