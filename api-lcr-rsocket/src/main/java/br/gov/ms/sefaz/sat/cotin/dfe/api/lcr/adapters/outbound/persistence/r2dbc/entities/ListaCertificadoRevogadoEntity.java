package br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.adapters.outbound.persistence.r2dbc.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Table(value = "dfe_lista_certificado_revogado")
public class ListaCertificadoRevogadoEntity {

    @Id
    @Column("idlcr")
    Integer id;

    @Column("indi_lcr_delta")
    int indiLcrDelta;

    @Column("indi_atualz_lcr")
    String indiAtualzLcr;

    @Column("info_url_lcr")
    String infoUrlLcr;

}
