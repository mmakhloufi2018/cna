package ma.cdgep.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ma.cdgep.demande.entity.LotDemandeBruteEntity;

@Repository
public interface LotDemandeBruteRepository extends CrudRepository<LotDemandeBruteEntity, Long> {

}
