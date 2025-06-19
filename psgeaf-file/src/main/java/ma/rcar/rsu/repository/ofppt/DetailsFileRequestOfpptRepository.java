package ma.rcar.rsu.repository.ofppt;

import ma.rcar.rsu.entity.ofppt.DetailsFileRequestOfpptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



/**
 * @author BAKHALED Ibrahim.
 *
 */


@Repository
public interface DetailsFileRequestOfpptRepository extends JpaRepository<DetailsFileRequestOfpptEntity, Long> {
}
