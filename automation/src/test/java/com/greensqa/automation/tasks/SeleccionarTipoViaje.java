package com.greensqa.automation.tasks;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import com.greensqa.automation.ui.HomePage;

/** Tarea: seleccionar el tipo de viaje (Ida y vuelta / Solo ida). */
public class SeleccionarTipoViaje implements Task {

    private final String tipo;

    public SeleccionarTipoViaje(String tipo) { this.tipo = tipo; }

    @Step("{0} selecciona el tipo de viaje '#tipo'")
    @Override
    public <T extends Actor> void performAs(T actor) {
        if (tipo.toLowerCase().contains("solo")) {
            actor.attemptsTo(Click.on(HomePage.TIPO_VIAJE_SOLO_IDA));
        }
        // Ida y vuelta suele ser la opción por defecto en la home; evitar clic innecesario.
    }

    public static Performable de(String tipo) {
        return net.serenitybdd.screenplay.Tasks.instrumented(SeleccionarTipoViaje.class, tipo);
    }
}
