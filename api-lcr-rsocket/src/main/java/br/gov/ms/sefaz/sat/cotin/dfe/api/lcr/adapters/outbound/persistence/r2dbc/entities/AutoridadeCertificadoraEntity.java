package br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.adapters.outbound.persistence.r2dbc.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(value = "dfe_autoridade_certificadora")
public class AutoridadeCertificadoraEntity {

    @Id
    @Column("iditc")
    Integer iditc;

    @Column("alias")
    String alias;

    @Column("nome")
    String nome;

}
