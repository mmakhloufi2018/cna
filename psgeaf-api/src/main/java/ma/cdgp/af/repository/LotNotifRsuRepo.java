package ma.cdgp.af.repository;


import ma.cdgp.af.entity.notifRsu.LotNotifRsu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LotNotifRsuRepo extends JpaRepository<LotNotifRsu, Long> {

}
