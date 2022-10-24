package br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.usecase.ports;

import br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.entities.Projeto;

import java.util.List;
import java.util.Optional;

public interface ProjetoQueryRepositoryPort {


    Optional<Projeto> findByNome(String nome);

    List<Projeto> findAllProjetos();

}
