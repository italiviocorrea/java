package br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.repositorio;

import br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.entities.Projeto;
import br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.usecase.ports.ProjetoQueryRepositoryPort;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProjetoQueryRepositoyPortImpl implements ProjetoQueryRepositoryPort {


    @Autowired
    private ProjetoRepositorio query;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public Optional<Projeto> findByNome(String nome) {
        return query.findByNome(nome).map(entity -> modelMapper.map(entity, Projeto.class));
    }

    @Override
    public List<Projeto> findAllProjetos() {
        return query.findAll()
                .stream()
                .map(projeto -> modelMapper.map(projeto, Projeto.class))
                .collect(Collectors.toList());
    }

}
