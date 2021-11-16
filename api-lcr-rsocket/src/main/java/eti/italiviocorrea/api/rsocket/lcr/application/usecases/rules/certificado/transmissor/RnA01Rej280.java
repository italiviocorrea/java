package eti.italiviocorrea.api.rsocket.lcr.application.usecases.rules.certificado.transmissor;

import com.hazelcast.map.IMap;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.DadosCertificado;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.RespostaValidacao;
import io.opentelemetry.extension.annotations.WithSpan;
import org.springframework.util.ObjectUtils;
import reactor.util.Loggers;

import java.security.cert.X509Certificate;
import java.util.function.Supplier;

public class RnA01Rej280 implements Supplier<RespostaValidacao> {

    private static final reactor.util.Logger LOGGER = Loggers.getLogger(CertificadoTransmissorSupervisor.class);

    private final RespostaValidacao resp280 = RespostaValidacao.builder().cStat("280")
            .xMotivo("Rejeicao: Certificado Transmissor Invalido").build();
    private final DadosCertificado dadosCertificado;
    private final IMap<X509Certificate, X509Certificate> cacheInvalido;

    public RnA01Rej280(DadosCertificado dadosCertificado, IMap<X509Certificate, X509Certificate> cacheInvalido) {
        this.dadosCertificado = dadosCertificado;
        this.cacheInvalido = cacheInvalido;
    }

    @WithSpan
    @Override
    public RespostaValidacao get() {

        if (ObjectUtils.isEmpty(dadosCertificado)
                || ObjectUtils.isEmpty(dadosCertificado.getCertificate())) {
            return RespostaValidacao.resp999().trace("pre requisitos da regra").className(getClass().getName());
        }

        if (dadosCertificado.getVersao() != 3
                || dadosCertificado.getBasicConstraints() == 1
                || !dadosCertificado.getIsClientAuthentication()) {
            cacheInvalido.put(dadosCertificado.getCertificate(),dadosCertificado.getCertificate());
            return resp280;
        }
        LOGGER.info("regra 280 OK.");
        return RespostaValidacao.builder().ok(true).cStat("100").xMotivo("OK").build();
    }
}
