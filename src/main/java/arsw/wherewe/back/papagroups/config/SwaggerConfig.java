package arsw.wherewe.back.papagroups.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PapaGroups API")
                        .version("1.0.0")
                        .description("API for managing groups in the PapaGroups service")
                        .contact(new Contact()
                                .name("Picada Team")
                                .email("picadaarsw2025@outlook.com")));
    }
}
