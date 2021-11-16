package eti.italiviocorrea.api.rsocket.lcr.application.usecases.rules.certificado.transmissor;

import com.hazelcast.map.IMap;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.AutoridadeCertificadora;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.DadosCertificado;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.RespostaValidacao;
import eti.italiviocorrea.api.rsocket.lcr.application.ports.inboud.AutoridadeCertificadoraQueryPort;
import io.opentelemetry.extension.annotations.WithSpan;
import org.springframework.util.ObjectUtils;

import java.security.cert.X509Certificate;
import java.util.function.Supplier;

public class RnA03Rej283 implements Supplier<RespostaValidacao> {

    private final RespostaValidacao resp283 = RespostaValidacao.builder().cStat("283")
            .xMotivo("Rejeicao: Certificado Transmissor - erro Cadeia de Certificacao").build();
    private final DadosCertificado dadosCertificado;
    private final IMap<X509Certificate, X509Certificate> cacheInvalido;
    private final AutoridadeCertificadoraQueryPort repository;

    public RnA03Rej283(DadosCertificado dadosCertificado,
                       IMap<X509Certificate, X509Certificate> cacheInvalido,
                       AutoridadeCertificadoraQueryPort repository) {
        this.dadosCertificado = dadosCertificado;
        this.cacheInvalido = cacheInvalido;
        this.repository = repository;
    }

    @WithSpan
    @Override
    public RespostaValidacao get() {

        if (ObjectUtils.isEmpty(dadosCertificado)) {
            return RespostaValidacao.resp999().trace("pre requisitos da regra").className(getClass().getName());
        }

        AutoridadeCertificadora ac = repository.findByNome(dadosCertificado.getNomeAC()).block();

        if (ObjectUtils.isEmpty(ac)) {
            cacheInvalido.put(dadosCertificado.getCertificate(),dadosCertificado.getCertificate());
            return resp283;
        }

        return RespostaValidacao.builder().ok(true).cStat("100").xMotivo("OK").build();
    }

}
