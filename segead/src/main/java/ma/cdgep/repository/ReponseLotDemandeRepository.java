package ma.cdgep.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ma.cdgep.demande.entity.ReponseLotDemandeEntity;

@Repository
public interface ReponseLotDemandeRepository extends CrudRepository<ReponseLotDemandeEntity, Long> {

}
