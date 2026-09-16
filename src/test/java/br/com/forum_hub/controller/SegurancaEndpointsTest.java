package br.com.forum_hub.controller;

import br.com.forum_hub.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@DisplayName("Regras de acesso dos endpoints")
class SegurancaEndpointsTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /cursos e publico e responde 200")
    void listarCursosEhPublico() throws Exception {
        mockMvc.perform(get("/cursos"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /topicos e publico e responde 200")
    void listarTopicosEhPublico() throws Exception {
        mockMvc.perform(get("/topicos"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /topicos sem token e bloqueado")
    void cadastrarTopicoSemTokenEhBloqueado() throws Exception {
        var corpo = """
                {
                  "titulo": "Duvida sobre Spring Security",
                  "mensagem": "Como configuro o filtro de token?",
                  "cursoId": 1
                }
                """;

        mockMvc.perform(post("/topicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpo))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("PUT /editar-perfil sem token e bloqueado")
    void editarPerfilSemTokenEhBloqueado() throws Exception {
        mockMvc.perform(put("/editar-perfil")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("A documentacao OpenAPI e publica e responde 200")
    void documentacaoOpenApiEhPublica() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk());
    }
}
