package br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.repositorio.model;

import lombok.Data;

@Data
public class AmbienteModel {

    Long id;
    String nome;
    String dominio;
    Long porta;
    String protocolo;

}
