# 🚀 Tecnologias Utilizadas

- Java 21
- Spring Boot
- Spring Data MongoDB
- RabbitMQ
- Docker

---

# 📋 Desafio

O enunciado completo do desafio está disponível no arquivo:

```md
[problem.md](./problem.md)
```

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
```