package eti.italiviocorrea.paises.util;

import eti.italiviocorrea.paises.model.Paises;
import io.vertx.mutiny.sqlclient.Row;
import org.eclipse.microprofile.faulttolerance.Asynchronous;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.jboss.logging.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class MyAbstractLogging {

    private static final Logger LOG = Logger.getLogger(MyAbstractResource.class);

    // métricas
    @Timeout(250)
    @Counted(description = "Total de gravações de logs", absolute = true, name = "qtd-gravar-logs")
    @Timed(name = "tempo-gravar-log", description = "Quanto tempo leva para gravar um log", unit = MetricUnits.MILLISECONDS)
    @Asynchronous
    @Bulkhead(value = 125, waitingTaskQueue = 100)
    protected Future writeMessageLogging(String item) {
        LOG.info(item);
        return CompletableFuture.completedFuture("ok");
    }

    // Metodos de apoio
    protected static Paises from(Row row) {
        return new Paises(row.getLong("id"),
                row.getString("nome"),
                row.getString("sigla"));
    }

}
