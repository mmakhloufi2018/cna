package ma.cdgep.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ma.cdgep.demande.entity.LotDemandeEntity;

@Repository
public interface LotDemandeRepository extends CrudRepository<LotDemandeEntity, Long> {

	List<LotDemandeEntity> findByReferenceLot(String referenceLot);

}
