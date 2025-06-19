package ma.cdgp.af.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.cdgp.af.entity.cmr.LotSituationCmr;
import ma.cdgp.af.entity.cnss.LotSituationCnss;
import ma.cdgp.af.entity.rcar.DetailsLotSituationRcar;
import ma.cdgp.af.entity.rcar.LotSituationRcar;
import ma.cdgp.af.entity.tgr.LotSituationTgr;

@Repository
public interface DetailsLotSituationRcarRepo extends JpaRepository<DetailsLotSituationRcar, Long> {
	@Query("select distinct l from LotSituationRcar l where l.lotId = :lotId and l.partenaire = :partenaire")
	public List<LotSituationRcar> findLotByReferenceAndType(String lotId, String partenaire);
}
