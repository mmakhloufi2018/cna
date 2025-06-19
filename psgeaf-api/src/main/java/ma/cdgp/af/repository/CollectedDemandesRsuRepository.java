package ma.cdgp.af.repository;


import org.springframework.data.repository.query.Param;
import ma.cdgp.af.dto.af.rsu.DemandeRsuDto;
import ma.cdgp.af.entity.rsu.CollectedDemandesRsu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CollectedDemandesRsuRepository  extends JpaRepository<CollectedDemandesRsu, Long> {

    Optional<CollectedDemandesRsu> findByIdcs(Long idcs);



    @Modifying
    @Transactional
    @Query("UPDATE CollectedDemandesRsu c SET c.flag = :flag WHERE c.id IN :ids")
    void updateFlagForIdcs(@Param("ids") Set<Long> ids, @Param("flag") boolean flag);


    Set<CollectedDemandesRsu> findAllByIdcsIn(Set<Long> idcs);


    @Query("SELECT c FROM CollectedDemandesRsu c\n" +
            "            WHERE (c.flag IS NULL OR c.flag = false ) AND c.dateNaissance is not null AND c.idcs is not null\n")
    Page<CollectedDemandesRsu> findDemandesRsuEsgeafByInstanceId(Pageable pageable);

}
