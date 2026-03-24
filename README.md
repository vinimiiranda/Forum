# 🚀 Forum Hub - API REST com Spring Boot

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
* ModelMapper
* Spring Mail
* OAuth2 (Google e GitHub)
* TOTP (2FA)
* Maven

---

## 🚀 Como Executar o Projeto

### Pré-requisitos

* Java 21
* Maven
* MySQL
* IDE (IntelliJ recomendado)

---

## 🧪 Testando a API

Você pode utilizar:

* Postman
* Insomnia

Principais métodos HTTP utilizados:

* `GET`
* `POST`
* `PUT`
* `PATCH`
* `DELETE`

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

---

## 📈 Diferenciais do Projeto

* ✔️ Arquitetura limpa e organizada
* ✔️ Segurança avançada com JWT + 2FA
* ✔️ Integração com OAuth2
* ✔️ Controle de acesso com hierarquia de perfis
* ✔️ Versionamento de banco com Flyway
* ✔️ Uso de DTOs para desacoplamento
* ✔️ Boas práticas com Spring Boot

---

## 👨‍💻 Autor

**Vinicius de Miranda Melo**

* GitHub: https://github.com/vinimiiranda
* LinkedIn: (adicione seu link aqui)

---

💡 Projeto desenvolvido com foco em evolução no ecossistema Java e construção de APIs seguras, escaláveis e bem estruturadas.
