package ma.cdgp.af.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.cdgp.af.entity.CandidatCollected;
import ma.cdgp.af.entity.cnss.LotSituationCnss;

@Repository
public interface LotSituationCnssRepo extends JpaRepository<LotSituationCnss, Long> {
	@Query("select distinct r from LotSituationCnss r  where  ( r.etatLot is null or r.etatLot in ( 'KO_REP','WAITING_RESPONSE' )   "
			+ ")")
	public Page<LotSituationCnss> findReponseKO(Pageable pageable);

	
	@Query("select distinct r from LotSituationCnss r  where  ( r.etatLot is null or r.etatLot in ( 'KO_SEND' )  "
			+ ")")
	public Page<LotSituationCnss> findIntegrationKO(Pageable pageable);
	 
	@Query("select distinct r from CandidatCollected r  where  not exists (from  DemandeSituationCnss d where d.cin = r.cin "
			+ ")")
	public Page<CandidatCollected> findMissedCnssCandidat(Pageable pageable);
}
