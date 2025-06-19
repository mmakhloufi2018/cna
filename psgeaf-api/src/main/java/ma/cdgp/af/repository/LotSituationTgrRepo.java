package ma.cdgp.af.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.cdgp.af.entity.CandidatCollected;
import ma.cdgp.af.entity.cmr.LotSituationCmr;
import ma.cdgp.af.entity.cnss.LotSituationCnss;
import ma.cdgp.af.entity.tgr.LotSituationTgr;

@Repository
public interface LotSituationTgrRepo extends JpaRepository<LotSituationTgr, Long> {
	@Query("select distinct r from LotSituationSante r left join r.personnes req  where  r.lotId is not null and ( r.etatLot is null or r.etatLot like 'KO'   "
			+ ")")
	public Page<LotSituationTgr> findKO(Pageable pageable);

	@Query("select distinct l from LotSituationSante l where l.lotId = :lotId and l.partenaire = :partenaire")
	public List<LotSituationTgr> findLotByReferenceAndType(String lotId, String partenaire);
	
	@Query("select distinct r from LotSituationTgr r  where  ( r.etatLot is null or r.etatLot in ( 'KO' )  "
			+ ")")
	public Page<LotSituationTgr> findIntegrationKO(Pageable pageable);
	 
	@Query("select distinct r from CandidatCollected r  where  not exists (from  DemandeSituationTgr d where d.cin = r.cin "
			+ ")")
	public Page<CandidatCollected> findMissedCandidat(Pageable pageable);
}
