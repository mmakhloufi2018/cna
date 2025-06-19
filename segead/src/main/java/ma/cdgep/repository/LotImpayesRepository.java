package ma.cdgep.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ma.cdgep.paiement.entity.LotImpayesEntity;

@Repository
public interface LotImpayesRepository extends CrudRepository<LotImpayesEntity, Long> {

	

}
