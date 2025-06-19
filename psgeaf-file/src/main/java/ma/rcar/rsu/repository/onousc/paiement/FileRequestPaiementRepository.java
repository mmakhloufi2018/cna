package ma.rcar.rsu.repository.onousc.paiement;


import ma.rcar.rsu.entity.onousc.paiement.FileRequestOnouscPaiementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



/**
 * @author BAKHALED Ibrahim.
 *
 */


@Repository
public interface FileRequestPaiementRepository extends JpaRepository<FileRequestOnouscPaiementEntity, Long> {

	@Query(value = "select distinct f from ma.rcar.rsu.entity.onousc.paiement.FileRequestOnouscPaiementEntity f where f.fileName  = :fileName")
	public FileRequestOnouscPaiementEntity getByFileName(@Param("fileName") String fileName);
}
