package ma.rcar.rsu.repository.mhai;


import ma.rcar.rsu.entity.mhai.DetailsFileRequestMhaiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



/**
 * @author BAKHALED Ibrahim.
 *
 */

@Repository
public interface DetailsFileRequestMhaiRepository extends JpaRepository<DetailsFileRequestMhaiEntity, Long> {
}
