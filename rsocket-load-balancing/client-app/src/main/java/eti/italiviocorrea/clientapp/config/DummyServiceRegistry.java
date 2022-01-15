package eti.italiviocorrea.clientapp.config;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@ConfigurationProperties(prefix = "rsocket.square-service")
public class DummyServiceRegistry {

    private List<RSocketServerInstance> servers;

    public void setServers(List<RSocketServerInstance> servers) {
        this.servers = servers;
    }

    public List<RSocketServerInstance> getServers() {
        return this.servers;
    }


}
