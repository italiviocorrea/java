package br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.domain.ports.outbound;

import br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.domain.models.AutoridadeCertificadora;
import reactor.core.publisher.Mono;

public interface AutoridadeCertificadoraRepositoryPort {

    Mono<AutoridadeCertificadora> findByNome(String nome);

}
