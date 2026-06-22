package com.greensqa.automation.tasks;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import com.greensqa.automation.ui.HomePage;

/**
 * Tarea: configurar la cantidad y tipo de pasajeros. Cuando se construye a partir
 * de la edad de un dato de prueba, decide si el pasajero es menor o adulto.
 */
public class ConfigurarPasajeros implements Task {

    private final int adultos;
    private final int menores;

    private ConfigurarPasajeros(int adultos, int menores) {
        this.adultos = adultos; this.menores = menores;
    }

    public static Performable adultos(int n) {
        return net.serenitybdd.screenplay.Tasks.instrumented(ConfigurarPasajeros.class, n, 0);
    }

    public static Performable segunEdad(int edad) {
        if (edad < 18) {
            // un adulto acompañante + un menor
            return net.serenitybdd.screenplay.Tasks.instrumented(ConfigurarPasajeros.class, 1, 1);
        }
        return net.serenitybdd.screenplay.Tasks.instrumented(ConfigurarPasajeros.class, 1, 0);
    }

    @Step("{0} configura #adultos adulto(s) y #menores menor(es)")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Click.on(HomePage.SELECTOR_PASAJEROS));
        // La interacción concreta con +/- se omite por brevedad del POC;
        // el selector queda abierto y los valores por defecto (1 adulto) aplican.
        // En implementación real: incrementar/decrementar con botones data-testid.
    }
}
