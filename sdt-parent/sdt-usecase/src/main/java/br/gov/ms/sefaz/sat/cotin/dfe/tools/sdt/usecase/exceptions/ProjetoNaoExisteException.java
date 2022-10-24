package br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.usecase.exceptions;

public class ProjetoNaoExisteException extends RuntimeException{

    public ProjetoNaoExisteException(final String mensagem) {
        super(mensagem);
    }
}
