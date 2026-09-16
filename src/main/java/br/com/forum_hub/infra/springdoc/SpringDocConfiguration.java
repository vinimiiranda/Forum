package br.com.forum_hub.infra.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Informe o token retornado por POST /login, sem o prefixo Bearer.")))
                .info(new Info()
                        .title("Forum Hub API")
                        .version("v1")
                        .description("API REST de forum com autenticacao JWT, refresh token, 2FA (TOTP), "
                                + "login social via OAuth2 e controle de acesso por perfis.")
                        .contact(new Contact()
                                .name("Vinicius de Miranda Melo")
                                .url("https://github.com/vinimiiranda"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
