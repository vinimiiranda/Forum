package br.com.forum_hub;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Base para os testes de integracao.
 *
 * Sobe um MySQL real em container, aplica as migrations do Flyway e conecta a
 * aplicacao nele. Assim os testes rodam contra o mesmo banco usado em producao,
 * sem depender de nenhum MySQL instalado na maquina de quem roda o build.
 */
@SpringBootTest
@Testcontainers
public abstract class AbstractIntegrationTest {

    @Container
    @ServiceConnection
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0");
}
