package eti.italiviocorrea.api.rsocket.lcr.application.domain;

/**
 *
 * @author gelson
 */
public enum ResultadoProcessamento {
    RP_280("280", "Rejeicao: Certificado Transmissor Invalido"),
    RP_290("290", "Rejeicao: Certificado Assinatura invalido"),
    RP_999("999", "Rejeicao: Erro nao catalogado");

    private final String cStat;
    private final String xMotivo;

    private ResultadoProcessamento(String cStat, String xMotivo) {
        this.cStat = cStat;
        this.xMotivo = xMotivo;
    }

    public String getCStat() {
        return cStat;
    }

    public String getXMotivo() {
        return xMotivo;
    }

}
