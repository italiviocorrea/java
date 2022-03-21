package eti.italiviocorrea.api.paymentmethodsserver.adapters.config.database.payments.datasource;

import io.r2dbc.mssql.MssqlConnectionConfiguration;
import io.r2dbc.mssql.MssqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.SqlServerDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.util.Logger;
import reactor.util.Loggers;

@Configuration
@EnableR2dbcRepositories(entityOperationsRef = "paymentsEntityOperations",
        basePackages = "eti.italiviocorrea.api.paymentmethodsserver.adapters.config.database.payments.repository")
public class DataSourceConfig {

    private static final Logger log = Loggers.getLogger(DataSourceConfig.class);

    @Value("${app.database.payments.host}")
    private String host;

    @Value("${app.database.payments.port}")
    private Integer port;

    @Value("${app.database.payments.password}")
    private String pwd;

    @Value("${app.database.payments.username}")
    private String userName;

    @Value("${app.database.payments.name}")
    private String database;

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    @Qualifier("paymentsConnectionFactory")
    public ConnectionFactory connectionFactory() {
        MssqlConnectionConfiguration config = MssqlConnectionConfiguration.builder()
                .host(host)
                .port(port)
                .username(userName)
                .password(pwd)
                .database(database)
                .applicationName(applicationName)
                .sendStringParametersAsUnicode(false)
                .build();

        return new MssqlConnectionFactory(config);
    }

    @Bean
    public R2dbcEntityOperations paymentsEntityOperations(@Qualifier("paymentsConnectionFactory") ConnectionFactory connectionFactory) {
        DatabaseClient databaseClient = DatabaseClient.create(connectionFactory);
        return new R2dbcEntityTemplate(databaseClient, SqlServerDialect.INSTANCE);
    }
}
