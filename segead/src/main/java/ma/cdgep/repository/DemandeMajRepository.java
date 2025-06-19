package ma.cdgep.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ma.cdgep.demande.entity.DemandeMajEntity;

@Repository
public interface DemandeMajRepository extends CrudRepository<DemandeMajEntity, Long> {

}
