package ma.cdgp.af.repository;


import ma.cdgp.af.entity.TraceEnvoiRetourNotification;
import ma.cdgp.af.entity.rsu.TraceEnvoiRetourDemandeRsu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TraceEnvoiRetourDemandeRsuRepository extends JpaRepository<TraceEnvoiRetourDemandeRsu,Long> {
}
