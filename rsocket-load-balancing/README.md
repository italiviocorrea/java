# java
## Balanceamento de carga RSocket - Lado do cliente

Neste exemplo, demonstramos o balanceamento de carga do Spring boot RSocket no lado cliente, com checagem de saúde do servidor.

Com base no exemplo do artigo, adicionei a checagem de saúde do servidor.

https://www.vinsguru.com/rsocket-load-balancing-client-side/


Execute 3 instâncias do aplicativo conforme mostrado aqui (execute este comando do server-app/target )

/usr/lib/jvm/java-11-openjdk-amd64/bin/java -jar -Dserver.port=9000 -Dspring.rsocket.server.port=6565 server-app/target/server-app-0.0.1-SNAPSHOT.jar
/usr/lib/jvm/java-11-openjdk-amd64/bin/java -jar -Dserver.port=9001 -Dspring.rsocket.server.port=6566 server-app/target/server-app-0.0.1-SNAPSHOT.jar
/usr/lib/jvm/java-11-openjdk-amd64/bin/java -jar -Dserver.port=9002 -Dspring.rsocket.server.port=6567 server-app/target/server-app-0.0.1-SNAPSHOT.jar




