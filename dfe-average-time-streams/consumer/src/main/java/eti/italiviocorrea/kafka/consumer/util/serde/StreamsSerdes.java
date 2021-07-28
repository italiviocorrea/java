package eti.italiviocorrea.kafka.consumer.util.serde;

import eti.italiviocorrea.kafka.consumer.model.NF3eEventoMsg;
import eti.italiviocorrea.kafka.consumer.model.NF3eTempoMedio;
import eti.italiviocorrea.kafka.consumer.model.NF3eTempoMedioAcumulado;
import eti.italiviocorrea.kafka.consumer.model.NF3eWsService;
import eti.italiviocorrea.kafka.consumer.util.serializer.JsonDeserializer;
import eti.italiviocorrea.kafka.consumer.util.serializer.JsonSerializer;
import io.quarkus.kafka.client.serialization.ObjectMapperSerde;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class StreamsSerdes {


    public static Serde<NF3eEventoMsg> NF3eEventoMsgSerde() {
        return new NF3eEventoMsgSerde();
    }

    public static ObjectMapperSerde<NF3eWsService> NF3eWsServiceSerde = new ObjectMapperSerde<>(NF3eWsService.class);
    public static ObjectMapperSerde<NF3eTempoMedio> NF3eTempoMedioSerde = new ObjectMapperSerde<>(NF3eTempoMedio.class);
    public static ObjectMapperSerde<NF3eTempoMedioAcumulado> aggregationSerde = new ObjectMapperSerde<>(NF3eTempoMedioAcumulado.class);

    public static final class NF3eEventoMsgSerde extends WrapperSerde<NF3eEventoMsg> {
        public NF3eEventoMsgSerde() {
            super(new JsonSerializer<>(), new JsonDeserializer<>(NF3eEventoMsg.class));
        }
    }


    private static class WrapperSerde<T> implements Serde<T> {

        private JsonSerializer<T> serializer;
        private JsonDeserializer<T> deserializer;

        WrapperSerde(JsonSerializer<T> serializer, JsonDeserializer<T> deserializer) {
            this.serializer = serializer;
            this.deserializer = deserializer;
        }

        @Override
        public void configure(Map<String, ?> map, boolean b) {

        }

        @Override
        public void close() {

        }

        @Override
        public Serializer<T> serializer() {
            return serializer;
        }

        @Override
        public Deserializer<T> deserializer() {
            return deserializer;
        }
    }
}
