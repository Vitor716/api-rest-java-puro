# API REST com Java Puro

API REST de **Clientes** construída em **Java puro**, sem Spring, usando o servidor HTTP que já vem no JDK (`com.sun.net.httpserver`), **Jackson** para JSON e **Hibernate/JPA** para persistência em **PostgreSQL**.

O objetivo do projeto é entender, na prática, **como uma requisição HTTP percorre uma aplicação Java do início ao fim**, sem a "mágica" de um framework.

> Projeto de estudo baseado no curso [readme-curso-api-rest-java-puro](https://github.com/TeamDevForce/readme-curso-api-rest-java-puro) da TeamDevForce.

---

## Stack

| Tecnologia | Uso |
| --- | --- |
| Java 21 | Linguagem |
| `com.sun.net.httpserver.HttpServer` | Servidor HTTP (nativo do JDK) |
| Jackson 2.17 | Conversão JSON ↔ objeto Java (com suporte a `LocalDate`) |
| Hibernate 6.5 / Jakarta Persistence (JPA) | Mapeamento objeto-relacional |
| PostgreSQL 16 | Banco de dados (via Docker) |
| Maven | Build e dependências |

---

## Arquitetura

Cada camada tem uma única responsabilidade:

```text
Cliente HTTP
     ↓
HttpServer          → recebe a conexão na porta 8080
     ↓
ClienteController   → entende HTTP: método, URL, body, JSON, status code
     ↓
ClienteService      → regras de negócio e validações
     ↓
ClienteRepository   → acesso aos dados (interface + implementação JPA)
     ↓
PostgreSQL
```

As dependências são montadas manualmente na `Main` (injeção pelo construtor):

```text
ClienteRepositoryImpl → ClienteService → ClienteController → HttpServer
```

### Estrutura de pastas

```text
src/main/java/com/devforce/api
├── Main.java                         # monta as dependências e sobe o servidor
├── config/JPAUtil.java               # fábrica de EntityManager
├── controller/ClienteController.java # camada HTTP (roteamento + respostas)
├── service/ClienteService.java       # regras de negócio
├── repository/
│   ├── ClienteRepository.java        # contrato (o QUE fazer)
│   └── ClienteRepositoryImpl.java    # implementação JPA (COMO fazer)
├── model/Cliente.java                # entidade JPA
└── util/
    ├── JsonUtil.java                 # JSON ↔ objeto
    └── RepostaUtil.java              # helpers de leitura/escrita HTTP

src/main/resources
├── META-INF/persistence.xml          # configuração do JPA/Hibernate
└── simplelogger.properties           # configuração de logs
```

---

## Endpoints

| Método | Rota | Descrição | Sucesso | Status |
| --- | --- | --- | --- | --- |
| `POST` | `/clientes` | Cadastrar cliente | `201` | ✅ implementado |
| `GET` | `/clientes` | Listar clientes | `200` | 🚧 em andamento |
| `GET` | `/clientes/{id}` | Buscar por ID | `200` | 🚧 em andamento |
| `PUT` | `/clientes/{id}` | Atualizar cliente | `200` | 🚧 em andamento |
| `DELETE` | `/clientes/{id}` | Excluir cliente | `204` | 🚧 em andamento |

### Respostas de erro

| Status | Quando |
| --- | --- |
| `400` | JSON inválido, ID não numérico ou nome em branco |
| `404` | Cliente não encontrado |
| `405` | Método não suportado na rota |
| `500` | Erro inesperado |

Formato:

```json
{ "erro": "Nome é obrigatório" }
```

---

## Como rodar

### Pré-requisitos

- Java 21+
- Maven 3.9+
- Docker Desktop

### 1. Subir o banco

```bash
docker compose up -d
docker compose ps      # aguarde o status "healthy"
```

Isso sobe um PostgreSQL 16 em `localhost:5432` com:

| Banco | Usuário | Senha |
| --- | --- | --- |
| `api_clientes` | `postgres` | `postgres` |

> As credenciais são apenas para desenvolvimento local.
> A tabela `clientes` é criada automaticamente pelo Hibernate (`hbm2ddl.auto=update`).

### 2. Rodar a API

Pelo IntelliJ: execute a classe `com.devforce.api.Main`.

Ou pelo terminal:

```bash
mvn compile exec:java
```

Saída esperada:

```text
API iniciada na porta 8080
```

> Se o banco não estiver rodando, a aplicação para logo na inicialização com a mensagem
> `Não foi possível conectar ao banco. O container está rodando?`

### 3. Testar

```bash
curl -i -X POST http://localhost:8080/clientes \
  -H "Content-Type: application/json" \
  -d '{"nome":"Marcelo","dataNascimento":"1992-05-20"}'
```

Resposta:

```http
HTTP/1.1 201 Created
Content-Type: application/json

{"id":1,"nome":"Marcelo","dataNascimento":"1992-05-20"}
```

Conferir no banco:

```bash
docker exec -it api-clientes-postgres psql -U postgres -d api_clientes -c "SELECT * FROM clientes;"
```

### Parar o banco

```bash
docker compose down      # para o container e mantém os dados
docker compose down -v   # para o container e apaga os dados
```

---

## Roadmap

- [x] Servidor HTTP com `HttpServer`
- [x] Controller com roteamento e tratamento de erros
- [x] Conversão JSON com Jackson (`LocalDate`)
- [x] Persistência com Hibernate/JPA + PostgreSQL no Docker
- [x] `POST /clientes`
- [ ] `GET /clientes` e `GET /clientes/{id}`
- [ ] `PUT /clientes/{id}`
- [ ] `DELETE /clientes/{id}`
