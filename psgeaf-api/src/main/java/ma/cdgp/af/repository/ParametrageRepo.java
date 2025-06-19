package ma.cdgp.af.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ma.cdgp.af.entity.ParametrageCollection;

@Repository
public interface ParametrageRepo extends JpaRepository<ParametrageCollection, Long> {
	@Query("select distinct p from ParametrageCollection p where  p.partenaire = :part ")
	public ParametrageCollection findByPartenaire( @Param("part") String part);

}
