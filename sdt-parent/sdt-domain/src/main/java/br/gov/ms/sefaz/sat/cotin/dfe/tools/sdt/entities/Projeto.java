package br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.entities;

import lombok.Data;

import java.util.List;

@Data
public class Projeto {

    String nome;
    String descricao;
    List<Versao> versoes;
    List<Ambiente> ambientes;

}
