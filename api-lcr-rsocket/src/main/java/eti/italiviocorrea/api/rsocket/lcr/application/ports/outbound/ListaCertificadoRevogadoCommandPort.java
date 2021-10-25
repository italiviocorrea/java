package eti.italiviocorrea.api.rsocket.lcr.application.ports.outbound;

import eti.italiviocorrea.api.rsocket.lcr.application.domain.ListaCertificadoRevogado;
import reactor.core.publisher.Mono;

public interface ListaCertificadoRevogadoCommandPort {

    Mono<ListaCertificadoRevogado> findStatusListaByUrl(String url);

    Mono<ListaCertificadoRevogado> findByUrlESerialNumber(String url, String serialNumber);

    Mono<ListaCertificadoRevogado> save(ListaCertificadoRevogado listaCertificadoRevogado);

}
