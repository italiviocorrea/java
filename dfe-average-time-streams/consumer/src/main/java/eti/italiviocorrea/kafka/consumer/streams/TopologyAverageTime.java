package eti.italiviocorrea.kafka.consumer.streams;

import eti.italiviocorrea.kafka.consumer.model.NF3eEventoMsg;
import eti.italiviocorrea.kafka.consumer.model.NF3eTempoMedio;
import eti.italiviocorrea.kafka.consumer.model.NF3eTempoMedioAcumulado;
import eti.italiviocorrea.kafka.consumer.util.serde.StreamsSerdes;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.Stores;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class TopologyAverageTime {

    private static final String NF3E_AUTORIZADO_VALUES_TOPIC = "nf3e-evt-aut";
    private static final String NF3E_REJEITADO_VALUES_TOPIC = "nf3e-evt-rej";
    private static final String NF3E_SERVICES_TOPIC = "nf3e-ws-service";
    private static final String NF3E_SERVICES_STORE = "nf3e-tempo-medio-store";

    @Produces
    public Topology gerarTopologia() {

        StreamsBuilder builder = new StreamsBuilder();

        KeyValueBytesStoreSupplier storeSupplier = Stores.persistentKeyValueStore(
                NF3E_SERVICES_STORE);

        // Consume os topicos de NF3e autorizadas
        KStream<Integer, NF3eEventoMsg> nf3eAutKS =
                builder.stream(NF3E_AUTORIZADO_VALUES_TOPIC,
                        Consumed.with(Serdes.Integer(), StreamsSerdes.NF3eEventoMsgSerde()));

        // Consume os topicos de NF3e rejeitadas
        KStream<Integer, NF3eEventoMsg> nf3eRejKS =
                builder.stream(NF3E_REJEITADO_VALUES_TOPIC,
                        Consumed.with(Serdes.Integer(), StreamsSerdes.NF3eEventoMsgSerde()));

        // Cria um novo kafka stream com o merge da NF3e autorizadas e rejeitadas
        KStream<Integer, NF3eEventoMsg> nf3eMergeKS = nf3eAutKS.merge(nf3eRejKS);

        // Deriva um kafka stream calculando o tempo médio de cada NF3e
        KStream<Integer, NF3eTempoMedio> nf3eTempoMedioKS =
                nf3eMergeKS.mapValues(nF3eEventoMsg -> NF3eTempoMedio.builder(nF3eEventoMsg).build());

        // Calcula o tempo médio agregado (Falta criar um processador com Window para agregar somente os últimos 5 minutos)
        KStream<Integer, NF3eTempoMedioAcumulado> nf3eTempoMedioAgregadoKS =
                nf3eTempoMedioKS.groupByKey()
                        .aggregate(NF3eTempoMedioAcumulado::new,
                                (serviceId, value, aggregation) -> aggregation.updateFrom(value),
                                Materialized.<Integer, NF3eTempoMedioAcumulado>as(storeSupplier)
                                        .withKeySerde(Serdes.Integer())
                                        .withValueSerde(StreamsSerdes.aggregationSerde)
                        )
                        .toStream();

        nf3eMergeKS.print(Printed.<Integer, NF3eEventoMsg>toSysOut().withLabel("NF3eEventoMsg"));
        nf3eTempoMedioKS.print(Printed.<Integer, NF3eTempoMedio>toSysOut().withLabel("NF3eTempoMedio"));
        nf3eTempoMedioAgregadoKS.print(Printed.<Integer, NF3eTempoMedioAcumulado>toSysOut().withLabel("NF3eTempoMedioAgregado"));

        return builder.build();
    }
}
