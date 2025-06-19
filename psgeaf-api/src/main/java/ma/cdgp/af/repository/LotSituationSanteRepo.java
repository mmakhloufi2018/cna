package ma.cdgp.af.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.cdgp.af.entity.sante.LotSituationSante;

@Repository
public interface LotSituationSanteRepo extends JpaRepository<LotSituationSante, Long> {
	@Query("select distinct r from LotSituationSante r left join r.personnes req  where  r.lotId is not null and ( r.etatLot is null or r.etatLot like 'KO'   "
			+ ")")
	public Page<LotSituationSante> findKO(Pageable pageable);

	@Query("select distinct l from LotSituationSante l where l.lotId = :lotId and l.partenaire = :partenaire")
	public List<LotSituationSante> findLotByReferenceAndType(String lotId, String partenaire);
}
