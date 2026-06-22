package com.greensqa.automation.stepdefinitions;

import com.greensqa.automation.model.TestPerson;
import com.greensqa.automation.questions.MensajeDeValidacion;
import com.greensqa.automation.questions.ResultadosDeVuelos;
import com.greensqa.automation.tasks.*;
import com.greensqa.automation.utils.TestDataProvider;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.ensure.Ensure;

import java.util.List;

/** Mapeo de pasos Gherkin -> tareas/preguntas Screenplay. */
public class BusquedaVuelosStepDefinitions {

    private TestPerson personaActual;

    @Dado("que el pasajero ingresa a la página de inicio de LATAM")
    public void ingresaHome() {
        OnStage.setTheStage(new OnlineCast());
        Actor pasajero = OnStage.theActorCalled("Pasajero");
        pasajero.attemptsTo(AbrirHomeLatam.ahora());
    }

    @Cuando("toma un pasajero desde los datos de prueba generados")
    public void tomaPasajeroDeDatos() {
        List<TestPerson> datos = TestDataProvider.cargarPorDefecto();
        personaActual = datos.stream()
                .filter(p -> !p.esEmpresa())
                .findFirst()
                .orElse(datos.get(0));
        OnStage.theActorInTheSpotlight().remember("persona", personaActual);
    }

    @Cuando("selecciona el tipo de viaje {string}")
    public void seleccionaTipoViaje(String tipo) {
        OnStage.theActorInTheSpotlight().attemptsTo(SeleccionarTipoViaje.de(tipo));
    }

    @Y("completa el origen {string} y el destino {string}")
    public void completaRuta(String origen, String destino) {
        OnStage.theActorInTheSpotlight().attemptsTo(IngresarRuta.de(origen, destino));
    }

    @Y("intenta completar el origen {string} y el destino {string}")
    public void intentaRuta(String origen, String destino) {
        OnStage.theActorInTheSpotlight().attemptsTo(IngresarRuta.de(origen, destino));
    }

    @Y("selecciona una fecha de ida a {int} días y una fecha de regreso a {int} días")
    public void seleccionaFechasIdaVuelta(int dIda, int dRegreso) {
        OnStage.theActorInTheSpotlight()
                .attemptsTo(SeleccionarFechas.idaYRegreso(dIda, dRegreso));
    }

    @Y("selecciona una fecha de ida a {int} días")
    public void seleccionaFechaIda(int dIda) {
        OnStage.theActorInTheSpotlight().attemptsTo(SeleccionarFechas.soloIda(dIda));
    }

    @Y("configura {int} pasajero adulto")
    public void configuraAdulto(int n) {
        OnStage.theActorInTheSpotlight().attemptsTo(ConfigurarPasajeros.adultos(n));
    }

    @Y("configura los pasajeros según la edad del dato de prueba")
    public void configuraSegunDato() {
        TestPerson p = OnStage.theActorInTheSpotlight().recall("persona");
        OnStage.theActorInTheSpotlight()
                .attemptsTo(ConfigurarPasajeros.segunEdad(p.getEdad()));
    }

    @Y("ejecuta la búsqueda de vuelos")
    public void ejecutaBusqueda() {
        OnStage.theActorInTheSpotlight().attemptsTo(BuscarVuelos.ahora());
    }

    @Entonces("se muestran resultados de vuelos disponibles para la ruta seleccionada")
    public void verificaResultados() {
        OnStage.theActorInTheSpotlight()
                .attemptsTo(Ensure.that(ResultadosDeVuelos.sonVisibles()).isTrue());
    }

    @Entonces("el sistema impide buscar vuelos con el mismo origen y destino")
    public void verificaImpedimento() {
        OnStage.theActorInTheSpotlight()
                .attemptsTo(Ensure.that(MensajeDeValidacion.seImpideBusqueda()).isTrue());
    }
}
