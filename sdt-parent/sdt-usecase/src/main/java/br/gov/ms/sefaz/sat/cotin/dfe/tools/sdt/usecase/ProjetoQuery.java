package br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.usecase;

import br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.entities.Projeto;
import br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.usecase.ports.ProjetoCommandRepositoryPort;
import br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.usecase.ports.ProjetoQueryRepositoryPort;

import java.util.List;
import java.util.Optional;

public final class ProjetoQuery {

    private final ProjetoQueryRepositoryPort query;


    public ProjetoQuery(ProjetoQueryRepositoryPort query) {
        this.query = query;
    }

    public Optional<Projeto> findByNome(String nome) {
        return query.findByNome(nome);
    }

    public List<Projeto> findAllProjetos() {
        return query.findAllProjetos();
    }

}
