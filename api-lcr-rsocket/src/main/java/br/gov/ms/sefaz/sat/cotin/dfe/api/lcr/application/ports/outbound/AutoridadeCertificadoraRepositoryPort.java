package br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.application.ports.outbound;

import br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.application.domain.AutoridadeCertificadora;
import reactor.core.publisher.Mono;

public interface AutoridadeCertificadoraRepositoryPort {

    Mono<AutoridadeCertificadora> findByNome(String nome);

}
