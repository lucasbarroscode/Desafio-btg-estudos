# AGENTS.md

## Stack
- Java 21+
- Spring Boot
- JUnit 5
- Mockito
- RabbitMq
- Docker
- Spring Data MongoDB

## Regras Gerais
- Sempre leia os arquivos em /specs antes de implementar
- Nunca implemente sem critérios de aceitação
- O código deve ser simples e legível
- Evite excesso de engenharia


 


## Fluxo de Trabalho Necessário
1. Leia as especificações no diretório /specs
2. Gere tasks.md se ele não existir
3. Implemente com base nas tarefas
4. Crie testes automatizados
5. Valide os critérios de aceitação

## Testes
- Cubra todos os critérios de aceitação
- Os testes devem ser claros e diretos
- O código gerado deve atingir **90% de cobertura de testes unitários**

## Restrições
- Não invente requisitos que não estejam descritos
- Não altere o comportamento sem atualizar a especificação

## Exemplo do payload Json

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

## Consultar pedidos de um cliente:

```http
GET http://localhost:8080/customers/1/orders
POST http://localhost:8080/publish
```