# Microservice para inclusão de Lista de Certificado Revogado usando Arquitetura Hexagonal
__________________________________________________________________________________________

## Tecnologias utilizadas
_________________________

    * JDK 11
    * Maven
    * Lombok
    * Spring WebFlux
    * Spring R2BC
    * Mssql JDBC Driver
    * Docker

## Executando a aplicação

    1. Execute os serviços Docker:

        `docker-compose up -d`

    2. Execute a aplicação: 

        `./mvnw spring-boot:run`

### Envidando uma requisição via cliente rsocket (rsc)

`java -jar /home/icorrea/tools/rsocket-client/rsc-0.6.1.jar \
--request --route=api-lcr.incluir --debug tcp://localhost:7000 \
-d '{"nomeAC":"AC SOLUTI Multipla v5","urlLcr":"http://ccd.acsoluti.com.br/lcr/ac-soluti-multipla-v5.crl","indiLcrDelta":0,"indiAtualzLcr":"S"}'`
