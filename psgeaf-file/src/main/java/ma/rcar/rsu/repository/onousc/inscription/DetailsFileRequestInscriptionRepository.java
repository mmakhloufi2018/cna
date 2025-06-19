package ma.rcar.rsu.repository.onousc.inscription;

import ma.rcar.rsu.entity.onousc.inscription.DetailsFileRequestOnouscInscriptionEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



/**
 * @author BAKHALED Ibrahim.
 *
 */



@Repository
public interface DetailsFileRequestInscriptionRepository extends JpaRepository<DetailsFileRequestOnouscInscriptionEntity, Long> {
}
