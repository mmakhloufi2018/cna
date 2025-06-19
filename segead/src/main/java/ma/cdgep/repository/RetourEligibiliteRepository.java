package ma.cdgep.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ma.cdgep.eligibilite.entity.RetourEligibiliteLotEntity;

@Repository
public interface RetourEligibiliteRepository extends CrudRepository<RetourEligibiliteLotEntity, Long> {

	

}
