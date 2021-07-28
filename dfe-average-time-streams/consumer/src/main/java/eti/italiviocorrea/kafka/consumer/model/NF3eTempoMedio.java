package eti.italiviocorrea.kafka.consumer.model;

import java.time.Duration;
import java.util.Objects;

public class NF3eTempoMedio {

    private Integer id;
    private String serviceName;
    private Integer tempoMedio;

    private NF3eTempoMedio(Builder builder) {
        id = builder.id;
        serviceName = builder.serviceName;
        tempoMedio = builder.tempoMedio;
    }

    public static Builder builder(NF3eEventoMsg nf3eEventoMsg) {
        NF3eTempoMedio.Builder builder = new NF3eTempoMedio.Builder();
        Duration difference = Duration.between(nf3eEventoMsg.dhFimProc,nf3eEventoMsg.dhIniProc);
        builder.id(1);
        builder.serviceName("NF3eAutorizacao");
        builder.tempoMedio((difference.toMillisPart() > 0) ? difference.toMillisPart() : 1);
        return builder;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Integer getTempoMedio() {
        return tempoMedio;
    }

    public void setTempoMedio(Integer tempoMedio) {
        this.tempoMedio = tempoMedio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NF3eTempoMedio that = (NF3eTempoMedio) o;
        return id.equals(that.id) && serviceName.equals(that.serviceName) && tempoMedio.equals(that.tempoMedio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serviceName, tempoMedio);
    }

    @Override
    public String toString() {
        return "NF3eTempoMedio{" +
                "id=" + id +
                ", serviceName='" + serviceName + '\'' +
                ", tempoMedio=" + tempoMedio +
                '}';
    }

    public static final class Builder {

        private Integer id;
        private String serviceName;
        private Integer tempoMedio;

        public Builder() {
        }

        public Builder(NF3eTempoMedio nf3eTempoMedio) {
            this.id = nf3eTempoMedio.getId();
            this.serviceName = nf3eTempoMedio.getServiceName();
            this.tempoMedio = nf3eTempoMedio.getTempoMedio();
        }

        public Builder id(Integer val) {
            id = val;
            return this;
        }

        public Builder serviceName(String val) {
            serviceName = val;
            return this;
        }

        public Builder tempoMedio(Integer val) {
            tempoMedio = val;
            return this;
        }

        public NF3eTempoMedio build() {
            return new NF3eTempoMedio(this);
        }

    }
}
