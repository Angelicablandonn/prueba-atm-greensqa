package com.greensqa.datagen;

import com.greensqa.datagen.generator.IdentityGenerator;
import com.greensqa.datagen.model.CompanyIdentity;
import com.greensqa.datagen.model.Identity;
import com.greensqa.datagen.model.PersonIdentity;
import com.greensqa.datagen.patterns.singleton.UniquenessRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Reglas de negocio del generador de datos LATAM")
class IdentityGeneratorTest {

    private IdentityGenerator generator;
    private RandomGenerator rnd;

    @BeforeEach
    void setUp() {
        UniquenessRegistry.getInstance().reset();
        generator = new IdentityGenerator();
        rnd = RandomGeneratorFactory.of("L64X128MixRandom").create();
    }

    private List<Identity> generarMuchos(int n) {
        return IntStream.range(0, n)
                .mapToObj(i -> generator.generar(rnd))
                .collect(Collectors.toList());
    }

    @Test
    @DisplayName("Edad siempre entre 11 y 79 inclusive")
    void edadEnRango() {
        for (Identity id : generarMuchos(500)) {
            assertTrue(id.getEdad() > 10 && id.getEdad() < 80,
                    "Edad fuera de rango: " + id.getEdad());
        }
    }

    @Test
    @DisplayName("Empresas: apellido en blanco y documento inicia por 9")
    void reglasEmpresa() {
        List<Identity> empresas = generarMuchos(800).stream()
                .filter(i -> i instanceof CompanyIdentity)
                .toList();
        assertFalse(empresas.isEmpty(), "Deberían generarse empresas");
        for (Identity e : empresas) {
            assertTrue(e.getApellido() == null || e.getApellido().isBlank(),
                    "Empresa con apellido: " + e);
            assertTrue(e.getDocumento().startsWith("9"),
                    "Documento de empresa no inicia por 9: " + e.getDocumento());
        }
    }

    @Test
    @DisplayName("Menores: documento >= 11000000")
    void reglasMenor() {
        List<Identity> menores = generarMuchos(1000).stream()
                .filter(i -> i instanceof PersonIdentity)
                .filter(i -> i.getEdad() < 18)
                .toList();
        for (Identity m : menores) {
            assertTrue(Long.parseLong(m.getDocumento()) >= 11_000_000L,
                    "Documento de menor < 11000000: " + m.getDocumento());
        }
    }

    @Test
    @DisplayName("Mayores: documento con dígitos > 8 y < 12")
    void reglasMayor() {
        List<Identity> mayores = generarMuchos(1000).stream()
                .filter(i -> i instanceof PersonIdentity)
                .filter(i -> i.getEdad() >= 18)
                .toList();
        for (Identity m : mayores) {
            int d = m.getDocumento().length();
            assertTrue(d > 8 && d < 12, "Dígitos fuera de rango (" + d + "): " + m);
        }
    }

    @Test
    @DisplayName("La combinación nombre+apellido no se repite")
    void nombreApellidoUnico() {
        List<Identity> datos = generarMuchos(1000);
        Set<String> claves = new HashSet<>();
        for (Identity id : datos) {
            assertTrue(claves.add(id.claveNombreCompleto()),
                    "Combinación repetida: " + id.claveNombreCompleto());
        }
    }

    @Test
    @DisplayName("Los documentos no se repiten")
    void documentoUnico() {
        List<Identity> datos = generarMuchos(1000);
        Set<String> docs = new HashSet<>();
        for (Identity id : datos) {
            assertTrue(docs.add(id.getDocumento()),
                    "Documento repetido: " + id.getDocumento());
        }
    }

    @Test
    @DisplayName("País distinto a Colombia no puede tener idioma Español")
    void idiomaSegunPais() {
        for (Identity id : generarMuchos(1000)) {
            if (!"Colombia".equals(id.getPais())) {
                assertNotEquals("Español", id.getIdioma(),
                        "País no-Colombia con Español: " + id);
            }
        }
    }

    @Test
    @DisplayName("Todas las identidades generadas son válidas según su tipo")
    void todasValidas() {
        for (Identity id : generarMuchos(500)) {
            assertTrue(id.esValido(), "Identidad inválida: " + id);
        }
    }
}
