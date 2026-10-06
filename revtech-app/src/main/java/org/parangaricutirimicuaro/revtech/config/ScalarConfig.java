package org.parangaricutirimicuaro.revtech.config;

import com.scalar.maven.webmvc.ScalarWebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Activa el portal de documentación interactiva Scalar en {@code /scalar}.
 */
@Configuration
@Import(ScalarWebMvcAutoConfiguration.class)
public class ScalarConfig {
}
