package br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.spring.boot.rest.controllers;

import br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.entities.Projeto;
import br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.usecase.ProjetoCommand;
import br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.usecase.ProjetoQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/projetos")
public class ProjetoControllers {

    @Autowired
    ProjetoCommand command;

    @Autowired
    ProjetoQuery query;

    @GetMapping("{nome}")
    public ResponseEntity<Optional<Projeto>> findByNome(@RequestParam String nome) {
        return new ResponseEntity<>(query.findByNome(nome), HttpStatus.FOUND);
    }

    @GetMapping
    public ResponseEntity<List<Projeto>> findAll() {
        return new ResponseEntity<>(query.findAllProjetos(), HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<Projeto> create(@RequestBody Projeto projeto) {
        return new ResponseEntity<>(command.create(projeto), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Projeto> update(@RequestBody Projeto projeto) {
        return new ResponseEntity<>(command.update(projeto), HttpStatus.OK);
    }

    @DeleteMapping("{nome}")
    public ResponseEntity<String> delete(@PathVariable String nome) {
        return new ResponseEntity<>("deleted", HttpStatus.OK);
    }
}
