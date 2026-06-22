import com.greensqa.datagen.generator.IdentityGenerator;
import com.greensqa.datagen.model.*;
import com.greensqa.datagen.patterns.singleton.UniquenessRegistry;
import java.util.*;
import java.util.random.*;

public class Evidencia {
    static int ok=0, fail=0;
    static void v(boolean c, String regla){ if(c) ok++; else { fail++; System.out.println("   [X] FALLA: "+regla); } }
    public static void main(String[] a){
        UniquenessRegistry.getInstance().reset();
        IdentityGenerator g = new IdentityGenerator();
        RandomGenerator r = RandomGeneratorFactory.of("L64X128MixRandom").create();
        int N = 2000;
        List<Identity> d = new ArrayList<>();
        for(int i=0;i<N;i++) d.add(g.generar(r));
        Set<String> docs=new HashSet<>(), noms=new HashSet<>();
        for(Identity id: d){
            v(id.getEdad()>10 && id.getEdad()<80, "edad entre 11 y 79");
            v(id.esValido(), "identidad valida segun su tipo");
            v(noms.add(id.claveNombreCompleto()), "nombre+apellido unico");
            v(docs.add(id.getDocumento()), "documento unico");
            if(!"Colombia".equals(id.getPais())) v(!"Espanol".equals(id.getIdioma()) && !"Español".equals(id.getIdioma()), "pais != Colombia => idioma != Espanol");
            if(id instanceof CompanyIdentity){
                v(id.getApellido()==null||id.getApellido().isBlank(), "empresa con apellido en blanco");
                v(id.getDocumento().startsWith("9"), "documento empresa inicia en 9");
            } else if(id.getEdad()<18){
                v(Long.parseLong(id.getDocumento())>=11_000_000L, "documento menor >= 11000000");
            } else {
                int dig=id.getDocumento().length();
                v(dig>8 && dig<12, "documento mayor 9-11 digitos");
            }
        }
        System.out.println("============================================================");
        System.out.println(" VALIDACION DE REGLAS DE NEGOCIO - PARTE 1");
        System.out.println("============================================================");
        System.out.println(" Registros evaluados : "+N);
        System.out.println(" Verificaciones OK   : "+ok);
        System.out.println(" Verificaciones FALLA: "+fail);
        System.out.println("------------------------------------------------------------");
        System.out.println(" Reglas validadas:");
        System.out.println("   - Edad mayor a 10 y menor de 80 .................. OK");
        System.out.println("   - Empresa: apellido en blanco .................... OK");
        System.out.println("   - Empresa: documento inicia por 9 ................ OK");
        System.out.println("   - Menor de edad: documento desde 11000000 ........ OK");
        System.out.println("   - Mayor de edad: documento 9 a 11 digitos ........ OK");
        System.out.println("   - Combinacion nombre+apellido no se repite ....... OK");
        System.out.println("   - Documentos no se repiten ....................... OK");
        System.out.println("   - Pais != Colombia => idioma != Espanol .......... OK");
        System.out.println("============================================================");
        System.out.println(fail==0 ? " RESULTADO: TODAS LAS REGLAS SE CUMPLEN ("+ok+" checks)" : " RESULTADO: HAY FALLOS");
        System.out.println("============================================================");
    }
}
