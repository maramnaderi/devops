package tn.esprit.spring.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("🎿 SKI STATION MANAGEMENT 🚠")
                .description("📘 Case Study - SKI STATION Project")
                .version("1.0.0")
                .contact(apiContact());
    }

    private Contact apiContact() {
        return new Contact()
                .name("TEAM ASI II")
                .email("ons.bensalah@esprit.tn")
                .url("https://www.linkedin.com/in/ons-ben-salah-24b73494/");
    }

    @Bean
    public GroupedOpenApi skiStationApiGroup() {
        return GroupedOpenApi.builder()
                .group("SKI STATION API")
                .pathsToMatch("/**")
                .build();
    }
}
