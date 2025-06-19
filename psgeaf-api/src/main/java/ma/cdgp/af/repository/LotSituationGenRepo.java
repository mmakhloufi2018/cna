package ma.cdgp.af.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.cdgp.af.entity.LotSituationGen;

@Repository
public interface LotSituationGenRepo extends JpaRepository<LotSituationGen, Long> {
	@Query("select distinct r from LotSituationGen r "
			+ " left join LotSituationRcar rcar on rcar.id = r.id and rcar.lotId is not null "
//			+ " left join LotSituationCmr cmr on cmr.id = r.id  and cmr.lotId is not null  "
//			+ " left join LotSituationCnss cnss on cnss.id = r.id  and cnss.lotId is not null "
//			+ " left join LotSituationTgr tgr on tgr.id = r.id  and tgr.lotId is not null "
//			+ " left join LotSituationSante sante on sante.id = r.id  and sante.lotId is not null "
			+ " where   1 = 2 "
			+ " or ( rcar.id is not null and (rcar.locked is null or rcar.locked is false) and (rcar.etatLot is null or rcar.etatLot like 'KO') ) "
//			+ " or ( cmr.id is not null and (cmr.locked is null or cmr.locked is false) and ( cmr.etatLot is null or cmr.etatLot like 'KO' ))  "
//			+ " or ( cnss.id is not null and (cnss.locked is null or cnss.locked is false) and ( cnss.etatLot is null or cnss.etatLot like 'KO' )) "
//			+ " or ( tgr.id is not null and (tgr.locked is null or tgr.locked is false) and (tgr.etatLot is null or tgr.etatLot like 'KO') )  "
//			+ " or ( sante.id is not null and (sante.locked is null or sante.locked is false) and (sante.etatLot is null or sante.etatLot like 'KO') )  "
			+ " ")
	public Page<LotSituationGen> findKO(Pageable pageable);
	
	

}
