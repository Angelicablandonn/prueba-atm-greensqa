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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/** Tarea: abrir la home de LATAM y aceptar cookies si aparecen. */
public class AbrirHomeLatam implements Task {

    private static final String URL = System.getProperty("base.url", "https://www.latamairlines.com/co/es");

    @Step("{0} abre la home de LATAM")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Open.url(URL));
        WebDriver driver = BrowseTheWeb.as(actor).getDriver();
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.or(
                        ExpectedConditions.titleContains("LATAM"),
                        ExpectedConditions.titleContains("Access Denied")));

        if (driver.getPageSource().contains("Access Denied")
                || driver.getTitle().contains("Access Denied")) {
            throw new IllegalStateException(
                    "LATAM bloqueó el acceso (protección anti-bot/Akamai). "
                            + "Intente -Dheadless.mode=false o ejecute desde otra red.");
        }

        // Aceptar cookies de forma tolerante (no falla si no aparece)
        try {
            Thread.sleep(3000);
            if (!driver.findElements(org.openqa.selenium.By.cssSelector(
                    "#cookies-politics-button, button[data-testid='cookies-politics-button'], #onetrust-accept-btn-handler")).isEmpty()) {
                actor.attemptsTo(Click.on(HomePage.ACEPTAR_COOKIES));
                Thread.sleep(1000);
            }
        } catch (Exception ignored) { }

        // Si el buscador no está visible, intentar abrir la sección de vuelos
        if (driver.findElements(org.openqa.selenium.By.cssSelector(
                "#origin-input, #txtInputOrigin, input[data-testid='origin-input']")).isEmpty()) {
            try {
                actor.attemptsTo(Click.on(HomePage.TAB_VUELOS));
                Thread.sleep(2000);
            } catch (Exception ignored) { }
        }
    }

    public static Performable ahora() {
        return net.serenitybdd.screenplay.Tasks.instrumented(AbrirHomeLatam.class);
    }
}
