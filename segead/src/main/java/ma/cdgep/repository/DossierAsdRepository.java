package ma.cdgep.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ma.cdgep.dossier.entity.DossierAsdEntity;

@Repository
public interface DossierAsdRepository extends CrudRepository<DossierAsdEntity, Long> {
	List<DossierAsdEntity> findByImmatriculationInAndDateClotureIsNull(List<String> immatriculation);
	
	@Query(value = "select distinct idcs from  M_Demandeur de join DOSSIER_ASD d on D.ID = DE.DOSSIER_ASD_ID   where idcs in(:listIdcs)  and D.DATE_CLOTURE is null  union    "
			+ "     select distinct idcs from  M_Conjoint c join DOSSIER_ASD d on D.ID = c.DOSSIER_ASD_ID   where idcs in(:listIdcs) and D.DATE_CLOTURE is null union    "
			+ "     select distinct idcs from  M_Enfant e join DOSSIER_ASD d on D.ID = e.DOSSIER_ASD_ID   where idcs in(:listIdcs)  and D.DATE_CLOTURE is null union "
			+ "      select distinct idcs from  M_Autre_Membre a  join DOSSIER_ASD d on D.ID = a.DOSSIER_ASD_ID   where idcs in(:listIdcs) and D.DATE_CLOTURE is null  ", nativeQuery = true)
	public List<String> getExistIdcs(List<String> listIdcs);

	@Query(value = "SELECT HASH FROM transaction  WHERE TO_CHAR (TO_DATE (t.DATE_STATUT, 'dd/MM/YYYY HH24:MI:SS'), 'dd/MM/YYYY')  :dateTrasaction "
			+ "            ORDER BY TO_DATE (t.DATE_STATUT, 'dd/MM/YYYY HH24:MI:SS') ASC, identifiant ASC ", nativeQuery = true )
	
	public String[] getHashTransactions(String dateTrasaction);
	
	@Query(value = " select distinct idcs from  M_Demandeur de join DOSSIER_ASD d on D.ID = DE.DOSSIER_ASD_ID   where idcs in(:idcs)  and D.DATE_CLOTURE is null ", nativeQuery = true )
	List<String> getExistIdcsDemandeur(List<String> idcs);
	
	@Query(value = "select d.reference ,md.idcs   from dossier_asd d join m_demandeur md on md.dossier_asd_id = d.id "
			+ "where exists ( "
			+ " select 1 from droit dr where dr.dossier_asd_id = d.id  "
			+ " and dr.montant = 0  "
			+ " and dr.id in  "
			+ "( select max(dr1.id) from droit dr1  where dr1.dossier_asd_id = dr.DOSSIER_ASD_ID and  "
			+ "    not exists(select 1 from detail_droit dd where dd.RUBRIQUE = 'PRIME_NAISSANCE' and dd.droit_id = dr1.id ))  ) "
			+ "and reference in (:references)" , nativeQuery = true)
	List<RefIdcsDossier> getDossiersDroitZero(List<String> references);
	
	@Query(value = "select da.reference , md.idcs , da.TYPE_PRESTATION PRESTATION, da.date_cloture datecloture, mc.idcs idcsconjoint,"
			+ " md.GENRE genre , md.code_menage_rsu codemenage ,md.cin , md.etat_matrimonial etatmatrimonial, "
			+ "			 (select LISTAGG( ';'||idcs || ';', '') WITHIN GROUP (ORDER BY idcs) from m_enfant me where me.dossier_asd_id = da.id) idcsenfants , "
			+ "			 (select LISTAGG( ';'||idcs || ';', '') WITHIN GROUP (ORDER BY idcs) from m_conjoint mc where mc.dossier_asd_id = da.id)  idcsconjoints	   "
			+ "from dossier_asd da join m_demandeur md on da.id = md.dossier_asd_id "
			+ "left join m_conjoint mc on mc.dossier_asd_id = da.id "
			+ "where da.reference in(:references) ", nativeQuery = true)
	List<InfoControlAjoutMembre> getInfoControlAjoutMembre(List<String> references);
	

}