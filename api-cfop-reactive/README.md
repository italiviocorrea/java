# api-cfop-reactive project

Este projeto usa o Quarkus, e o Supersonic Subatomic Java Framework.
Se você deseja aprender mais sobre o Quarkus, por favor visite o sítio: https://quarkus.io/ .

O objetivo deste projeto é mostrar o uso do Quarkus para criar um micro serviço reativo JAX-RS com vert.x e acessando
um banco de dados relacional MSSQLServer da Microsoft via Hibernate ORM, este banco de dados não tem um cliente reactivo
vert.x, então usei uma abordagem de usar UNI que fornece o item usando a chamada de bloqueio (método sincrono do banco de dados).
Para evitar o bloqueio, foi utilizado o subscribeOn para chamar o metodo sincrono em outra thread. Aqui passei o pool de thread
padrão, mas você pode personalizar seu próprio executor.
Nesta API também é demonstrado o uso OpenTracing (jaegger), métricas que podem ser usadas para monitoramento, captura e envio
de logs para o graylog, exemplo de healt checks e uso de recursos para tolerância a falha : resiliência com timeouts, retrys, fallbacks,
e Circuit Breaker.

## Executando a aplicação no modo dev

Antes de executar a aplicação, execute o docker-compose para ativar a dependências do projeto

```
docker-compose up -d
```
O docker compose vai ativar um stack com os seguintes componentes : MSSql Server, jaeger, elasticsearch,
mongodb e graylog. Para verificar se o stack subiu completo use o comando :

```
docker-compose ps
```

Quando não precisar mais do stack, utilize o seguinte comando para parar :
```
docker-compose down
```

Você pode executar seu aplicativo no modo dev, que permite codificar e compilar imediatamente.:
```
./mvnw quarkus:dev
```

As seguintes URIs para acessar : metrics, health check, jaeguer e pgAdmin.
- Documentação OpenAPI
```
http://localhost:8080/dfe/v1/cfops/swagger-ui/
```
- Métricas
```
http://localhost:8080/dfe/v1/cfops/metrics/
http://localhost:8080/dfe/v1/cfops/metrics/application

```
- Health Checks
```
http://localhost:8080/dfe/v1/cfops/health/
http://localhost:8080/dfe/v1/cfops/health/live
http://localhost:8080/dfe/v1/cfops/health/ready
```

## Compilando e executando a aplicação

O aplicativo pode ser compilado usando `./mvnw package`.
Ele produzirá o arquivo `api-cfop-reactive-1.0-SNAPSHOT-runner.jar` no diretório `/target` .
Esteja ciente de que não é um _über-jar_, pois as dependências são copiadas no diretório `target/lib` .

O aplicativo agora pode ser executado usando `java -jar target/api-cfop-reactive-1.0-SNAPSHOT-runner.jar`.

## Criando um executável nativo

Você pode criar um executável nativo usando: `./mvnw package -Pnative`.

Ou, se você não tiver o GraalVM instalado, poderá executar a compilação nativa em um contêiner usando: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

Você pode então executar seu executável nativo com: `./target/api-cfop-reactive-1.0-SNAPSHOT-runner`

Se você quiser saber mais sobre a criação de executáveis nativos, consulte https://quarkus.io/guides/building-native-image.