package ma.rcar.rsu.repository.onousc.paiement;


import ma.rcar.rsu.entity.onousc.paiement.DetailsFileRequestOnouscPaiementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;




/**
 * @author BAKHALED Ibrahim.
 *
 */


@Repository
public interface DetailsFileRequestPaiementRepository extends JpaRepository<DetailsFileRequestOnouscPaiementEntity, Long> {
}
