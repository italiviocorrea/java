package br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.usecase.ports;

import br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.entities.Projeto;

import java.util.List;
import java.util.Optional;

public interface ProjetoCommandRepositoryPort {

    Projeto create(Projeto projeto);

    void deleteByNome(String nome);

    Projeto update(Projeto projeto);

}
