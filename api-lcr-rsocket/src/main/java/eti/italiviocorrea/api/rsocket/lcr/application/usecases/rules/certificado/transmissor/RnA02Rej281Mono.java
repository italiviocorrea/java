package eti.italiviocorrea.api.rsocket.lcr.application.usecases.rules.certificado.transmissor;

import com.hazelcast.map.IMap;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.DadosCertificado;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.RespostaValidacao;
import io.opentelemetry.extension.annotations.WithSpan;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.function.Supplier;

public class RnA02Rej281Mono implements Supplier<Mono<RespostaValidacao>> {

    private final RespostaValidacao resp281 = RespostaValidacao.builder().cStat("281")
            .xMotivo("Rejeicao: Certificado Transmissor Data Validade").build();
    private final DadosCertificado certificado;
    private final IMap<X509Certificate, X509Certificate> cacheInvalido;

    public RnA02Rej281Mono(DadosCertificado certificado, IMap<X509Certificate, X509Certificate> cacheInvalido) {
        this.certificado = certificado;
        this.cacheInvalido = cacheInvalido;
    }

    @WithSpan
    @Override
    public Mono<RespostaValidacao> get() {

//        if (ObjectUtils.isEmpty(certificado)) {
//            return Mono.just(RespostaValidacao.resp999().trace("pre requisitos da regra").className(getClass().getName()));
//        }
//
//        try {
//            certificado.getCertificate().checkValidity(new Date());
//        } catch (CertificateExpiredException | CertificateNotYetValidException ex) {
//            // se for um certificado inválido colocar no cache de inválido
//            cacheInvalido.put(certificado.getCertificate(),certificado.getCertificate());
//            return Mono.just(resp281);
//        }
//
//        return Mono.just(RespostaValidacao.builder().ok(true).cStat("100").xMotivo("OK").build());

        if (ObjectUtils.isEmpty(certificado) || ObjectUtils.isEmpty(certificado.getCertificate())) {
            return Mono.just(RespostaValidacao.resp999().trace("pre requisitos da regra").className(getClass().getName()));
        }

        return Mono.just(certificado)
                .flatMap(dadosCertificado -> {

                    try {
                        certificado.getCertificate().checkValidity(new Date());
                    } catch (CertificateExpiredException | CertificateNotYetValidException ex) {
                        // se for um certificado inválido colocar no cache de inválido
                        cacheInvalido.put(certificado.getCertificate(),certificado.getCertificate());
                        return Mono.just(resp281);
                    }

                    return Mono.just(RespostaValidacao.builder().ok(true).cStat("100").xMotivo("OK").build());
                });
    }

}
