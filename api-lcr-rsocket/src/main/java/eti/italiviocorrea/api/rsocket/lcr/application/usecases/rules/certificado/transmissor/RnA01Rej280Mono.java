package eti.italiviocorrea.api.rsocket.lcr.application.usecases.rules.certificado.transmissor;

import com.hazelcast.map.IMap;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.DadosCertificado;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.RespostaValidacao;
import io.opentelemetry.extension.annotations.WithSpan;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;
import reactor.util.Loggers;

import java.security.cert.X509Certificate;
import java.util.function.Supplier;

public class RnA01Rej280Mono implements Supplier<Mono<RespostaValidacao>> {

    private static final reactor.util.Logger LOGGER = Loggers.getLogger(CertificadoTransmissorSupervisor.class);

    private final RespostaValidacao resp280 = RespostaValidacao.builder().cStat("280")
            .xMotivo("Rejeicao: Certificado Transmissor Invalido").build();
    private final DadosCertificado dadosCertificado;
    private final IMap<X509Certificate, X509Certificate> cacheInvalido;

    public RnA01Rej280Mono(DadosCertificado dadosCertificado, IMap<X509Certificate, X509Certificate> cacheInvalido) {
        this.dadosCertificado = dadosCertificado;
        this.cacheInvalido = cacheInvalido;
    }

    @WithSpan
    @Override
    public Mono<RespostaValidacao> get() {

//        if (ObjectUtils.isEmpty(dadosCertificado)
//                || ObjectUtils.isEmpty(dadosCertificado.getCertificate())) {
//            return Mono.just(RespostaValidacao.resp999().trace("pre requisitos da regra").className(getClass().getName()));
//        }
//
//        if (dadosCertificado.getVersao() != 3
//                || dadosCertificado.getBasicConstraints() == 1
//                || !dadosCertificado.getIsClientAuthentication()) {
//            cacheInvalido.put(dadosCertificado.getCertificate(),dadosCertificado.getCertificate());
//            return Mono.just((resp280);
//        }
//        LOGGER.info("regra 280 OK.");
//        //return Mono.just(RespostaValidacao.builder().ok(true).cStat("100").xMotivo("OK").build());

        return Mono.just(dadosCertificado)
                .flatMap(dadosCertificado1 -> {
                    if( ObjectUtils.isEmpty(dadosCertificado1)
                            || ObjectUtils.isEmpty(dadosCertificado1.getCertificate())){
                        cacheInvalido.put(dadosCertificado.getCertificate(),dadosCertificado.getCertificate());
                        return Mono.just(resp280);
                    } else if (dadosCertificado.getVersao() != 3
                            || dadosCertificado.getBasicConstraints() == 1
                            || !dadosCertificado.getIsClientAuthentication()) {
                        cacheInvalido.put(dadosCertificado.getCertificate(),dadosCertificado.getCertificate());
                        return Mono.just(resp280);
                    }
                    LOGGER.info("regra 280 OK.");
                    return Mono.just(RespostaValidacao.builder().ok(true).cStat("100").xMotivo("OK").build());
                });
    }
}
