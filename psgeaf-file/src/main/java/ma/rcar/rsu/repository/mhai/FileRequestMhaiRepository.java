package ma.rcar.rsu.repository.mhai;



import ma.rcar.rsu.entity.mhai.FileRequestMhaiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * @author BAKHALED Ibrahim.
 *
 */


@Repository
public interface FileRequestMhaiRepository extends JpaRepository<FileRequestMhaiEntity, Long> {

    @Query(value = "select distinct f from ma.rcar.rsu.entity.mhai.FileRequestMhaiEntity f where f.fileName  = :fileName")
    public FileRequestMhaiEntity getByFileName(@Param("fileName") String fileName);
}
