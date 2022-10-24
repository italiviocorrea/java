package br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.repositorio.model;

import lombok.Data;

import java.util.List;

@Data
public class ServicoModel {

    String nome;
    String wsdl;
    String envelope;
    List<MetodoModel> metodoModels;
    List<EsquemaModel> esquemaModels;

}
