package eti.italiviocorrea.api.rsocket.lcr.application.usecases.rules.certificado.transmissor;

import com.hazelcast.map.IMap;
import eti.italiviocorrea.api.rsocket.lcr.adapters.utils.ICPBrasilUtil;
import eti.italiviocorrea.api.rsocket.lcr.adapters.utils.X509CertificateWrapper;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.DadosCertificado;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.RespostaValidacao;
import io.opentelemetry.extension.annotations.WithSpan;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

import java.security.cert.X509Certificate;
import java.util.function.Supplier;

public class RnA06Rej285Mono implements Supplier<Mono<RespostaValidacao>> {

    private final RespostaValidacao resp285 = RespostaValidacao.builder().cStat("285")
            .xMotivo("Rejeicao: Certificado Transmissor difere ICP-Brasil").build();
    private final DadosCertificado dadosCertificado;
    private final IMap<X509Certificate, X509Certificate> cacheInvalido;

    public RnA06Rej285Mono(DadosCertificado dadosCertificado1, IMap<X509Certificate, X509Certificate> cacheInvalido) {
        this.dadosCertificado = dadosCertificado1;
        this.cacheInvalido = cacheInvalido;
    }

    @WithSpan
    @Override
    public Mono<RespostaValidacao> get() {

        if (ObjectUtils.isEmpty(dadosCertificado) || ObjectUtils.isEmpty(dadosCertificado.getCertificate())) {
            return Mono.just(RespostaValidacao.resp999().trace("pre requisitos da regra").className(getClass().getName()));
        }

        return Mono.just(dadosCertificado)
                .flatMap(dadosCertificado1 -> {
                    try {
                        if (!ICPBrasilUtil.isIcpBrasil(new X509CertificateWrapper(dadosCertificado.getCertificate()))) {
                            cacheInvalido.put(dadosCertificado.getCertificate(), dadosCertificado.getCertificate());
                            return Mono.just(resp285);
                        }
                    } catch (Exception ex) {
                        return Mono.just(resp285.className(getClass().getName()).trace("falha ao verificar se é ICPBrasil").throwable(ex));
                    }
                    return Mono.just(RespostaValidacao.builder().ok(true).cStat("100").xMotivo("OK").build());
                });
    }
}
