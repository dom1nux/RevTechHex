package org.parangaricutirimicuaro.msvc_inspection.config;

import com.scalar.maven.webmvc.ScalarWebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ScalarWebMvcAutoConfiguration.class)
public class ScalarConfig {
}
