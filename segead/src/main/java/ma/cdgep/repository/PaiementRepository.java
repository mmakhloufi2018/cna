package ma.cdgep.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ma.cdgep.paiement.dto.BeneficiareDto;
import ma.cdgep.paiement.entity.PaiementEntity;

@Repository
public interface PaiementRepository extends CrudRepository<PaiementEntity, Long> {
	
	@Procedure(procedureName = "pr_alimenter_nv_doss_ouvert", outputParameterName = "p_message")
	String alimenterNvDossierOuvert(@Param("p_mois_annee") String moisAnnee);
	
	@Procedure(procedureName = "pr_alimenter_nv_doss_cloture", outputParameterName = "p_message")
	String alimenterNvDossieCloture(@Param("p_mois_annee") String moisAnnee);
	
	@Procedure(procedureName = "pr_alimenter_candidat", outputParameterName = "p_message")
	String alimenterCandidat(@Param("p_mois_annee") String moisAnnee);
	
	@Query(value = " select p.REFERENCE_PAIEMENT referencePaiement, p.MONTANT_OPERATION MONTANTOPERATION,dp.mois periode,me.idcs,dp.rubrique, 'ENFANT' type , dd.MONTANT ,  "
			+ "			 dd.OFFSET from paiement p join detail_paiement dp on dp.DOSSIER_ID = p.DOSSIER_ID and to_char(p.DATE_DEBUT,'MMyyyy') = dp.echeance  "
			+ "			 JOIN lot_paiement l on l.id = p.LOT_PAIEMENT_ID "
			+ "			 join droit dr on dr.id = dp.DROIT_ID  "
			+ "			 join detail_droit dd on dd.droit_id = dr.id  "
			+ "			 join m_enfant me on me.id = dd.MEMBRE  "
			+ "			where p.REFERENCE_PAIEMENT = :reference and l.type_prestation = 'E' and dp.rubrique = 'ENCOURS' "
			+ "			union "
			+ "			select p.REFERENCE_PAIEMENT referencePaiement, p.MONTANT_OPERATION MONTANTOPERATION,dp.mois periode,md.idcs,dp.rubrique, 'DEMENDEUR' type , dp.MONTANT ,  "
			+ "			 null OFFSET from paiement p join detail_paiement dp on dp.DOSSIER_ID = p.DOSSIER_ID and to_char(p.DATE_DEBUT,'MMyyyy') = dp.echeance  "
			+ "			join m_demandeur md on md.DOSSIER_ASD_ID = p.DOSSIER_ID "
			+ "			JOIN lot_paiement l on l.id = p.LOT_PAIEMENT_ID "
			+ "			where reference_paiement = :reference and l.type_prestation = 'E' and dp.rubrique <> 'ENCOURS' "
			+ "			union			 "
			+ "			select p.REFERENCE_PAIEMENT referencePaiement, p.MONTANT_OPERATION MONTANTOPERATION,dp.mois periode,md.idcs,dp.rubrique, 'DEMANDEUR' type , dp.MONTANT ,  "
			+ "			null OFFSET from paiement p join detail_paiement dp on dp.DOSSIER_ID = p.DOSSIER_ID and to_char(p.DATE_DEBUT,'MMyyyy') = dp.echeance  "
			+ "			 JOIN lot_paiement l on l.id = p.LOT_PAIEMENT_ID "
			+ "			 join m_demandeur md on md.DOSSIER_ASD_ID = p.DOSSIER_ID "
			+ "			where l.type_prestation = 'F' and p.REFERENCE_PAIEMENT =:reference  " , nativeQuery = true)
	List<PaiementCnss> getDetailPaiement(String reference);
	
	
	@Query( value=" select id , to_char(to_date(echeance, 'MM/yyyy'), 'yyyy/MM/dd'), numero from lot_indu where etat ='N' ", nativeQuery = true)
	List<Object[]> getBenifIndu();

	@Query(value =" select i.idcs , m.cin from indu i, m_demandeur m where nvl(solde,0) <> 1 and m.idcs=i.idcs and m.dossier_asd_id = i.dossier_asd_id and lot_indu_id = :idLot", nativeQuery = true)
	List<BeneficiareDto> getbenefIndu(BigDecimal idLot);

	@Modifying
	@Transactional
	@Query(value =" update indu i set i.code_reponse= :codeResponse, i.lib_response= :libResponse where i.lot_indu_id = :idLot and i.idcs= :idcs", nativeQuery = true)
	void updateIndu(String idcs, String codeResponse, String libResponse, BigDecimal idLot);

	@Modifying
	@Transactional
	@Query(value ="update lot_indu set etat = :etat where id = :id", nativeQuery = true)
	void updateLotIndu(String etat, BigDecimal id);
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	@Query( value=" select id,reference  from LotclotureBenifIndu where etat ='N' ", nativeQuery = true)
	List<Object[]> getClotureBenifIndu();

	@Query(value =" select cin, idcs from clotureBenifIndu where lot_id = :idLot", nativeQuery = true)
	List<BeneficiareDto> getClotureIndu(BigDecimal idLot);

	
	@Modifying
	@Transactional
	@Query(value =" update clotureBenifIndu i set i.code_reponse= :codeResponse, i.lib_response= :libResponse where i.lot_id = :idLot and i.idcs= :idcs", nativeQuery = true)
	void updateclotureBenifIndu(String idcs, String codeResponse, String libResponse, BigDecimal idLot);

	@Modifying
	@Transactional
	@Query(value ="update LotclotureBenifIndu set etat = :etat where id = :id", nativeQuery = true)
	void updateLotclotureBenifIndu(String etat, BigDecimal id);
	
}
