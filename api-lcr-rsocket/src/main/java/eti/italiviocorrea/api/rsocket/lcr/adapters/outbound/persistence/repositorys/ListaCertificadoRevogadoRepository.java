package eti.italiviocorrea.api.rsocket.lcr.adapters.outbound.persistence.repositorys;

import eti.italiviocorrea.api.rsocket.lcr.adapters.outbound.persistence.r2dbc.MssqlListaCertificadoRevogadoRepository;
import eti.italiviocorrea.api.rsocket.lcr.adapters.outbound.persistence.r2dbc.entities.ListaCertificadoRevogadoEntity;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.ListaCertificadoRevogado;
import eti.italiviocorrea.api.rsocket.lcr.application.ports.outbound.ListaCertificadoRevogadoRepositoryPort;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Primary
public class ListaCertificadoRevogadoRepository implements ListaCertificadoRevogadoRepositoryPort {

    private final MssqlListaCertificadoRevogadoRepository repository;


    public ListaCertificadoRevogadoRepository(MssqlListaCertificadoRevogadoRepository repository) {
        this.repository = repository;
    }

    @Autowired
    ModelMapper modelMapper;

    @Override
    public Mono<ListaCertificadoRevogado> findStatusListaByUrl(String url) {
        return repository.findStatusListaByUrl(url)
                .map(entity -> modelMapper.map(entity, ListaCertificadoRevogado.class));
    }

    @Override
    public Mono<ListaCertificadoRevogado> findByUrlESerialNumber(String url, String serialNumber) {
        return repository.findByUrlESerialNumber(url, serialNumber)
                .map(entity -> modelMapper.map(entity, ListaCertificadoRevogado.class));
    }

    public Mono<ListaCertificadoRevogado> save(ListaCertificadoRevogado listaCertificadoRevogado) {
        return repository.save(modelMapper.map(listaCertificadoRevogado, ListaCertificadoRevogadoEntity.class))
                .map(entity -> modelMapper.map(entity, ListaCertificadoRevogado.class));
    }

}
