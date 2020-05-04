# Projeto api-paises-react 

Este projeto utiliza o Quarkus, vert.x e postgresql, para demonstrar uma api REST reativa. Para este propósito vamos
construir uma API para manter o cadastro de países para o projeto de Documentos Fiscais Eletrônicos do Brasil.
A API usa o OpenAPI para documentação, demonstra o uso healt check, tracing com OpenTracing, captura de métricas, uso 
de tolerância a falhas e integração log centralizado usando o graylog.

Se você deseja conhecer o Quarkus, por favor visite o sítio: https://quarkus.io/ .
Se você deseja saber sobre a tabela de países do projeto de Documento Fiscais Eletrônicos, por favor visite o sitio: 
http://www.nfe.fazenda.gov.br/portal/listaConteudo.aspx?tipoConteudo=Iy/5Qol1YbE=

## Executando a aplicação no modo dev

Antes de executar a aplicação, execute o docker-compose para ativar a dependências do projeto

```
docker-compose up -d
```
O docker compose vai ativar um stack com os seguintes componentes : Postgresql, Pgadmin, jaeger, elasticsearch,
mongodb e graylog. Para verificar se o stack subiu completo use o comando :

```
docker-compose ps
```

Quando não precisar mais do stack, utilize o seguinte comando para parar :
```
docker-compose down
```
           
Para você pode executar seu aplicativo no modo dev, que permirá a codificação e o deploy automático das mudanças, use
no terminal linux :
```
./mvnw quarkus:dev
```
As seguintes URIs para acessar : metrics, health check, jaeguer e pgAdmin.
- Documentação OpenAPI
```
http://localhost:8080/dfe/v1/paises/docs/
```
- Métricas
```
http://localhost:8080/dfe/v1/paises/metrics/
http://localhost:8080/dfe/v1/paises/metrics/application

```
- Health Checks
```
http://localhost:8080/dfe/v1/paises/health/
http://localhost:8080/dfe/v1/paises/health/live
http://localhost:8080/dfe/v1/paises/health/ready

```
- PgAdmin : ferramenta para gerenciar o postgresql
```
http://localhost:8181
```

## Compilando e executando o aplicativo

A sua aplicação pode ser compilado usando  `./mvnw package`.
Ele gerará o arquivo `api-paises-react-1.0-SNAPSHOT-runner.jar` no diretório `/target` .
Esteja ciente de que não é um uber-jar, pois as dependências são copiadas para o diretório `target/lib` .

O aplicativo agora pode ser executado usando `java -jar target/api-paises-react-1.0-SNAPSHOT-runner.jar`.

## Criando um executável nativo

Você pode criar um executável nativo usando: `./mvnw package -Pnative`.

Ou, se você não tiver o GraalVM instalado, poderá executar a compilação executável nativa em um contêiner usando: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

Você pode então executar seu executável nativo com: `./target/api-paises-react-1.0-SNAPSHOT-runner`

Se você quiser saber mais sobre a criação de executáveis nativos, consulte https://quarkus.io/guides/building-native-image.