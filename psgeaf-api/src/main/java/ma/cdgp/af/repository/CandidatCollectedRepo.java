package ma.cdgp.af.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ma.cdgp.af.entity.CandidatCollected;

@Repository
public interface CandidatCollectedRepo extends JpaRepository<CandidatCollected, Long> {

	@Query("select distinct c from CandidatCollected c where c.reference = :reference ")
	List<CandidatCollected> findByReference(@Param("reference") String reference);

	@Query("select distinct r from CandidatCollected r  where  r.cin is not null and not exists (from  CandidatCheckEvent d where d.collected = r and d.partenaire = 'CMR' "
			+ ") and r.requeteSp = '1' "
			+ " and r.archive is false")
	public Page<CandidatCollected> findMissedCmrCandidat(Pageable pageable);

	@Query("select distinct r from CandidatCollected r  where r.cin is not null and  not exists (from  CandidatCheckEvent d where d.collected = r and d.partenaire = 'TGR' "
			+ ") and r.requeteSp = '1' "
			+ "and r.archive is false")
	public Page<CandidatCollected> findMissedTgrCandidat(Pageable pageable);

	@Query("select distinct r from CandidatCollected r  where r.cin is not null and not exists ("
			+ " from CandidatCheckEvent d where d.collected = r and d.partenaire = 'CNSS'"
			+ ") and r.requeteSp = '1' and r.archive is false")
	public Page<CandidatCollected> findMissedCnssCandidat(Pageable pageable);

	@Query("select distinct r from CandidatCollected r  where  r.massar is not null and not exists (from  CandidatCheckEvent d where d.collected = r and (d.partenaire = 'MASSAR' or d.partenaire = 'MASSSAR') "
			+ ") and r.requeteSc = '1' "
			+ " and r.archive is false")
	public Page<CandidatCollected> findMissedMassarCandidat(Pageable pageable);


	@Query("select distinct r from CandidatCollected r  where  r.massar is not null and not exists (from  CandidatCheckEventMassar2025 d where d.collected = r and (d.partenaire = 'MASSAR' or d.partenaire = 'MASSSAR') "
			+ ") and r.requeteSc = '1' "
			+ " and r.archive is false")
	public Page<CandidatCollected> findMissedMassar2025Candidat(Pageable pageable);

	@Query("select distinct r from CandidatCollected r  where  r.idcs is not null and not exists (from CandidatCheckEvent d where d.collected = r and d.partenaire = 'SANTE' "
			+ ") and r.requeteHa like '1' and r.blDelete is false"
			+ " and r.archive is false")
	public Page<CandidatCollected> findMissedSanteCandidat(Pageable pageable);

	@Query("select distinct r from CandidatCollected r  where  r.cin is not null and not exists (from CandidatCheckEvent d where d.collected = r and d.partenaire = 'MI' "
			+ ") and r.requeteSp like '1' and r.blDelete is false"
			+ " and r.archive is false")
	public Page<CandidatCollected> findMissedMiCandidat(Pageable pageable);

}
