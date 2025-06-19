package ma.cdgep.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ma.cdgep.paiement.entity.EcheanceEntity;

@Repository
public interface EcheanceRepository extends CrudRepository<EcheanceEntity, Long> {

	List<EcheanceEntity> findByAnneeAndMoisAndType(String annee, String mois, String type);
	
	List<EcheanceEntity> findByStatut(String statut);

}
