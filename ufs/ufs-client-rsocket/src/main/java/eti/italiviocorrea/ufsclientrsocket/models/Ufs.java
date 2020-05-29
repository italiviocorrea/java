package eti.italiviocorrea.ufsclientrsocket.models;

import lombok.*;

import java.time.LocalDate;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ufs {

    private Integer id;
    private Short codigo;
    private String nome;
    private String sigla;
    private LocalDate inicioVigencia;
    private LocalDate fimVigencia;

}
