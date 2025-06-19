package ma.cdgp.af.repository;

import ma.cdgp.af.entity.tgr.LotNotifTgr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LotNotifTgrRepo extends JpaRepository<LotNotifTgr, Long> {



}
