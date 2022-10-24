package br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.entities;

import lombok.Data;

import java.util.List;

@Data
public class Servico {

    String nome;
    String wsdl;
    String envelope;
    List<Metodo> metodos;
    List<Esquema> esquemas;

}
