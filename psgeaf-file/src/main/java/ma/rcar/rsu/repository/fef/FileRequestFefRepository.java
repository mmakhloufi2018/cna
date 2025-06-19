package ma.rcar.rsu.repository.fef;


import ma.rcar.rsu.entity.fef.FileRequestFefEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * @author BAKHALED Ibrahim.
 *
 */



@Repository
public interface FileRequestFefRepository extends JpaRepository<FileRequestFefEntity, Long> {

    @Query(value = "select distinct f from ma.rcar.rsu.entity.fef.FileRequestFefEntity f where f.fileName  = :fileName")
    public FileRequestFefEntity getByFileName(@Param("fileName") String fileName);
}
