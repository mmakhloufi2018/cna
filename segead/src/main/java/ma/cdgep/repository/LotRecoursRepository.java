package ma.cdgep.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ma.cdgep.demande.entity.LotRecoursEntity;

@Repository
public interface LotRecoursRepository extends CrudRepository<LotRecoursEntity, Long> {
	List<LotRecoursEntity> findByReferenceLot(String referenceLot);
}
