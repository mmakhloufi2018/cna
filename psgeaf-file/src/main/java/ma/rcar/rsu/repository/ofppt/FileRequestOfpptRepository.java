package ma.rcar.rsu.repository.ofppt;


import ma.rcar.rsu.entity.ofppt.FileRequestOfpptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



/**
 * @author BAKHALED Ibrahim.
 *
 */

@Repository
public interface FileRequestOfpptRepository extends JpaRepository<FileRequestOfpptEntity, Long> {


    @Query(value = "select distinct f from ma.rcar.rsu.entity.ofppt.FileRequestOfpptEntity f where f.fileName  = :fileName")
    public FileRequestOfpptEntity getByFileName(@Param("fileName") String fileName);
}
