package ma.cdgep.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;

import ma.cdgep.paiement.dto.SyntheseEncheanceEnvoyeeProj;
import ma.cdgep.paiement.entity.SyntheseEcheanceEntity;

@Repository
public interface SyntheseEcheanceRepository extends CrudRepository<SyntheseEcheanceEntity, Long> {

	
	@Query(value = "select count(l.id) as nombreLigneEnvoyee," + 
			" count(distinct s.id) as nombreLotEnvoye, sum(l.montant_operation) as montantTotalEnvoye from  paiement l, lot_paiement s "
			+ " WHERE s.id = l.lot_paiement_id AND s.statut = 'T' and s.REFERENCE_ECHEANCE = :refeeche AND s.TYPE_PRESTATION = :prestation", nativeQuery = true)
	SyntheseEncheanceEnvoyeeProj getSyntheseEncheanceEnvoyee(@Param("refeeche") String refeeche, @Param("prestation") String prestation);

	@Transactional
	@DeleteMapping
	@Query(value = "DELETE FROM SYNTHESE_ECHEANCE s "
			+ " WHERE s.REFERENCE_ECHEANCE = :refeeche AND s.TYPE_PRESTATION = :prestation", nativeQuery = true)
	void initSyntheseEcheance(@Param("refeeche") String refeeche, @Param("prestation") String prestation);

}
