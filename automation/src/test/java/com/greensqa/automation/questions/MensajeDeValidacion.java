package com.greensqa.automation.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import com.greensqa.automation.ui.HomePage;

/** Pregunta: ¿el sistema impide la búsqueda (no hay resultados / hay error)? */
public class MensajeDeValidacion implements Question<Boolean> {
    @Override
    public Boolean answeredBy(Actor actor) {
        boolean hayError = HomePage.MENSAJE_ERROR.resolveFor(actor).isPresent();
        boolean sinResultados = !HomePage.RESULTADOS.resolveFor(actor).isPresent();
        return hayError || sinResultados;
    }
    public static Question<Boolean> seImpideBusqueda() { return new MensajeDeValidacion(); }
}
