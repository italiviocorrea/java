package br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.repositorio;

import br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.entities.Projeto;
import br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.repositorio.model.ProjetoModel;
import br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.usecase.ports.ProjetoCommandRepositoryPort;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class ProjetoCommandRepositoryPortImpl implements ProjetoCommandRepositoryPort {


    @Autowired
    private ProjetoRepositorio command;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public Projeto create(Projeto projeto) {
        ProjetoModel entity = modelMapper.map(projeto, ProjetoModel.class);
        return modelMapper.map(command.save(entity), Projeto.class);
    }

    @Override
    public void deleteByNome(String nome) {
        command.deleteByNome(nome);
    }

    @Override
    public Projeto update(Projeto projeto) {
        return modelMapper.map(command.save(modelMapper.map(projeto, ProjetoModel.class)), Projeto.class);
    }
}
