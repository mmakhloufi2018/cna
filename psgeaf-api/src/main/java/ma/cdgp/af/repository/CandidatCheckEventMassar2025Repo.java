package ma.cdgp.af.repository;

import ma.cdgp.af.entity.CandidatCheckEvent;
import ma.cdgp.af.entity.massar2025.CandidatCheckEventMassar2025;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidatCheckEventMassar2025Repo extends JpaRepository<CandidatCheckEventMassar2025, Long> {
}
