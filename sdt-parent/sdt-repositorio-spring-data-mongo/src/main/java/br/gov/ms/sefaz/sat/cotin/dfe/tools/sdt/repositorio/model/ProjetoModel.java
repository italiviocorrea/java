package br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.repositorio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "projetos")
@Builder
@Getter
@Setter
@AllArgsConstructor
public class ProjetoModel {

    @Id
    String id;
    String nome;
    String descricao;
    List<VersaoModel> versoes;
    List<AmbienteModel> ambientes;

}
