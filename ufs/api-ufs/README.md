# Projeto API-UFS (Unidades Federativas)

Este projeto implementa uma API responsiva usando RSocket, para implementar um serviço CRUD para o assunto Unidade Federativa.
Também vamos utilizar o R2DBC com o cliente para o banco de dados MSSQL Server. Mas com poucas mudanças é possível utilizar outros bancos de dados suportados pelo R2DBC.
O motivo da utilização do MSSQL Server, é pelo fato de que a maioria dos exemplos que encontramos usa o postgresql ou mysql, e também pelo fato de que aonde eu trabalho atualmente usar o MSSQL.

Antes de executar a aplicação, execute o docker-compose para ativar a dependências do projeto

```
docker-compose up -d
```
O docker compose vai ativar um stack com os seguintes componentes : Mssql, Pgadmin, jaeger, elasticsearch,
mongodb e graylog. Para verificar se o stack subiu completo use o comando :

```
docker-compose ps
```

Quando não precisar mais do stack, utilize o seguinte comando para parar :
```
docker-compose down
```


