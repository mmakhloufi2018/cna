package ma.cdgp.af.repository;


import ma.cdgp.af.entity.CandidatCollected;
import ma.cdgp.af.entity.massar.LotSituationMassar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LotSituationMassarRepo extends JpaRepository<LotSituationMassar, Long> {

    @Query("select distinct r from LotSituationMassar r  where  ( r.etatLot is null or r.etatLot in ( 'KO' )  "
            + ")")
    public Page<LotSituationMassar> findIntegrationKO(Pageable pageable);


    @Query("select distinct r from CandidatCollected r  where  not exists (from  DemandeSituationMassar d where d.numMassar = r.massar "
            + ")")
    public Page<CandidatCollected> findMissedMassarCandidat(Pageable pageable);
    
    

}