package ma.cdgp.af.repository;


import ma.cdgp.af.entity.rsu.DemandeRsu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandeRsuRepository extends JpaRepository<DemandeRsu,Long> {
}
