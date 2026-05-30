# 🐝 Bee Better - Backend

Backend da aplicação Bee Better, responsável pelo gerenciamento de usuários, autenticação, persistência de dados e integração com serviços externos.

## 🚀 Tecnologias Utilizadas

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT (JSON Web Token)
- OAuth2
- Maven

---

## 📋 Funcionalidades

### Autenticação e Segurança

- Cadastro de usuários
- Login com JWT
- Autenticação baseada em token
- Proteção de rotas com Spring Security
- Integração OAuth2:
  - Google
  - Facebook
  - X (Twitter)

### Gerenciamento de Dados

- Persistência com PostgreSQL
- Operações CRUD
- Camada de Services
- Camada de Repositories
- DTOs para transferência de dados

### Upload de Arquivos

- Upload de imagens de perfil
- Armazenamento local de arquivos

### Validações

- Validação de e-mail
- Validação de senha forte:
  - Mínimo de 8 caracteres
  - Letra maiúscula
  - Letra minúscula
  - Número
  - Caractere especial

### Tratamento de Erros

- Tratamento centralizado de exceções
- Retorno padronizado para erros da API

---

## 📁 Estrutura do Projeto

```text
src
├── controller
├── service
├── repository
├── entity
├── dto
├── security
├── config
├── exception
└── util
```

---

## ⚙️ Configuração do Ambiente

### 1. Clone o projeto

```bash
git clone https://github.com/millagmgomes/beebetter.git
```

### 2. Configure as variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto:

```env
DATABASE_USER=seu_usuario
DATABASE_PASSWORD=sua_senha

JWT_SECRET=sua_chave_jwt

GOOGLE_CLIENT_ID=seu_client_id
GOOGLE_CLIENT_SECRET=seu_client_secret
```

---

### 3. Configure o banco PostgreSQL

Exemplo:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/beebetter
spring.datasource.username=${DATABASE_USER}
spring.datasource.password=${DATABASE_PASSWORD}
```

---

### 4. Execute a aplicação

```bash
mvn spring-boot:run
```

ou

```bash
./mvnw spring-boot:run
```

---

## 🔐 Autenticação

Após realizar login, a API retorna um token JWT.

Exemplo:

```json
{
  "token": "eyJhbGciOiJIUzI1NiIs..."
}
```

As rotas protegidas devem receber o token no cabeçalho:

```http
Authorization: Bearer SEU_TOKEN
```

---

## 🧪 Testes

Os endpoints podem ser testados utilizando:

- Postman

---

## 👨‍💻 Equipe

Projeto desenvolvido para o Bee Better.

Backend desenvolvido e mantido pela equipe do projeto.

---

## 📌 Observações

- Dados sensíveis são armazenados por meio de variáveis de ambiente.
- O frontend da aplicação é mantido em um repositório/projeto separado.
- O sistema utiliza autenticação JWT para controle de acesso.
