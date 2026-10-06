package org.parangaricutirimicuaro.revtech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de arranque de RevTech. Spring escanea este paquete y sus subpaquetes, y las clases de {@code config} ensamblan cada contexto.
 */
@SpringBootApplication
public class RevTechApplication {

    public static void main(String[] args) {
        SpringApplication.run(RevTechApplication.class, args);
    }

}
