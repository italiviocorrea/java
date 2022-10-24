package br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.usecase.validators;

import br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.entities.Projeto;
import br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.usecase.exceptions.ProjetoValidacaoException;
import org.apache.commons.lang3.ObjectUtils;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class ProjetoValidador {

    public static void validateProjeto(final Projeto projeto) {
        if( projeto == null) throw new ProjetoValidacaoException("Projeto não pode ser nulo");
        if( isBlank(projeto.getNome())) throw new ProjetoValidacaoException("O nome do projeto não pode ser nulo");
        if(ObjectUtils.isEmpty(projeto.getAmbientes())) throw new ProjetoValidacaoException("O projeto deve ter pelo menos um ambiente");
        if(ObjectUtils.isEmpty(projeto.getVersoes())) throw new ProjetoValidacaoException("O projeto deve ter pelo menos uma versão");
    }
}
