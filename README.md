# TaskManager-Spring-QA

Uma API REST simples em Spring Boot para gerenciamento de tarefas (Task Manager). O projeto usa Spring Boot (via Maven), Spring Data JPA com H2 em memória, e inclui testes unitários com JUnit.

Este repositório é um exemplo didático: implementa endpoints básicos para listar e criar tarefas, persiste em H2 (memória) e vem acompanhado de testes. É ideal para estudar uma API Spring Boot pequena com persistência em memória.
---

## 📌 Visão geral

Este README seguirá o padrão de arquitetura de microsserviços (Eureka, Gateway, microsserviços de Clientes/Cartões/Avaliador) e inclui instruções para executar localmente componentes essenciais como RabbitMQ e Keycloak.

> Observação: o código presente neste repositório implementa um serviço de exemplo chamado `manager` (classe principal em `src/main/java/com/task/manager/Application.java`) com um controlador de tarefas (`TaskController`) usado para demonstração. Em uma arquitetura completa, cada bloco descrito abaixo normalmente existe em repositórios/módulos separados.

---

## Visão geral do projeto

O projeto contém uma API REST com os seguintes pontos principais:

- Endpoints para gerenciar tarefas (`/tasks`) — listagem e criação.
- Persistência usando H2 em memória (configurada em `src/main/resources/application.properties`).
- Entidade `Task` com campos `id`, `title`, `description` e `completed`.
- Testes unitários em `src/test/java/com/task/manager`.

### Endpoints (implementados neste repositório)

- GET /tasks → lista todas as tasks
- POST /tasks → cria uma nova task (body: JSON da entidade Task)

Exemplo de payload para POST /tasks:

```json
{
  "title": "Exemplo",
  "description": "Tarefa de teste",
  "completed": false
}
```

Observação: as rotas implementadas estão em `src/main/java/com/task/manager/controller/TaskController.java`.

---
## 🛠️ Stack Tecnológico

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange" />
  <img src="https://img.shields.io/badge/Spring%20Boot-3.5.11-green" />
  <img src="https://img.shields.io/badge/H2-Database-blue" />
  <img src="https://img.shields.io/badge/Lombok-annotations-yellow" />
  <img src="https://img.shields.io/badge/JPA-Hibernate-lightgrey" />
  <img src="https://img.shields.io/badge/Maven-3.x-red" />
</p>

---

## ⚙️ Configuração de Mensageria

Fila usada no padrão do projeto (conforme `MQConfig.java` esperado):

```text
emissao-cartoes
```

No ecossistema, `mscartoes` costuma ser o consumidor dessa fila para efetivar a emissão do cartão.

---

## 🔐 Autenticação e Autorização (Keycloak)

Este README inclui uma seção com instruções para rodar o Keycloak em modo de desenvolvimento e integrá-lo localmente.

### Rodar o Keycloak (modo dev)

Executar o container do Keycloak (porta 8180 neste exemplo, para evitar conflito com aplicações locais):

```powershell
docker run -p 8180:8080 \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  quay.io/keycloak/keycloak:latest start-dev
```

- Console de administração: http://localhost:8180/
- Usuário/Senha (dev): `admin` / `admin` (conforme variáveis acima)

### Configuração mínima (via console Keycloak)

1. Criar um Realm (ex.: `ms-realm`).
2. Criar um Client (ex.: `ms-client`) com Access Type `public` ou `confidential` (dev: `public`).
3. Criar usuários de teste (ex.: `usuario` / senha `senha`).

Para ambientes de produção, não use `start-dev` e configure HTTPS, certificados e políticas de segurança.

### Obter token (exemplo dev - grant_type=password)

```bash
curl -s -X POST "http://localhost:8180/realms/ms-realm/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=usuario" \
  -d "password=senha" \
  -d "grant_type=password" \
  -d "client_id=ms-client"
```

Resposta: JSON com `access_token`. Use no header das requisições:

```
Authorization: Bearer <access_token>
```

### Integração com Spring Boot

Configure `spring.security.oauth2.resourceserver.jwt.issuer-uri` (ou `jwk-set-uri`) apontando para o issuer do Keycloak (ex.: `http://localhost:8180/realms/ms-realm`). Proteja endpoints conforme roles.

Se quiser, posso gerar exemplos de `application.yml` para o `mscloudgateway` e para o `msavaliadorcredito` com as propriedades `spring.security.oauth2` já prontas.

---

## 🧪 Testes com Insomnia

Recomendo usar o Insomnia para testar endpoints e automatizar a obtenção do token JWT do Keycloak.

Exemplo rápido de Environment no Insomnia:

```json
{
  "base_url": "http://localhost:8080",
  "keycloak_url": "http://localhost:8180",
  "realm": "ms-realm",
  "client_id": "ms-client",
  "username": "usuario",
  "password": "senha"
}
```

Fluxo:

1. Request POST para `{{ keycloak_url }}/realms/{{ realm }}/protocol/openid-connect/token` para obter `access_token`.
2. Salvar `access_token` no Environment do Insomnia.
3. Testar endpoints protegidos com header `Authorization: Bearer {{ access_token }}`.

Exemplos úteis (endpoints deste repositório):

- Criar task (POST): `POST {{ base_url }}/tasks` — body JSON
- Listar tasks (GET): `GET {{ base_url }}/tasks`

---

## ▶️ Como Executar o Projeto (local)

### 1️⃣ Subir o RabbitMQ (console de gerenciamento)

```powershell
docker run -it --rm --name rabbitmq \
  -p 5672:5672 \
  -p 15672:15672 \
  rabbitmq:3.9-management
```

- Console: http://localhost:15672 (guest/guest)

### 2️⃣ Subir o Keycloak (opcional, para autenticação)

Veja a seção Keycloak acima (porta 8180 no exemplo).

### 3️⃣ Executar a aplicação Spring Boot (neste repositório)

No Windows (PowerShell) use o wrapper:

```powershell
.\\mvnw.cmd clean spring-boot:run
```

No Linux/macOS:

```bash
./mvnw clean spring-boot:run
```

Ou buildar o jar e executar:

```powershell
.\\mvnw.cmd -DskipTests package
java -jar target/*.jar
```

Observação: não foi definida explicitamente a propriedade `server.port` em `src/main/resources/application.properties`, portanto a aplicação usa a porta padrão `8080`.
Se preferir, defina `server.port` em `application.properties` ou via parâmetro de linha de comando:

```powershell
.\mvnw.cmd spring-boot:run -Dspring-boot.run.arguments="--server.port=9090"
```

### Estrutura principal do projeto

```
src/
  main/
    java/com/task/manager/
      Application.java            # classe principal
      controller/TaskController.java
      entity/Task.java
      repository/TaskRepository.java
      service/TaskService.java
    resources/
      application.properties      # H2 + JPA config
      static/
      templates/
  test/                          # testes unitários
```

### Entidade Task

A entidade `Task` possui os seguintes campos (vide `src/main/java/com/task/manager/entity/Task.java`):

- `id: Long` (PK)
- `title: String`
- `description: String`
- `completed: boolean`

### H2 Console

O projeto está configurado com H2 em memória. Console acessível em:

http://localhost:8080/h2-console

JDBC URL padrão (application.properties): `jdbc:h2:mem:taskdb`

### Exemplos de requisições (cURL)

Criar uma task:

```bash
curl -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Comprar leite","description":"Lembrar de comprar leite","completed":false}'
```

Listar tasks:

```bash
curl http://localhost:8080/tasks
```

---

### Testes

O projeto inclui testes unitários em `src/test/java/com/task/manager`.
Execute os testes com:

```powershell
.\mvnw.cmd test
```


### 4️⃣ Testes

Executar os testes unitários:

```powershell
.\\mvnw.cmd test
```

---

## ✅ Observações Finais

- Arquitetura baseada em microsserviços desacoplados.
- Demonstração de Service Discovery, API Gateway e Mensageria (RabbitMQ).
- Ideal para estudar Spring Cloud, OpenFeign e integração com Keycloak.

---

## Próximos passos que posso ajudar a automatizar

- Gerar versão em inglês do README
- Adaptar README para apresentação em portfólio GitHub
- Adicionar exemplos cURL e coleção Postman/Insomnia exportável
- Gerar `application.yml` de exemplo para Gateway e para `msavaliadorcredito` com OIDC

Se quiser que eu aplique alguma dessas opções, diga qual delas e eu implemento.
# TaskManager-Spring-QA