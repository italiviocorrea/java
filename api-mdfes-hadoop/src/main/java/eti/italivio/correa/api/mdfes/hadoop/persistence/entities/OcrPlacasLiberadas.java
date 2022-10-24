package eti.italivio.correa.api.mdfes.hadoop.persistence.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.joda.time.DateTime;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OcrPlacasLiberadas {

    private String placa;
    private Timestamp data_inclusao;
    private boolean entrada_liberada;
    private Timestamp data_alteracao;
    private boolean saida_liberada;
    private String uf_ini;
    private String uf_fim;

}

