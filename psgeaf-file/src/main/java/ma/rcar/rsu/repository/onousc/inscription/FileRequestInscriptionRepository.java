package ma.rcar.rsu.repository.onousc.inscription;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ma.rcar.rsu.entity.onousc.inscription.FileRequestOnouscInscriptionEntity;



/**
 * @author BAKHALED Ibrahim.
 *
 */


@Repository
public interface FileRequestInscriptionRepository extends JpaRepository<FileRequestOnouscInscriptionEntity, Long> {

	@Query(value = "select distinct f from ma.rcar.rsu.entity.onousc.inscription.FileRequestOnouscInscriptionEntity f where f.fileName  = :fileName")
	public FileRequestOnouscInscriptionEntity getByFileName(@Param("fileName") String fileName);
}
