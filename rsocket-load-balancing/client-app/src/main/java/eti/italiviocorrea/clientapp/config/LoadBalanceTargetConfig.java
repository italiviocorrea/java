package eti.italiviocorrea.clientapp.config;

import io.rsocket.loadbalance.LoadbalanceTarget;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class LoadBalanceTargetConfig {

    @Autowired
    private DummyServiceRegistry serviceRegistry;

    @Bean
    public Flux<List<LoadbalanceTarget>> targets() {
        return Mono.fromSupplier(() -> healthyServer(serviceRegistry.getServers()))
                .repeatWhen(longFlux -> longFlux.delayElements(Duration.ofSeconds(2)))
                .map(this::toLoadBalanceTarget);

    }

    private List<LoadbalanceTarget> toLoadBalanceTarget(List<RSocketServerInstance> rSocketServers) {
        return rSocketServers.stream()
                .map(server -> LoadbalanceTarget.from(server.getHost() + server.getPort(), TcpClientTransport.create(server.getHost(), server.getPort())))
                .collect(Collectors.toList());
    }

    /**
     * Filtra os servidores disponiveis e saudáveis.
     * @param servers
     * @return
     */
    private List<RSocketServerInstance> healthyServer(List<RSocketServerInstance> servers) {
        List<RSocketServerInstance> instances = new ArrayList<>();
        for (RSocketServerInstance rSocketServerInstance : servers) {
            try {
                if (healthCheck(rSocketServerInstance))
                    instances.add(rSocketServerInstance);
            } catch (IOException e) {

            }
        }
        return instances;
    }

    /**
     * Checa se um servidor esta saúdavel
     * @param rSocketServerInstance
     * @return
     * @throws IOException
     */
    private boolean healthCheck(RSocketServerInstance rSocketServerInstance) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        boolean result = false;

        try {

            HttpGet request = new HttpGet("http://"+rSocketServerInstance.getHost() + ":" + rSocketServerInstance.getPortHttp() + "/actuator/health");

            CloseableHttpResponse response = httpClient.execute(request);

            try {
                HttpEntity entity = response.getEntity();
                if (response.getStatusLine().getStatusCode() == 200
                        && entity != null) {
                    result = true;
                }
            } finally {
                response.close();
            }
        } finally {
            httpClient.close();
        }

        return result;
    }

}
