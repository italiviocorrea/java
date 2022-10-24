package br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.repositorio;

import br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.repositorio.model.ProjetoModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjetoRepositorio extends MongoRepository<ProjetoModel,String> {

    Optional<ProjetoModel> findByNome(String nome);

    void deleteByNome(String nome);
}
