package ma.cdgp.af.repository;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.cdgp.af.entity.RetourTracabilite;

@Repository
public interface RetourTracabiliteRepository extends JpaRepository<RetourTracabilite, Long> {

	Set<RetourTracabilite> findByRetourId(Long idRet);

	@Query("select h from RetourTracabilite h order by  dateEvent desc")
	Page<RetourTracabilite> find20LastKO(Pageable pageable);

}
