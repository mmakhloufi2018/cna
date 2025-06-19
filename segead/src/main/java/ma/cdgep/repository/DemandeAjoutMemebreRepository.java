package ma.cdgep.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ma.cdgep.demande.entity.DemandeAjoutMembreEntity;

@Repository
public interface DemandeAjoutMemebreRepository extends CrudRepository<DemandeAjoutMembreEntity, Long> {

}
