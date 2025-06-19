package ma.cdgep.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ma.cdgep.demande.entity.BlocEntity;

@Repository
public interface BlocRepository extends CrudRepository<BlocEntity, Long> {
	

	@Query(value = "SELECT HASH FROM transaction  WHERE TO_CHAR (TO_DATE (DATE_STATUT, 'dd/MM/YYYY HH24:MI:SS'), 'dd/MM/YYYY') =  :dateTrasaction "
			+ "            ORDER BY TO_DATE (DATE_STATUT, 'dd/MM/YYYY HH24:MI:SS') ASC, identifiant ASC ", nativeQuery = true )
	
	public String[] getHashTransactions(String dateTrasaction);
	

}
