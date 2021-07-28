package eti.italiviocorrea.kafka.consumer.model;

import java.time.OffsetDateTime;

public class NF3eEventoMsg {

    Integer nRec;
    OffsetDateTime dhRecbto;
    OffsetDateTime dhIniProc;
    OffsetDateTime dhFimProc;

    public void setnRec(Integer nRec) {
        this.nRec = nRec;
    }

    public void setDhRecbto(OffsetDateTime dhRecbto) {
        this.dhRecbto = dhRecbto;
    }

    public void setDhIniProc(OffsetDateTime dhIniProc) {
        this.dhIniProc = dhIniProc;
    }

    public void setDhFimProc(OffsetDateTime dhFimProc) {
        this.dhFimProc = dhFimProc;
    }

    public NF3eEventoMsg(Integer nRec, OffsetDateTime dhRecbto, OffsetDateTime dhIniProc, OffsetDateTime dhFimProc) {
        this.nRec = nRec;
        this.dhRecbto = dhRecbto;
        this.dhIniProc = dhIniProc;
        this.dhFimProc = dhFimProc;
    }

    @Override
    public String toString() {
        return "{" +
                "nRec=" + nRec +
                ", dhRecbto=" + dhRecbto +
                ", dhIniProc=" + dhIniProc +
                ", dhFimProc=" + dhFimProc +
                '}';
    }

}
