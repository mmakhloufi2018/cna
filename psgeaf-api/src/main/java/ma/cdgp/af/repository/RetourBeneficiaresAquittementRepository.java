package ma.cdgp.af.repository;

import ma.cdgp.af.entity.tgr.RetourBeneficiaresAquittementTgr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RetourBeneficiaresAquittementRepository extends JpaRepository<RetourBeneficiaresAquittementTgr, Long> {
}
