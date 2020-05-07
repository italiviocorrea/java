package eti.italiviocorrea.paises.service;

import eti.italiviocorrea.paises.model.Paises;
import eti.italiviocorrea.paises.model.PaisesResposta;
import eti.italiviocorrea.paises.util.DBSemConexaoException;
import eti.italiviocorrea.paises.util.MyAbstractLogging;
import io.smallrye.mutiny.TimeoutException;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.opentracing.Traced;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

@Traced
@ApplicationScoped
public class PaisesService extends MyAbstractLogging {

    /**
     * Injeta o valor da propridade api_paises.schema.create que encontra-se no arquivo :
     * application.config na variável schemaCreate.
     */
    @Inject
    @ConfigProperty(name = "api_paises.schema.create", defaultValue = "true")
    boolean schemaCreate;

    /**
     * Injeta o cliente do banco de dados postgresql.
     */
    @Inject
    PgPool client;

    @PostConstruct
    void config() {
        if (schemaCreate) {
            initdb();
        }
    }

    /**
     * Cria a tabela paises no banco de dados paisesdb, caso a tabela não exista no banco de
     * dados.
     */
    @Timeout(100)
    @Retry(retryOn = {RuntimeException.class, TimeoutException.class, DBSemConexaoException.class},
            maxRetries = 4)
    @CircuitBreaker(failOn = {RuntimeException.class, TimeoutException.class, DBSemConexaoException.class},
            successThreshold = 5,
            requestVolumeThreshold = 4,
            failureRatio = 0.75,
            delay = 1000)
    private void initdb() {
        writeMessageLogging("Criando a tabela paises no banco de dados, caso ela não exista");
        client.query("CREATE TABLE IF NOT EXISTS paises (id INT, nome TEXT NOT NULL, sigla TEXT NOT NULL, PRIMARY KEY (id))")
                .await().indefinitely();
    }

    /**
     * Busca todos os países cadastrados no banco de dados.
     *
     * @return
     */
    @Timeout(100)
    @Retry(retryOn = {RuntimeException.class, TimeoutException.class, DBSemConexaoException.class},
            maxRetries = 4)
    @CircuitBreaker(failOn = {RuntimeException.class, TimeoutException.class, DBSemConexaoException.class},
            successThreshold = 5,
            requestVolumeThreshold = 4,
            failureRatio = 0.75,
            delay = 1000)
    public Uni<PaisesResposta> buscarTodos(short offset, short limit) {
        writeMessageLogging("Listando países : offset = " + offset + " e Limit = " + limit);
        // executa a consulta
        return client.preparedQuery("SELECT id, nome, sigla FROM paises ORDER BY id ASC OFFSET $1 LIMIT $2", Tuple.of(offset - 1, limit))
                .onItem().apply(paises -> {
                    writeMessageLogging("Total de países encontrado : " + paises.rowCount());
                    PaisesResposta paisesResposta = new PaisesResposta();
                    for (Row row : paises) {
                        paisesResposta.paises.add(from(row));
                    }
                    return paisesResposta;
                });
    }

    /**
     * Busca um país no banco de dados.
     *
     * @param id Identificador do país a localizado no banco de dados.
     * @return
     */
    @Timeout(100)
    @Retry(retryOn = {TimeoutException.class, DBSemConexaoException.class},
            maxRetries = 4)
    @CircuitBreaker(failOn = {TimeoutException.class, DBSemConexaoException.class},
            successThreshold = 5,
            requestVolumeThreshold = 4,
            failureRatio = 0.75,
            delay = 1000)
    public Uni<Paises> buscarPorId(Long id) {

        return client.preparedQuery("SELECT id, nome, sigla FROM paises WHERE id = $1", Tuple.of(id))
                .onItem().apply(RowSet::iterator)
                .onItem().apply(iterator -> iterator.hasNext() ? from(iterator.next()) : null);
    }

    /**
     * Inclui um país no banco de dados
     *
     * @param paises Informações do país a ser incluído no banco de dados
     * @return
     */
    @Timeout(100)
    @Retry(retryOn = {RuntimeException.class, TimeoutException.class, DBSemConexaoException.class},
            maxRetries = 4)
    @CircuitBreaker(failOn = {RuntimeException.class, TimeoutException.class, DBSemConexaoException.class},
            successThreshold = 5,
            requestVolumeThreshold = 4,
            failureRatio = 0.75,
            delay = 1000)
    public Uni<Boolean> incluir(@NotNull Paises paises) {
        return client
                .preparedQuery("INSERT INTO paises (id, nome, sigla) VALUES($1,$2,$3)",
                        Tuple.of(paises.id, paises.nome, paises.sigla))
                .onItem().apply(pgRowSet -> pgRowSet.rowCount() == 1);
    }

    /**
     * Metódo para atualizar os dados do país informado.
     *
     * @param paises Dados do país a ser modificado
     * @return
     */
    @Timeout(100)
    @Retry(retryOn = {RuntimeException.class, TimeoutException.class, DBSemConexaoException.class},
            maxRetries = 4)
    @CircuitBreaker(failOn = {RuntimeException.class, TimeoutException.class, DBSemConexaoException.class},
            successThreshold = 5,
            requestVolumeThreshold = 4,
            failureRatio = 0.75,
            delay = 1000)
    public Uni<Boolean> atualizar(@NotNull Paises paises, Long id) {
        return client
                .preparedQuery("UPDATE paises SET nome = $1, sigla = $2 WHERE id = $3",
                        Tuple.of(paises.nome, paises.sigla, id))
                .onItem().apply(pgRowSet -> pgRowSet.rowCount() == 1);
    }

    /**
     * Remove o país do banco de dados.
     *
     * @param id Idenficador do país a ser removido
     * @return
     */
    @Timeout(250)
    @Retry(retryOn = {RuntimeException.class, TimeoutException.class, DBSemConexaoException.class},
            maxRetries = 4)
    @CircuitBreaker(failOn = {RuntimeException.class, TimeoutException.class, DBSemConexaoException.class},
            successThreshold = 5,
            requestVolumeThreshold = 4,
            failureRatio = 0.75,
            delay = 1000)
    public Uni<Boolean> remover(Long id) {
        return client.preparedQuery("DELETE FROM paises WHERE id = $1", Tuple.of(id))
                .onItem().apply(pgRowSet -> pgRowSet.rowCount() == 1);
    }

}
