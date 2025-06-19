package ma.rcar.rsu.repository.mesrsi;

import ma.rcar.rsu.entity.mesrsi.DetailsFileRequestMesrsiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



/**
 * @author BAKHALED Ibrahim.
 *
 */


@Repository
public interface DetailsFileRequestMesrsiRepository extends JpaRepository<DetailsFileRequestMesrsiEntity, Long> {
}
