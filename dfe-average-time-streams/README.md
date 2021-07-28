# Projeto dfe-average-time-streams

Este projeto tem como objetivo experimentar os recursos da API kafka-streams oferecidas pelo Quarkus.
O projeto é dividido em dois módulos:
- **producer:** Este módulo gera os eventos em tópico kafka, representado pela classe NF3eEventoMsg. São gerados dois tipos de mensagens : NF3eAutorizadas e NF3eRejeitadas, e cada uma é enviada para seu tópico correspondente.
- **consumer:** Este módulo processa os eventos gerados pelo módulo producer, através de um pipeline realizando as transformações nos dados dos eventos e gerando novas mensagens. O resultado do processamento deverá ser uma agregação (soma, média, quantidade, maximo e mínimo) do tempo médio de processamento de cada Mensagem NF3e durante o intervalo de 5 minutos. Esse resultado deverá ser armazenado em um tópico kafka para futuras consultas através da API Interactive Query do Kafka Streams.

## Executando a aplicação 

Para você executar a aplicação execute os comandos abaixo listados :
```shell script
./mvnw clean package -f producer/pom.xml
./mvnw clean package -f consumer/pom.xml
docker-compose up --build
```

