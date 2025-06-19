package ma.cdgep.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ma.cdgep.demande.entity.ConjointEntity;
import ma.cdgep.demande.entity.DemandeEntity;
import ma.cdgep.demande.entity.DemandeurEntity;
import ma.cdgep.demande.entity.EnfantEntity;

@Repository
public interface DemandeRepository extends CrudRepository<DemandeEntity, Long> {
	List<DemandeEntity> findByReferenceCnssIn(List<String> referenceDemande);
	@Query(value = " SELECT distinct d.referenceCnss FROM DemandeEntity  d where d.referenceCnss in(:referenceDemande) and d.typePrestation = :prestation and statut <> 'REJETE' " )
	List<String> getExistReferenceCnssAndPrestation(List<String> referenceDemande, String prestation);
	
	@Query(value = " SELECT distinct d.demandeur.idcs FROM DemandeEntity  d   where d.demandeur.idcs in(:idcs)" )
	List<String> getExistIdcs(List<String> idcs);
	
	@Query(value = " select distinct reference_cnss from demande d join dossier_asd da on da.reference = d.reference_cnss "
			+ "where da.date_cloture is null and d.reference_cnss in(:referenceDemande)" , nativeQuery = true)
	List<String> getExistReferenceCnss(List<String> referenceDemande);

	@Query(value = " SELECT distinct d FROM DemandeEntity  d " 
			+ " LEFT join fetch d.demandeur dr  "
			+ " LEFT JOIN   dr.conjoints c"
			+ " LEFT JOIN   dr.enfants e"
			+ " LEFT JOIN   dr.autreMembres a"
			+ " where d.statut =:statut"
			+ " and rownum < 1000"
			+ " order by  d.dateDemande ASC ")
	List<DemandeEntity> getDemandesByStatut(@Param("statut") String statut);
	
	
	@Query(value = " SELECT distinct d FROM DemandeEntity  d " 
			+ " where d.statut =:statut")
	Page<DemandeEntity> getDemandesByStatut2(@Param("statut") String statut,  Pageable pageable);
	
	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE DemandeEntity d set d.statut =:statut where d.immatriculation = :referenceDemande ")
	public int updateSatutDemande(String statut, String referenceDemande);
	
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "UPDATE DemandeEntity d set d.motifRejet = :motifRejet,  d.statut = 'EST_RELANCE' where d.id = :id ")
	public int updateSatutMotifDemande(String motifRejet, Long id);
	
	@Query(value = " SELECT e FROM EnfantEntity  e where e.demandeur = :demandeur" )
	List<EnfantEntity> getEnfs(DemandeurEntity demandeur);
	
	@Query(value = " SELECT e FROM ConjointEntity  e where e.demandeur = :demandeur" )
	List<ConjointEntity> getConjs(DemandeurEntity demandeur);
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "UPDATE DemandeEntity d set  d.statut = 'RELANCER_OK' where d.id = :id ")
	void updateSatutMotifDemande(Long id);

}
