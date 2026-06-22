package com.greensqa.automation.tasks;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import com.greensqa.automation.ui.HomePage;

/** Tarea: ingresar origen y destino con autocompletado. */
public class IngresarRuta implements Task {

    private final String origen;
    private final String destino;

    public IngresarRuta(String origen, String destino) {
        this.origen = origen; this.destino = destino;
    }

    @Step("{0} ingresa la ruta #origen -> #destino")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(HomePage.CAMPO_ORIGEN),
                Enter.theValue(origen).into(HomePage.CAMPO_ORIGEN),
                Click.on(HomePage.sugerenciaAutocompletar(origen)),
                Click.on(HomePage.CAMPO_DESTINO),
                Enter.theValue(destino).into(HomePage.CAMPO_DESTINO),
                Click.on(HomePage.sugerenciaAutocompletar(destino))
        );
    }

    public static Performable de(String origen, String destino) {
        return net.serenitybdd.screenplay.Tasks.instrumented(IngresarRuta.class, origen, destino);
    }
}
