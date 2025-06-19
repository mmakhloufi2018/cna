package ma.cdgep.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ma.cdgep.eligibilite.entity.EligibiliteLotDemandeEntity;

@Repository
public interface EligibiliteRepository extends CrudRepository<EligibiliteLotDemandeEntity, Long> {

	EligibiliteLotDemandeEntity getById(Long id);
	
	@Query("select id from EligibiliteLotDemandeEntity where etat ='N' ")
	List<Long> getall();

	@Modifying
	@Transactional
	@Query("update EligibiliteLotDemandeEntity set statusRetour= :statusRetour, errorRetour= :errorRetour, etat='S' where id = :id")
	void updateStatut(String statusRetour, String errorRetour, Long id);
	
	@Modifying
	@Transactional
	@Query("update EligibiliteLotDemandeEntity set statusRetour= :statusRetour, errorRetour= :errorRetour, etat='E' where id = :id")
	void updateStatutError(String statusRetour, String errorRetour, Long id);

	@Query("select id from EligibiliteLotDemandeEntity where etat ='N' and echeance= :echeance ")
	List<Long> getallBy(String echeance);

}
