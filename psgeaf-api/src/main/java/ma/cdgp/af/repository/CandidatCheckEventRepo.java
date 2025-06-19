package ma.cdgp.af.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.cdgp.af.entity.CandidatCheckEvent;

@Repository
public interface CandidatCheckEventRepo extends JpaRepository<CandidatCheckEvent, Long> {

}
