package eti.italiviocorrea.api.rsocket.lcr.application.usecases;

import eti.italiviocorrea.api.rsocket.lcr.application.domain.AcPossuiLcr;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.AutoridadeCertificadora;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.ListaCertificadoRevogado;
import eti.italiviocorrea.api.rsocket.lcr.application.ports.inboud.AutoridadeCertificadoraQueryPort;
import eti.italiviocorrea.api.rsocket.lcr.application.ports.inboud.ListaCertificadoRevogadoUseCasePort;
import eti.italiviocorrea.api.rsocket.lcr.application.ports.outbound.AcPossuiLcrCommandPort;
import eti.italiviocorrea.api.rsocket.lcr.application.ports.outbound.ListaCertificadoRevogadoCommandPort;
import io.opentelemetry.extension.annotations.WithSpan;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

public class ListaCertificadoRevogadoUseCase implements ListaCertificadoRevogadoUseCasePort {

    private final AutoridadeCertificadoraQueryPort acRepository;
    private final ListaCertificadoRevogadoCommandPort lcrRepository;
    private final AcPossuiLcrCommandPort acPossuiLcrRepository;

    public ListaCertificadoRevogadoUseCase(AutoridadeCertificadoraQueryPort acRepository,
                                           ListaCertificadoRevogadoCommandPort lcrRepository,
                                           AcPossuiLcrCommandPort acPossuiLcrRepository) {
        this.acRepository = acRepository;
        this.lcrRepository = lcrRepository;
        this.acPossuiLcrRepository = acPossuiLcrRepository;
    }

    @WithSpan
    @Transactional
    @Override
    public Mono<Void> incluirLcr(String nomeAC, String urlLcr) {

        Mono<AutoridadeCertificadora> ac =
                acRepository
                        .findByNome(nomeAC)
                        .switchIfEmpty(Mono.error(new Exception("AC nao encontrada!")));

        Mono<ListaCertificadoRevogado> lcr = this.createLcr(urlLcr)
                .flatMap(listaCertificadoRevogado -> lcrRepository.save(listaCertificadoRevogado))
                .onErrorMap(ex -> new Exception("LCR nao pode ser incluida!"));

        return Mono.zip(ac, lcr, (AC, LCR) ->
                        AcPossuiLcr.builder()
                                .iditc(AC.getIditc())
                                .idlcr(LCR.getId())
                                .build())
                .flatMap(acPossuiLcr ->
                        this.acPossuiLcrRepository
                                .inserirAcPossuiLcr(acPossuiLcr.getIditc(),
                                        acPossuiLcr.getIdlcr()))
                .then();
    }

    /**
     * @param urlLcr
     * @return
     */
    private Mono<ListaCertificadoRevogado> createLcr(String urlLcr) {
        return Mono.just(ListaCertificadoRevogado.builder()
                .infoUrlLcr(urlLcr)
                .indiLcrDelta(0)
                .indiAtualzLcr("S")
                .build());
    }

}
