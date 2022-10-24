package br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.usecase;

import br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.entities.Projeto;
import br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.usecase.exceptions.ProjetoJaExisteException;
import br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.usecase.exceptions.ProjetoNaoExisteException;
import br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.usecase.ports.ProjetoCommandRepositoryPort;
import br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.usecase.ports.ProjetoQueryRepositoryPort;
import br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.usecase.validators.ProjetoValidador;

public final class ProjetoCommand {

    private final ProjetoCommandRepositoryPort command;
    private final ProjetoQueryRepositoryPort query;


    public ProjetoCommand(ProjetoCommandRepositoryPort command, ProjetoQueryRepositoryPort query) {
        this.command = command;
        this.query = query;
    }

    public Projeto create(final Projeto projeto) {
        ProjetoValidador.validateProjeto(projeto);
        if(query.findByNome(projeto.getNome()).isPresent()){
            throw new ProjetoJaExisteException(projeto.getNome());
        }
        return command.create(projeto);
    }

    public Projeto update(final Projeto projeto) {
        ProjetoValidador.validateProjeto(projeto);
        if(!query.findByNome(projeto.getNome()).isPresent()){
            throw new ProjetoNaoExisteException(projeto.getNome());
        }
        return command.update(projeto);
    }

    public void delete(final String nome) {
        if(!query.findByNome(nome).isPresent()){
            throw new ProjetoNaoExisteException(nome);
        }
        command.deleteByNome(nome);
    }

}
