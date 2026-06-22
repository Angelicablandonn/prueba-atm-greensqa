package com.greensqa.automation.tasks;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.targets.Target;

import java.time.LocalDate;

/**
 * Tarea: seleccionar fechas en el datepicker calculando días relativos a hoy,
 * de modo que la prueba no dependa de fechas fijas (data-driven por offset).
 */
public class SeleccionarFechas implements Task {

    private final int diasIda;
    private final Integer diasRegreso; // null = solo ida

    private SeleccionarFechas(int diasIda, Integer diasRegreso) {
        this.diasIda = diasIda; this.diasRegreso = diasRegreso;
    }

    public static Performable idaYRegreso(int ida, int regreso) {
        return net.serenitybdd.screenplay.Tasks.instrumented(
                SeleccionarFechas.class, ida, Integer.valueOf(regreso));
    }
    public static Performable soloIda(int ida) {
        return net.serenitybdd.screenplay.Tasks.instrumented(
                SeleccionarFechas.class, ida, (Integer) null);
    }

    @Step("{0} selecciona las fechas del viaje")
    @Override
    public <T extends Actor> void performAs(T actor) {
        LocalDate ida = LocalDate.now().plusDays(diasIda);
        actor.attemptsTo(Click.on(celdaFecha(ida)));
        if (diasRegreso != null) {
            LocalDate regreso = LocalDate.now().plusDays(diasRegreso);
            actor.attemptsTo(Click.on(celdaFecha(regreso)));
        }
    }

    private Target celdaFecha(LocalDate fecha) {
        // LATAM marca las celdas seleccionables con aria-label = fecha ISO
        String iso = fecha.toString();
        return Target.the("día " + iso)
                .locatedBy("//*[@aria-label[contains(.,'" + iso + "')] or @data-date='" + iso + "']");
    }
}
