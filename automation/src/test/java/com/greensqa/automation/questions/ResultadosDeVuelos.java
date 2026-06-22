package com.greensqa.automation.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import com.greensqa.automation.ui.HomePage;

/** Pregunta: ¿se muestran resultados de vuelos? */
public class ResultadosDeVuelos implements Question<Boolean> {
    @Override
    public Boolean answeredBy(Actor actor) {
        return HomePage.RESULTADOS.resolveFor(actor).isPresent();
    }
    public static Question<Boolean> sonVisibles() { return new ResultadosDeVuelos(); }
}
