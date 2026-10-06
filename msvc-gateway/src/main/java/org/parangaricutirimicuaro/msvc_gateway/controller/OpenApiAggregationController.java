package org.parangaricutirimicuaro.msvc_gateway.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
public class OpenApiAggregationController {

    private static final Logger log = LoggerFactory.getLogger(OpenApiAggregationController.class);

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${INSPECTION_SERVICE_URL:http://localhost:8080}")
    private String inspectionUrl;

    @Value("${CLIENTES_SERVICE_URL:http://localhost:8081}")
    private String clientesUrl;

    @Value("${IDENTIDAD_SERVICE_URL:http://localhost:8082}")
    private String identidadUrl;

    public OpenApiAggregationController() {
        this.restClient = RestClient.create();
        this.objectMapper = new ObjectMapper();
    }

    @GetMapping(value = "/v3/api-docs/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getUnifiedOpenApi() {
        ObjectNode root = objectMapper.createObjectNode();
        root.put("openapi", "3.1.0");

        ObjectNode info = root.putObject("info");
        info.put("title", "RevTech Platform — Unified API");
        info.put("version", "v1.0");
        info.put("description", "API unificada que consolida los microservicios de Inspección Core, Clientes & Vehículos, e Identidad & Acceso.");

        ArrayNode servers = root.putArray("servers");
        ObjectNode server = servers.addObject();
        server.put("url", "http://localhost:8000");
        server.put("description", "API Gateway Entrypoint");

        ArrayNode combinedTags = root.putArray("tags");
        combinedTags.addObject()
                .put("name", "1. Inspección Técnica Vehicular (Core)")
                .put("description", "Ciclo de vida de la inspección, registro de pruebas técnicas y emisión de certificados o actas");
        combinedTags.addObject()
                .put("name", "2. Gestión de Vehículos")
                .put("description", "Catálogo, características técnicas y registro del parque vehicular");
        combinedTags.addObject()
                .put("name", "3. Gestión de Clientes")
                .put("description", "Registro y consulta de clientes, propietarios y conductores");
        combinedTags.addObject()
                .put("name", "4. Identidad y Acceso")
                .put("description", "Gestión de usuarios, inspectores, supervisores y roles del sistema");

        ObjectNode combinedPaths = root.putObject("paths");
        ObjectNode combinedComponents = root.putObject("components");
        ObjectNode combinedSchemas = combinedComponents.putObject("schemas");

        List<String> targetUrls = List.of(
                inspectionUrl + "/v3/api-docs",
                clientesUrl + "/v3/api-docs",
                identidadUrl + "/v3/api-docs"
        );

        for (String url : targetUrls) {
            try {
                String responseBody = restClient.get()
                        .uri(url)
                        .retrieve()
                        .body(String.class);

                if (responseBody != null) {
                    JsonNode spec = objectMapper.readTree(responseBody);

                    // Merge paths with section tagging
                    JsonNode paths = spec.get("paths");
                    if (paths != null && paths.isObject()) {
                        paths.fieldNames().forEachRemaining(path -> {
                            if (!path.startsWith("/scalar")) {
                                JsonNode pathItem = paths.get(path);
                                if (pathItem.isObject()) {
                                    String sectionTag;
                                    if (path.startsWith("/api/inspecciones")) {
                                        sectionTag = "1. Inspección Técnica Vehicular (Core)";
                                    } else if (path.startsWith("/api/vehiculos")) {
                                        sectionTag = "2. Gestión de Vehículos";
                                    } else if (path.startsWith("/api/clientes")) {
                                        sectionTag = "3. Gestión de Clientes";
                                    } else if (path.startsWith("/api/usuarios") || path.startsWith("/api/auth")) {
                                        sectionTag = "4. Identidad y Acceso";
                                    } else {
                                        sectionTag = "5. Otros Servicios";
                                    }

                                    pathItem.elements().forEachRemaining(operation -> {
                                        if (operation.isObject() && operation.has("operationId")) {
                                            ArrayNode opTags = ((ObjectNode) operation).putArray("tags");
                                            opTags.add(sectionTag);
                                        }
                                    });
                                }
                                combinedPaths.set(path, pathItem);
                            }
                        });
                    }

                    // Merge schemas
                    JsonNode components = spec.get("components");
                    if (components != null && components.has("schemas")) {
                        JsonNode schemas = components.get("schemas");
                        if (schemas.isObject()) {
                            schemas.fieldNames().forEachRemaining(schemaName -> combinedSchemas.set(schemaName, schemas.get(schemaName)));
                        }
                    }
                }
            } catch (Exception e) {
                log.warn("Could not fetch OpenAPI spec from {}: {}", url, e.getMessage());
            }
        }

        return ResponseEntity.ok(root.toString());
    }
}
