package com.greensqa.automation.tasks;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Open;
import com.greensqa.automation.ui.HomePage;
import org.openqa.selenium.WebDriver;

/** Tarea: abrir la home de LATAM y aceptar cookies si aparecen. */
public class AbrirHomeLatam implements Task {

    private static final String URL = System.getProperty("base.url", "https://www.latamairlines.com/co/es");

    @Step("{0} abre la home de LATAM")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Open.url(URL));
        // Aceptar cookies de forma tolerante (no falla si no aparece)
        try {
            WebDriver driver = BrowseTheWeb.as(actor).getDriver();
            if (!driver.findElements(org.openqa.selenium.By.cssSelector(
                    "#cookies-politics-button")).isEmpty()) {
                actor.attemptsTo(Click.on(HomePage.ACEPTAR_COOKIES));
            }
        } catch (Exception ignored) { }
    }

    public static Performable ahora() {
        return net.serenitybdd.screenplay.Tasks.instrumented(AbrirHomeLatam.class);
    }
}
