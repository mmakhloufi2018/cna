package ma.cdgep.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ma.cdgep.demande.entity.RecoursEntity;

@Repository
public interface RecoursRepository extends CrudRepository<RecoursEntity, Long> {

}
