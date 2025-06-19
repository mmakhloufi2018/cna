package ma.cdgep.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ma.cdgep.demande.entity.LotAjoutMembreEntity;

@Repository
public interface lotAjoutMembreRepository extends CrudRepository<LotAjoutMembreEntity, Long> {
	List<LotAjoutMembreEntity> findByReferenceLot(String referenceLot);
}
