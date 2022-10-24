package br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.usecase.exceptions;

public class ProjetoJaExisteException extends RuntimeException{

    public ProjetoJaExisteException(final String mensagem) {
        super(mensagem);
    }
}
