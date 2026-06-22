package com.greensqa.automation.tasks;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import com.greensqa.automation.ui.HomePage;

/** Tarea: ejecutar la búsqueda de vuelos. */
public class BuscarVuelos implements Task {

    @Step("{0} ejecuta la búsqueda de vuelos")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Click.on(HomePage.BOTON_BUSCAR));
    }

    public static Performable ahora() {
        return net.serenitybdd.screenplay.Tasks.instrumented(BuscarVuelos.class);
    }
}
