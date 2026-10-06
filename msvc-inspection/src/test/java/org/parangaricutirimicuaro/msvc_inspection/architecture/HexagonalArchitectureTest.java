package org.parangaricutirimicuaro.msvc_inspection.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.base.DescribedPredicate.alwaysTrue;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;

/**
 * Garantiza la regla de dependencias de la arquitectura hexagonal: adaptadores → aplicación → dominio.
 */
@AnalyzeClasses(packages = "org.parangaricutirimicuaro.msvc_inspection", importOptions = ImportOption.DoNotIncludeTests.class)
class HexagonalArchitectureTest {

    private static final String BASE = "org.parangaricutirimicuaro.msvc_inspection";

    @ArchTest
    static final ArchRule capas_hexagonales = onionArchitecture()
            .domainModels(BASE + ".domain..")
            .applicationServices(BASE + ".application..")
            .adapter("web", BASE + ".adapter.in.web..")
            .adapter("event-in", BASE + ".adapter.in.event..")
            .adapter("persistence", BASE + ".adapter.out.persistence..")
            .adapter("client", BASE + ".adapter.out.client..")
            .adapter("event-out", BASE + ".adapter.out.event..")
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
}
