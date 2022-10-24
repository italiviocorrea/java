package eti.italivio.correa.api.mdfes.hadoop.controllers;

import eti.italivio.correa.api.mdfes.hadoop.exceptions.PlacaNotFoundException;
import eti.italivio.correa.api.mdfes.hadoop.persistence.entities.OcrPlacasLiberadas;
import eti.italivio.correa.api.mdfes.hadoop.persistence.repositories.OcrPlacasLiberadasMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Pattern;

@Validated
@RestController
@RequestMapping("/api/v1/ocr/")
@Tag(name = "ocr", description = "Placas liberadas Endpoint")
@Slf4j
public class OcrPlacasLiberadasController {

    @Autowired
    private OcrPlacasLiberadasMapper ocrPlacasLiberadasMapper;

    @Operation(summary = "Encontra registros de liberações para uma placa informada", description = "Uma placa deve ser informada para buscar as liberações para ela.", tags = {"ocr"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Placa pesquisada com sucesso!", content = @Content(schema = @Schema(implementation = OcrPlacasLiberadas.class))),
            @ApiResponse(responseCode = "400", description = "Placa informada não é válida!", content = @Content),
            @ApiResponse(responseCode = "404", description = "Placa informada não foi encontrada!", content = @Content)})
    @GetMapping("placaliberada/{placa}")
    public ResponseEntity<OcrPlacasLiberadas> placaLiberada(@PathVariable("placa") @Pattern(regexp = "^[a-zA-Z]{3}[0-9][A-Za-z0-9][0-9]{2}$", message = "Placa informada não é válida!") String placa) {

        log.info("Pesquisando a placa " + placa);

        OcrPlacasLiberadas ocrPlacasLiberadas = ocrPlacasLiberadasMapper.findByPlaca(placa);
        if (ocrPlacasLiberadas == null) {
            throw new PlacaNotFoundException();
        }
        return ResponseEntity.ok(ocrPlacasLiberadas);
    }

}
