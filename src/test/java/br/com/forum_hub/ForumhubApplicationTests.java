package br.com.forum_hub;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Contexto da aplicacao")
class ForumhubApplicationTests extends AbstractIntegrationTest {

    @Test
    @DisplayName("deve subir o contexto e aplicar todas as migrations do Flyway")
    void contextLoads() {
    }
}
