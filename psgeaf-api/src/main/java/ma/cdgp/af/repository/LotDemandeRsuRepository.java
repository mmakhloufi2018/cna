package ma.cdgp.af.repository;


import ma.cdgp.af.entity.rsu.LotDemandeRsu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LotDemandeRsuRepository extends JpaRepository<LotDemandeRsu, Long> {
}
