package br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.entities;

import lombok.Data;

@Data
public class Ambiente {

    Long id;
    String nome;
    String dominio;
    Long porta;
    String protocolo;

}
