package eti.italiviocorrea.api.rsocket.lcr.adapters.configs;

import io.r2dbc.mssql.MssqlConnectionConfiguration;
import io.r2dbc.mssql.MssqlConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableR2dbcRepositories
@RequiredArgsConstructor
@EnableTransactionManagement
public class DFeDatabaseConfig extends AbstractR2dbcConfiguration {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private Environment env;

    @Bean
    @Override
    public MssqlConnectionFactory connectionFactory() {
        MssqlConnectionConfiguration config = MssqlConnectionConfiguration.builder()
                .host(env.getProperty("app.database.lcrdb.host"))
                .port(env.getProperty("app.database.lcrdb.port", Integer.class))
                .username(env.getProperty("app.database.lcrdb.username"))
                .password(getPassword(env.getProperty("app.database.lcrdb.password")))
                .database(env.getProperty("app.database.lcrdb.name"))
                .applicationName(env.getProperty("spring.application.name"))
                .sendStringParametersAsUnicode(false)
                .build();

        return new MssqlConnectionFactory(config);
    }

    @Bean
    public ReactiveTransactionManager transactionManager(MssqlConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }

    public String getPassword(String secret) {
        String password = "";
        try {
            Resource resource = new FileSystemResource(String.format("%s%s", env.getProperty("app.secrets.path"), secret));
            if (resource.exists()) {
                password = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8).trim();
            } else {
                password = System.getenv(secret.toUpperCase().replace("-", "_"));
            }

        } catch (IOException ex) {
            log.error(ex.toString(), ex);
        }
        return password;
    }

}
