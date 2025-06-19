package ma.cdgp.af.repository;


import ma.cdgp.af.entity.rsu.DetailsDemandeRsu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailsDemandeRsuRepository extends JpaRepository<DetailsDemandeRsu,Long> {
}
