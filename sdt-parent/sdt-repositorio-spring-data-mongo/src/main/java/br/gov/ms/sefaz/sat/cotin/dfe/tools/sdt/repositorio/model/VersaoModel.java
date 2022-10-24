package br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.repositorio.model;

import lombok.Data;

import java.util.List;

@Data
public class VersaoModel {

    String versao;
    String contexto;
    List<ServicoModel> servicoModels;
    List<CasoTesteModel> casosTestes;

}
