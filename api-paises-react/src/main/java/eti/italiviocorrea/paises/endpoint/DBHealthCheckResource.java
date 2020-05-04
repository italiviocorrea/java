package eti.italiviocorrea.paises.endpoint;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.*;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.net.Socket;

@Readiness
@ApplicationScoped
public class DBHealthCheckResource implements HealthCheck {

    @ConfigProperty(name = "db.host")
    String host;

    @ConfigProperty(name = "db.port")
    Integer port;

    @Override
    public HealthCheckResponse call() {

        HealthCheckResponseBuilder responseBuilder =
                HealthCheckResponse.named("Verificação de conexão com o banco de dados");

        try {
            serverListening(host, port);
            responseBuilder.up();
        } catch (Exception e) {
            // cannot access the database
            responseBuilder.down()
                    .withData("error", e.getMessage());
        }
        return responseBuilder.build();
    }

    private void serverListening(String host, int port) throws
            IOException {
        Socket s = new Socket(host, port);
        s.close();
    }
}