package ma.cdgep.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ma.cdgep.paiement.entity.LotPaiementEntity;

@Repository
public interface LotPaiementRepository extends CrudRepository<LotPaiementEntity, Long> {

	LotPaiementEntity getByReferenceEcheanceAndNumeroLot(String referenceEcheance, Integer numeroLot);

	@Modifying
	@Transactional
	@Query("update LotPaiementEntity set referenceCnss= :refecnss , statut = :status where id = :id")
	void updateLot(String refecnss, String status, Long id);

	@Modifying
	@Transactional
	@Query("update LotPaiementEntity set message= :message , statut = :status where id = :id")
	void updateLotEr(String message, String status, Long id);

	LotPaiementEntity getById(Long id);

	@Query("select id from LotPaiementEntity where statut ='N' ")
	List<Long> getall();
	
	@Query("select id from LotPaiementEntity where statut ='N' and referenceEcheance = :referenceEcheance  ")
	List<Long> getall(String referenceEcheance);
	

}
