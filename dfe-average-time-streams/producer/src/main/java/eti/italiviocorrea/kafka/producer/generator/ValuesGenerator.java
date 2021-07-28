package eti.italiviocorrea.kafka.producer.generator;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.kafka.Record;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@ApplicationScoped
public class ValuesGenerator {

    private static final Logger log = Logger.getLogger((ValuesGenerator.class));
    private Random random = new Random();

    private List<NF3eWsService> services = Collections.unmodifiableList(
            Arrays.asList(
                    new NF3eWsService(1, "NF3eAutorizacao", 0)
            ));

    @Outgoing("nf3e-evt-aut")
    public Multi<Record<Integer, NF3eEventoMsg>> generateNF3eAutorizadaSinc() {
        return Multi.createFrom().ticks().every(Duration.ofMillis(500))
                .onOverflow().drop()
                .map(tick -> {
                    NF3eEventoMsg nf3eEventoMsg = new NF3eEventoMsg(
                            0,
                            OffsetDateTime.now(),
                            OffsetDateTime.now().plus(random.nextInt(10), ChronoUnit.MILLIS),
                            OffsetDateTime.now().plus(random.nextInt(100), ChronoUnit.MILLIS));
                    return Record.of(services.get(0).id, nf3eEventoMsg);

                });
    }

    @Outgoing("nf3e-evt-rej")
    public Multi<Record<Integer, NF3eEventoMsg>> generateNF3eRejeitada() {
        return Multi.createFrom().ticks().every(Duration.ofMillis(500))
                .onOverflow().drop()
                .map(tick -> {
                    Integer nRec = random.nextInt();
                    NF3eEventoMsg nf3eEventoMsg = new NF3eEventoMsg(
                            (nRec < 0) ? nRec * -1 : nRec,
                            OffsetDateTime.now(),
                            OffsetDateTime.now().plus(random.nextInt(10), ChronoUnit.MILLIS),
                            OffsetDateTime.now().plus(random.nextInt(100), ChronoUnit.MILLIS));
                    return Record.of(services.get(0).id, nf3eEventoMsg);
                });
    }

    @Outgoing("nf3e-ws-service")
    public Multi<Record<Integer, String>> nf3eServices() {
        return Multi.createFrom().items(services.stream()
                .map(s -> Record.of(
                        s.id,
                        "{ \"id\" : " + s.id +
                                ", \"name\" : \"" + s.name + "\" }"))
        );
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private static class NF3eEventoMsg {

        Integer nRec;
        OffsetDateTime dhRecbto;
        OffsetDateTime dhIniProc;
        OffsetDateTime dhFimProc;

        public NF3eEventoMsg(Integer nRec, OffsetDateTime dhRecbto, OffsetDateTime dhIniProc, OffsetDateTime dhFimProc) {
            this.nRec = nRec;
            this.dhRecbto = dhRecbto;
            this.dhIniProc = dhIniProc;
            this.dhFimProc = dhFimProc;
        }

        public Integer getnRec() {
            return nRec;
        }

        public void setnRec(Integer nRec) {
            this.nRec = nRec;
        }

        public OffsetDateTime getDhRecbto() {
            return dhRecbto;
        }

        public void setDhRecbto(OffsetDateTime dhRecbto) {
            this.dhRecbto = dhRecbto;
        }

        public OffsetDateTime getDhIniProc() {
            return dhIniProc;
        }

        public void setDhIniProc(OffsetDateTime dhIniProc) {
            this.dhIniProc = dhIniProc;
        }

        public OffsetDateTime getDhFimProc() {
            return dhFimProc;
        }

        public void setDhFimProc(OffsetDateTime dhFimProc) {
            this.dhFimProc = dhFimProc;
        }

//        @Override
//        public String toString() {
//            return "{" +
//                    "nRec=" + nRec +
//                    ", dhRecbto=" + dhRecbto +
//                    ", dhIniProc=" + dhIniProc +
//                    ", dhFimProc=" + dhFimProc +
//                    '}';
//        }
    }

    private static class NF3eWsService {

        int id;
        String name;
        int tempoMedio;

        public NF3eWsService(int id, String name, int tempoMedio) {
            this.id = id;
            this.name = name;
            this.tempoMedio = tempoMedio;
        }
    }

}
