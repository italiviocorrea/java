package eti.italiviocorrea.apiufs.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("ufs")
public class Ufs {

    @Id
    @Column("id")
    private Integer id;

    @NonNull
    @Column("codigo")
    private Short codigo;

    @NonNull
    @Column("nome")
    private String nome;

    @NonNull
    @Column("sigla")
    private String sigla;

    @NonNull
    @Column("inicioVigencia")
    private LocalDate inicioVigencia;

    @Column("fimVigencia")
    private LocalDate fimVigencia;

}
