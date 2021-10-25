package eti.italiviocorrea.api.rsocket.lcr.adapters.outbound.persistence.r2dbc.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(value = "autoridade_certificadora")
public class AutoridadeCertificadoraEntity {

    @Id
    @Column("iditc")
    Integer iditc;

    @Column("alias")
    String alias;

    @Column("nome")
    String nome;

}
