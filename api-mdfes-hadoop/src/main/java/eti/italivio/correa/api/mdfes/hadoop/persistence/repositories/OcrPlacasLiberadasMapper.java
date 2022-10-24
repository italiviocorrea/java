package eti.italivio.correa.api.mdfes.hadoop.persistence.repositories;

import eti.italivio.correa.api.mdfes.hadoop.persistence.entities.OcrPlacasLiberadas;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OcrPlacasLiberadasMapper {

    @Select("SELECT * FROM DFE_MDFES.OCR_PLACAS_LIBERADAS WHERE PLACA = #{placa}  ORDER BY DATA_INCLUSAO LIMIT 1")
    @Results({
            @Result(property = "placa", column = "PLACA"),
            @Result(property = "data_inclusao", column = "DATA_INCLUSAO"),
            @Result(property = "entrada_liberada", column = "ENTRADA_LIBERADA"),
            @Result(property = "data_alteracao", column = "DATA_ALTERACAO"),
            @Result(property = "saida_liberada", column = "SAIDA_LIBERADA"),
            @Result(property = "uf_ini", column = "UF_INI"),
            @Result(property = "uf_fim", column = "UF_FIM")
    })
    OcrPlacasLiberadas findByPlaca(@Param("placa") String placa);

}
