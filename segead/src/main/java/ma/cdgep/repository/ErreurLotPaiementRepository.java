package ma.cdgep.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ma.cdgep.paiement.entity.ErreurLotPaiementEntity;

@Repository
public interface ErreurLotPaiementRepository extends CrudRepository<ErreurLotPaiementEntity, Long> {

	

}
