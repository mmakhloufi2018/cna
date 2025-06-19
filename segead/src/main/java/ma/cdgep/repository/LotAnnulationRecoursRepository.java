package ma.cdgep.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ma.cdgep.demande.entity.LotAnnulationRecoursEntity;

@Repository
public interface LotAnnulationRecoursRepository extends CrudRepository<LotAnnulationRecoursEntity, Long> {


}
