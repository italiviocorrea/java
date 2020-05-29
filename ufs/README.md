# Projeto API-UFS (Unidades Federativas)

O objetivo neste projeto é demonstrar o uso do protocolo RSOCKET, como uma alternativa a construção de API's reativas.

O RSocket é um protocolo binário para uso em transportes de fluxo de bytes, como TCP, WebSockets e Aeron.

Ele permite os seguintes modelos de interação simétrica via mensagem assíncrona passando por uma única conexão:

- request / response (fluxo de 1)
- request / stream (fluxo finito de muitos)
- fire-and-forget (sem resposta)
- event subscription (fluxo infinito de muitos)

Para maiores informações consulte o link [RSOCKET.IO](http://rsocket.io/) 

Para este exemplo, vamos construir duas aplicações, usando a linguagem java :

- Um serviço RSocket que utilizara o TCP como transporte, este serviço gravará informações Unidades Federativas em um banco de dados MSSQL Server, através do client reativo R2DBC. Na verdade é um serviço CRUD.

- O outro serviço será uma API REST reativa, que funcionará como um cliente do serviço RSocket CRUD.

Para isto utilizarei o Spring boot + webflux para construir a API REST cliente e também utilizarei o Spring boot + RSocket + R2BDC para construir o serviço RSocket server.


