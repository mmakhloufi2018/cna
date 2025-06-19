package ma.cdgp.af.repository;


import ma.cdgp.af.entity.cmr.LotNotifCmr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LotNotifCmrRepository  extends JpaRepository<LotNotifCmr, Long> {
}
