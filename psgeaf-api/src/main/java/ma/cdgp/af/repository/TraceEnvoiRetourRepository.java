package ma.cdgp.af.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.cdgp.af.entity.TraceEnvoiRetour;

@Repository
public interface TraceEnvoiRetourRepository extends JpaRepository<TraceEnvoiRetour, Long> {
	 
}
