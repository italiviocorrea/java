package eti.italiviocorrea.api.rsocket.lcr.application.usecases.rules.certificado.transmissor;

import com.hazelcast.map.IMap;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.DadosCertificado;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.RespostaValidacao;
import eti.italiviocorrea.api.rsocket.lcr.application.ports.outbound.ListaCertificadoRevogadoCommandPort;
import io.opentelemetry.extension.annotations.WithSpan;
import org.springframework.util.ObjectUtils;

import java.security.cert.X509Certificate;
import java.util.function.Supplier;

public class RnA05Rej284 implements Supplier<RespostaValidacao> {

    private final RespostaValidacao resp284 = RespostaValidacao.builder().cStat("284")
            .xMotivo("Rejeicao: Certificado Transmissor revogado").build();
    private final DadosCertificado dadosCertificado;
    private final IMap<X509Certificate, X509Certificate> cacheInvalido;
    private final ListaCertificadoRevogadoCommandPort repository;

    public RnA05Rej284(DadosCertificado dadosCertificado,
                       IMap<X509Certificate, X509Certificate> cacheInvalido,
                       ListaCertificadoRevogadoCommandPort repository) {
        this.dadosCertificado = dadosCertificado;
        this.cacheInvalido = cacheInvalido;
        this.repository = repository;
    }

    @WithSpan
    @Override
    public RespostaValidacao get() {

        if (ObjectUtils.isEmpty(dadosCertificado)
                || ObjectUtils.isEmpty(repository)) {
            return RespostaValidacao.resp999().trace("pre requisitos da regra").className(getClass().getName());
        }

        RespostaValidacao resp = RespostaValidacao.builder().ok(true).cStat("100").xMotivo("OK").build();

        dadosCertificado.getPontosDistribuicaoLCR().parallelStream().forEach(url -> {
            if (repository.findByUrlESerialNumber(url, dadosCertificado.getSerialNumber()).block() != null) {
                resp.set(inserirInvalido(dadosCertificado));
            }
        });

        return resp;
    }

    private RespostaValidacao inserirInvalido(DadosCertificado dadosCertificado) {
        cacheInvalido.put(dadosCertificado.getCertificate(), dadosCertificado.getCertificate());
        return resp284;
    }

}
