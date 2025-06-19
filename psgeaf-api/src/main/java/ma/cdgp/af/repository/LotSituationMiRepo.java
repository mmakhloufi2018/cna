package ma.cdgp.af.repository;


import ma.cdgp.af.entity.CandidatCollected;
import ma.cdgp.af.entity.massar.LotSituationMassar;
import ma.cdgp.af.entity.mi.LotSituationMi;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LotSituationMiRepo extends JpaRepository<LotSituationMi, Long> {

    @Query("select distinct r from LotSituationMi r  where  ( r.etatLot is null or r.etatLot in ( 'KO' )  "
            + ")")
    public Page<LotSituationMi> findIntegrationKO(Pageable pageable);


    @Query("select distinct r from CandidatCollected r  where  not exists (from  DemandeSituationMi d where d.cin = r.cin "
            + ")")
    public Page<CandidatCollected> findMissedMiCandidat(Pageable pageable);
    
    

}