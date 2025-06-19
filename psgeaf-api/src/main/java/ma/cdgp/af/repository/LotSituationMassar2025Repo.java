package ma.cdgp.af.repository;


import ma.cdgp.af.entity.CandidatCollected;
import ma.cdgp.af.entity.massar.LotSituationMassar;
import ma.cdgp.af.entity.massar2025.LotSituationMassar2025;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LotSituationMassar2025Repo extends JpaRepository<LotSituationMassar2025, Long> {


    @Query("select distinct r from LotSituationMassar2025 r  where  ( r.etatLot is null or r.etatLot in ( 'KO' )  "
            + ")")
    public Page<LotSituationMassar2025> findIntegrationKO(Pageable pageable);


    @Query("select distinct r from CandidatCollected r  where  not exists (from  DemandeSituationMassar2025 d where d.numMassar = r.massar "
            + ")")
    public Page<CandidatCollected> findMissedMassarCandidat(Pageable pageable);


}
