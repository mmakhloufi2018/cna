package ma.rcar.rsu.repository.fef;


import ma.rcar.rsu.entity.fef.DetailsFileRequestFefEntity;
import ma.rcar.rsu.entity.mesrsi.DetailsFileRequestMesrsiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * @author BAKHALED Ibrahim.
 *
 */



@Repository
public interface DetailsFileRequestFefRepository extends JpaRepository<DetailsFileRequestFefEntity, Long> {
}
