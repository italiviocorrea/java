package br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.domain.ports.outbound;

import br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.domain.models.ListaCertificadoRevogado;
import reactor.core.publisher.Mono;

public interface ListaCertificadoRevogadoRepositoryPort {

    Mono<ListaCertificadoRevogado> findStatusListaByUrl(String url);

    Mono<ListaCertificadoRevogado> findByUrlESerialNumber(String url, String serialNumber);

    Mono<ListaCertificadoRevogado> save(ListaCertificadoRevogado listaCertificadoRevogado);

}
