/**
 * =============================================================
 *  File: JacksonConfig.java
 *  Author: Daniel Mihalcioiu
 *  Description: Configuration class for customizing Jackson’s ObjectMapper.
 *               Ensures proper serialization and deserialization of Java 8+
 *               date and time API objects (e.g., LocalDate, LocalDateTime).
 * =============================================================
 */

package ch.vaudoise.exercice.api_factory.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class JacksonConfig {

    private final ObjectMapper objectMapper;

    /**
     * Constructor injection of the global ObjectMapper.
     * Automatically registers all detected Jackson modules.
     *
     * @param objectMapper the main ObjectMapper used by Spring Boot
     */
    public JacksonConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        objectMapper.findAndRegisterModules();
    }

    /**
     * Post-construction setup to register the JavaTimeModule.
     * This enables correct (de)serialization of LocalDate and LocalDateTime
     * in ISO-8601 format, instead of timestamps.
     */
    @PostConstruct
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }
}
