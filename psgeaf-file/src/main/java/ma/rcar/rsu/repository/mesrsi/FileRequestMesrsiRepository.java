package ma.rcar.rsu.repository.mesrsi;



import ma.rcar.rsu.entity.mesrsi.FileRequestMesrsiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



/**
 * @author BAKHALED Ibrahim.
 *
 */


@Repository
public interface FileRequestMesrsiRepository extends JpaRepository<FileRequestMesrsiEntity, Long> {


    @Query(value = "select distinct f from ma.rcar.rsu.entity.mesrsi.FileRequestMesrsiEntity f where f.fileName  = :fileName")
    public FileRequestMesrsiEntity getByFileName(@Param("fileName") String fileName);
}
