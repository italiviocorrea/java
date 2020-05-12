package eti.italiviocorrea.endpoint;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;

import javax.enterprise.context.ApplicationScoped;
import java.nio.file.Files;
import java.nio.file.Paths;

@Readiness
@ApplicationScoped
public class ReadinessHealthCheckResource implements HealthCheck {

    @Override
    public HealthCheckResponse call() {

        HealthCheckResponseBuilder responseBuilder =
                HealthCheckResponse.named("Verificação do sistema de arquivos");

        boolean tempFileExists = Files.exists(Paths.get("/tmp/tmp.lck"));

        if (!tempFileExists) {
            responseBuilder.up();
        }
        else {
            responseBuilder.down()
                    .withData("error", "Arquivo de bloqueio detectado!");
        }

        return responseBuilder.build();
    }

}