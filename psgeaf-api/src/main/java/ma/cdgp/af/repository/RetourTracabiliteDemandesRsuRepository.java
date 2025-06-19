package ma.cdgp.af.repository;

import ma.cdgp.af.entity.rsu.RetourTracabiliteDemandeRsu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RetourTracabiliteDemandesRsuRepository extends JpaRepository<RetourTracabiliteDemandeRsu,Long> {
}
