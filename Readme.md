# 🚀 Tecnologias Utilizadas

- Java 21
- Spring Boot
- Spring Data MongoDB
- RabbitMQ
- Docker

---

# 📋 Desafio

O enunciado completo do desafio está disponível no arquivo: [problem.md](./problem.md)


---

# 🐰 RabbitMQ

Acesse o painel administrativo do RabbitMQ:

```txt
http://localhost:15672/#/
```

## Credenciais padrão

```txt
Usuário: guest
Senha: guest
```

---

# 🍃 Banco de Dados

Para visualizar e interagir com o MongoDB, foi utilizado:

- MongoDB Compass

---

# 🐳 Docker

Para subir toda a infraestrutura do projeto:

```bash
docker compose up
```

---

# 📦 Exemplo de Payload JSON

```json
{
  "codigoPedido": 1001,
  "codigoCliente": 1,
  "itens": [
    {
      "produto": "lápis",
      "quantidade": 100,
      "preco": 1.10
    },
    {
      "produto": "caderno",
      "quantidade": 10,
      "preco": 1.00
    }
  ]
}
```

---

# 🔗 Endpoint de Consulta

Consultar pedidos de um cliente:

```http
GET http://localhost:8080/customers/1/orders
POST http://localhost:8080/publish
```
## 📝 Changelog

### [v2.0-SNAPSHOT] - 2026-05-20
#### 🟢 Added

- Novo endpoint `POST /publish` para cadastrar pedidos a partir de um payload JSON.
- O endpoint publica a mensagem no RabbitMQ, permitindo que o fluxo de mensageria processe e salve os dados no MongoDB.
- Antes, o envio da mensagem era feito manualmente pela tela `Publish message` do RabbitMQ Management.