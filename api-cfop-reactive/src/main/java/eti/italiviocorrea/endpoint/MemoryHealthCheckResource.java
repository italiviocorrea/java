package eti.italiviocorrea.endpoint;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Liveness;

import javax.enterprise.context.ApplicationScoped;

@Liveness
@ApplicationScoped
public class MemoryHealthCheckResource implements HealthCheck {

    @ConfigProperty(name = "api_cfops.memory.threshold", defaultValue = "50000000")
    private long threshold;

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder =
                HealthCheckResponse.named("Verificação se há memória suficiente ");
        long freeMemory = Runtime.getRuntime().freeMemory();
        if (freeMemory >= threshold) {
            responseBuilder.up();
        } else {
            responseBuilder.down()
                    .withData("error", "Não encontrado memória livre! Por favor reinicie a aplicação "+freeMemory);
        }
        return responseBuilder.build();
    }

}