# 🚀 Forum Hub - API REST com Spring Boot

[![CI](https://github.com/vinimiiranda/Forum/actions/workflows/ci.yml/badge.svg)](https://github.com/vinimiiranda/Forum/actions/workflows/ci.yml)
![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3-6DB33F?logo=springboot&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-ready-2496ED?logo=docker&logoColor=white)

API REST desenvolvida com **Java e Spring Boot** para gerenciamento de um fórum, com foco em **segurança, autenticação robusta e boas práticas de arquitetura back-end**.

O projeto implementa funcionalidades completas de autenticação, controle de acesso por perfis, gerenciamento de tópicos e respostas, além de recursos avançados como **JWT, OAuth2 (Google/GitHub) e autenticação em dois fatores (2FA)**.

---

## 🧠 Principais Funcionalidades

* 🔐 Autenticação com JWT (stateless)
* 🔄 Refresh Token
* 🔒 Autenticação em dois fatores (2FA - TOTP)
* 📧 Verificação de conta por e-mail
* 🌐 Login social (Google e GitHub)
* 👤 Gerenciamento de usuários
* 📝 CRUD de tópicos
* 💬 Respostas em tópicos
* 🛡️ Controle de acesso por perfis (Role Hierarchy)
* ⚙️ Edição de perfil e alteração de senha

---

## 📖 Documentação da API

Com a aplicação rodando, a documentação interativa fica disponível em:

| Recurso | URL |
|---|---|
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI (JSON) | http://localhost:8080/v3/api-docs |

Para testar os endpoints protegidos: faça `POST /login`, copie o token retornado, clique em **Authorize** no topo do Swagger UI e cole o token.

---

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas para garantir organização e separação de responsabilidades:

```
controller → recebe requisições HTTP
domain → regras de negócio, entidades e DTOs
infra → segurança, exceções e integrações externas
```

📁 Estrutura:

```
br.com.forum_hub
├── controller
├── domain
│   ├── usuario
│   ├── topico
│   ├── resposta
│   ├── curso
│   └── perfil
└── infra
    ├── security
    ├── springdoc
    ├── email
    └── exception
```

---

## 🔐 Segurança

A aplicação utiliza:

* Spring Security
* Autenticação stateless com JWT
* Filtro de autenticação personalizado
* Refresh Token
* Hierarquia de perfis
* Autorização por roles
* 2FA com TOTP

Nenhum segredo fica no código: credenciais de banco, segredo do JWT e chaves de OAuth2 são lidos de variáveis de ambiente.

---

## 👥 Perfis de Acesso

* `ADMIN`
* `MODERADOR`
* `INSTRUTOR`
* `ESTUDANTE`

---

## ⚙️ Tecnologias Utilizadas

* Java 21
* Spring Boot
* Spring Web
* Spring Data JPA
* Spring Security
* JWT
* Flyway
* MySQL
* Lombok
* Spring Mail
* OAuth2 (Google e GitHub)
* TOTP (2FA)
* springdoc-openapi (Swagger)
* Docker e Docker Compose
* Testcontainers
* Maven

---

## 🚀 Como Executar o Projeto

### Opção 1 — Docker Compose (recomendado)

Sobe a API e o MySQL juntos, sem precisar instalar nada além do Docker.

```bash
git clone https://github.com/vinimiiranda/Forum.git
cd Forum

cp .env.example .env    # preencha os valores

docker compose up --build
```

A API fica disponível em http://localhost:8080 e o Swagger UI em http://localhost:8080/swagger-ui.html

### Opção 2 — Localmente

**Pré-requisitos:** Java 21, Maven e MySQL.

```bash
export DB_PASSWORD=sua-senha
export JWT_SECRET=um-segredo-longo-e-aleatorio

mvn spring-boot:run
```

### Variáveis de ambiente

| Variável | Descrição |
|---|---|
| `DB_URL` | URL de conexão com o MySQL |
| `DB_USERNAME` | Usuário do banco |
| `DB_PASSWORD` | Senha do banco |
| `JWT_SECRET` | Segredo usado para assinar os tokens |
| `EMAIL_USERNAME` / `EMAIL_PASSWORD` | Conta de envio dos e-mails de verificação |
| `GITHUB_CLIENT_ID` / `GITHUB_CLIENT_SECRET` | Credenciais do OAuth2 do GitHub |
| `GOOGLE_CLIENT_ID` / `GOOGLE_CLIENT_SECRET` | Credenciais do OAuth2 do Google |

O arquivo `.env.example` lista todas elas.

---

## 🧪 Testes

Os testes de integração sobem um **MySQL real em container** via Testcontainers e aplicam as migrations do Flyway, então rodam contra o mesmo banco usado em produção — sem depender de nenhum MySQL instalado na máquina.

```bash
mvn verify
```

Cobertura atual: subida do contexto com todas as migrations aplicadas, rotas públicas, bloqueio das rotas protegidas sem token e disponibilidade da documentação OpenAPI.

O mesmo comando roda automaticamente no **GitHub Actions** a cada push e pull request.

---

## 🔗 Endpoints Principais

### 🔐 Autenticação

* `POST /login`
* `POST /verificar-a2f`
* `POST /atualizar-token`

### 👤 Usuários

* `POST /registrar`
* `GET /{nomeUsuario}`
* `PUT /editar-perfil`
* `PATCH /alterar-senha`

### 📝 Tópicos

* `POST /topicos`
* `GET /topicos`
* `GET /topicos/{id}`
* `PUT /topicos`
* `DELETE /topicos/{id}`

A lista completa, com os parâmetros e os corpos de requisição, está no Swagger UI.

---

## 📈 Diferenciais do Projeto

* ✔️ Arquitetura limpa e organizada
* ✔️ Segurança avançada com JWT + 2FA
* ✔️ Integração com OAuth2
* ✔️ Controle de acesso com hierarquia de perfis
* ✔️ Versionamento de banco com Flyway
* ✔️ Uso de DTOs para desacoplamento
* ✔️ Testes de integração com banco real
* ✔️ Pipeline de CI no GitHub Actions
* ✔️ Ambiente reproduzível com Docker

---

## 👨‍💻 Autor

**Vinicius de Miranda Melo**

* GitHub: https://github.com/vinimiiranda
* LinkedIn: https://www.linkedin.com/in/vinicius-miranda-melo
