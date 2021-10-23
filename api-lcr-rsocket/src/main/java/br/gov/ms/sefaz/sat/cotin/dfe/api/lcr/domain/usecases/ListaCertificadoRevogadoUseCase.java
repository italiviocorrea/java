package br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.domain.usecases;

import br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.domain.models.AcPossuiLcr;
import br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.domain.models.AutoridadeCertificadora;
import br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.domain.models.ListaCertificadoRevogado;
import br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.domain.ports.inboud.ListaCertificadoRevogadoUseCasePort;
import br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.domain.ports.outbound.AcPossuiLcrRepositoryPort;
import br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.domain.ports.outbound.AutoridadeCertificadoraRepositoryPort;
import br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.domain.ports.outbound.ListaCertificadoRevogadoRepositoryPort;
import reactor.core.publisher.Mono;

public class ListaCertificadoRevogadoUseCase implements ListaCertificadoRevogadoUseCasePort {

    private final AutoridadeCertificadoraRepositoryPort acRepository;
    private final ListaCertificadoRevogadoRepositoryPort lcrRepository;
    private final AcPossuiLcrRepositoryPort acPossuiLcrRepository;

    public ListaCertificadoRevogadoUseCase(AutoridadeCertificadoraRepositoryPort acRepository,
                                           ListaCertificadoRevogadoRepositoryPort lcrRepository,
                                           AcPossuiLcrRepositoryPort acPossuiLcrRepository) {
        this.acRepository = acRepository;
        this.lcrRepository = lcrRepository;
        this.acPossuiLcrRepository = acPossuiLcrRepository;
    }

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
