package ma.cdgp.af.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.cdgp.af.entity.CandidatCollected;
import ma.cdgp.af.entity.cmr.LotSituationCmr;

@Repository
public interface LotSituationCmrRepo extends JpaRepository<LotSituationCmr, Long> {
	@Query("select distinct r from LotSituationCmr r left join r.personnes req  where  r.lotId is not null and ( r.etatLot is null or r.etatLot like 'KO'   "
			+ ")")
	public Page<LotSituationCmr> findKO(Pageable pageable);

	@Query("select distinct l from LotSituationCmr l where l.lotId = :lotId and l.partenaire = :partenaire")
	public List<LotSituationCmr> findLotByReferenceAndType(String lotId, String partenaire);
	
	
	
	@Query("select distinct r from LotSituationCmr r  where  ( r.etatLot is null or r.etatLot in ( 'KO' )  "
			+ ")")
	public Page<LotSituationCmr> findIntegrationKO(Pageable pageable);
	
	@Query("select distinct r from CandidatCollected r  where  not exists (from  DemandeSituationCmr d where d.cin = r.cin "
			+ ")")
	public Page<CandidatCollected> findMissedCmrCandidat(Pageable pageable);
}
