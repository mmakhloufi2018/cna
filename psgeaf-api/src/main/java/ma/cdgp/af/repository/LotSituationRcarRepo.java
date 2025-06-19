package ma.cdgp.af.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ma.cdgp.af.entity.rcar.LotSituationRcar;

@Repository
public interface LotSituationRcarRepo extends JpaRepository<LotSituationRcar, Long> {
	@Query("select distinct r from LotSituationRcar r left join r.personnes req  where  r.lotId is not null and ( r.etatLot is null or r.etatLot like 'KO'   "
			+ ")")
	public Page<LotSituationRcar> findKO(Pageable pageable);

	@Query("select distinct l from LotSituationRcar l where l.lotId = :lotId and l.partenaire = :partenaire")
	public List<LotSituationRcar> findLotByReferenceAndType(String lotId, String partenaire);
	
	
	@Transactional
	@Modifying
	@Query("update LotSituationRcar v set v.locked = :locked where id = :id")
	void updateLock(@Param("locked") Boolean locked, @Param("id")  Long id);
}
