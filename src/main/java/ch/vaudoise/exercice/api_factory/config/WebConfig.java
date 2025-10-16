/**
 * =============================================================
 *  File: WebConfig.java
 *  Author: Daniel Mihalcioiu
 *  Description: Configuration class defining global CORS settings
 *               for the API. Allows requests from all origins and
 *               supports basic HTTP methods.
 * =============================================================
 */

package ch.vaudoise.exercice.api_factory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    /**
     * Defines global Cross-Origin Resource Sharing (CORS) rules.
     * Allows frontend apps (e.g., Postman, localhost HTML tester, etc.)
     * to communicate with the API without restriction.
     *
     * @return configured WebMvcConfigurer instance
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // applies to all endpoints
                        .allowedOrigins("*") // allows all origins (can restrict to localhost if needed)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS"); // typical REST methods
            }
        };
    }
}
