package org.parangaricutirimicuaro.revtech.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.stream.Stream;

import static com.tngtech.archunit.base.DescribedPredicate.alwaysTrue;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;

/**
 * Garantiza la regla de dependencias de la arquitectura hexagonal (adaptadores → aplicación → dominio)
 * y el aislamiento entre contextos acotados dentro del monolito.
 * <p>
 * Distribución híbrida: la capa es el paquete de primer nivel y el contexto un subpaquete de cada capa
 * ({@code domain.<contexto>}, {@code application.<contexto>}, {@code adapter.*.<contexto>}).
 */
@AnalyzeClasses(packages = HexagonalArchitectureTest.BASE, importOptions = ImportOption.DoNotIncludeTests.class)
class HexagonalArchitectureTest {

    static final String BASE = "org.parangaricutirimicuaro.revtech";

    /** Contextos acotados implementados. Agregar un contexto nuevo es agregar su nombre aquí. */
    static final List<String> CONTEXTOS = List.of("inspeccion", "clientes");

    @ArchTest
    static final ArchRule capas_hexagonales = onionArchitecture()
            .domainModels(BASE + ".domain..")
            .applicationServices(BASE + ".application..")
            .adapter("web", BASE + ".adapter.in.web..")
            .adapter("event-in", BASE + ".adapter.in.event..")
            .adapter("persistence", BASE + ".adapter.out.persistence..")
            .adapter("client", BASE + ".adapter.out.client..")
            .adapter("event-out", BASE + ".adapter.out.event..")
            .adapter("integration", BASE + ".adapter.out.integration..")
            .withOptionalLayers(true)
            // config es la raíz de composición: ensambla todas las capas por diseño
            .ignoreDependency(resideInAPackage(BASE + ".config.."), alwaysTrue());

    @ArchTest
    static final ArchRule dominio_independiente_de_frameworks = noClasses()
            .that().resideInAPackage(BASE + ".domain..")
            .should().dependOnClassesThat().resideInAnyPackage(
                    "org.springframework..", "jakarta..", "lombok..", "feign..", "com.fasterxml..", "tools.jackson..")
            .because("el dominio debe ser Java puro y testeable sin infraestructura");

    @ArchTest
    static final ArchRule aplicacion_independiente_de_frameworks = noClasses()
            .that().resideInAPackage(BASE + ".application..")
            .should().dependOnClassesThat().resideInAnyPackage(
                    "org.springframework..", "jakarta.persistence..", "feign..")
            .because("los casos de uso solo dependen de puertos; Spring los ensambla desde config");

    @ArchTest
    static final ArchRule adaptadores_aislados_entre_si = noClasses()
            .that().resideInAPackage(BASE + ".adapter.in..")
            .should().dependOnClassesThat().resideInAPackage(BASE + ".adapter.out..")
            .because("la entrada solo habla con la aplicación a través de puertos de entrada");

    @ArchTest
    static final ArchRule integracion_solo_por_puertos_de_entrada = noClasses()
            .that().resideInAPackage(BASE + ".adapter.out.integration..")
            .should().dependOnClassesThat().resideInAnyPackage(BASE + ".application.*.service..")
            .because("un contexto consume a otro únicamente a través de sus puertos de entrada")
            .allowEmptyShould(true);

    @TestFactory
    Stream<DynamicTest> contextos_aislados_entre_si() {
        JavaClasses clases = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(BASE);
        return CONTEXTOS.stream().flatMap(contexto -> {
            String[] ajenos = CONTEXTOS.stream()
                    .filter(otro -> !otro.equals(contexto))
                    .flatMap(otro -> Stream.of(
                            BASE + ".domain." + otro + "..",
                            BASE + ".application." + otro + ".."))
                    .toArray(String[]::new);
            if (ajenos.length == 0) {
                return Stream.empty();
            }
            ArchRule nucleo = noClasses()
                    .that().resideInAnyPackage(BASE + ".domain." + contexto + "..", BASE + ".application." + contexto + "..")
                    .should().dependOnClassesThat().resideInAnyPackage(ajenos)
                    .because("el núcleo de " + contexto + " solo referencia a otros contextos por identificador");
            ArchRule adaptadores = noClasses()
                    .that().resideInAnyPackage(
                            BASE + ".adapter.in.*." + contexto + "..",
                            BASE + ".adapter.out.persistence." + contexto + "..",
                            BASE + ".adapter.out.event." + contexto + "..")
                    .should().dependOnClassesThat().resideInAnyPackage(ajenos)
                    .because("solo adapter.out.integration puede cruzar a otro contexto");
            return Stream.of(
                    DynamicTest.dynamicTest(contexto + ": núcleo aislado", () -> nucleo.check(clases)),
                    DynamicTest.dynamicTest(contexto + ": adaptadores aislados", () -> adaptadores.check(clases)));
        });
    }
}
