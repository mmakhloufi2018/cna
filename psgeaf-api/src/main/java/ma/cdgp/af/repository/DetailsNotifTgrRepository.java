package ma.cdgp.af.repository;

import ma.cdgp.af.entity.tgr.DetailsNotifTgr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailsNotifTgrRepository extends JpaRepository<DetailsNotifTgr, Long> {
}
