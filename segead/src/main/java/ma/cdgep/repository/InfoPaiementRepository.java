package ma.cdgep.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ma.cdgep.paiement.entity.InfoPaiementEntity;

@Repository
public interface InfoPaiementRepository extends CrudRepository<InfoPaiementEntity, Long> {
//	@Query(value = "select p.REFERENCE_PAIEMENT,  dp.RUBRIQUE ,nature , mois , echeance , nvl(me.idcs,p.REFERENCE_MENAGE) membre , l.TYPE_PRESTATION, p.dossier_id "  
//	+" from paiement p join detail_paiement dp on p.dossier_id = dp.dossier_id "
//	+" join droit d on d.id = dp.DROIT_ID"
//	+" join detail_droit dd on d.id = dd.DROIT_ID"
//	+" join lot_paiement l on l.ID = p.LOT_PAIEMENT_ID"
//	+" left join m_enfant me on dd.MEMBRE  = me.id"
//	+" where  p.REFERENCE_PAIEMENT = :referencePaiement " , nativeQuery = true)
//	@Query(value = "select p.REFERENCE_PAIEMENT,  dp.RUBRIQUE ,nature , mois , echeance , nvl(me.idcs,p.REFERENCE_MENAGE) membre , l.TYPE_PRESTATION, p.dossier_id "  
//	+" from paiement p join detail_paiement dp on p.dossier_id = dp.dossier_id "
//	+" join droit d on d.id = dp.DROIT_ID"
//	+" join detail_droit dd on d.id = dd.DROIT_ID"
//	+" join lot_paiement l on l.ID = p.LOT_PAIEMENT_ID"
//	+" left join m_enfant me on dd.MEMBRE  = me.id"
//	+" where  p.REFERENCE_PAIEMENT = :referencePaiement " , nativeQuery = true)
	List<InfoPaiementEntity> findByReferencePaiement(String referencePaiement);
	
	
	
	@Query(value =" select distinct idcs from m_demandeur where idcs = :idcs", nativeQuery = true)
	public String getDemandeur(String idcs);
	
	
	@Query(value =" select id from paiement where REFERENCE_MENAGE = :idcs and REFERENCE_PAIEMENT = :refPaiement", nativeQuery = true)
	public Long getPaiement(String idcs, String refPaiement);
	
	
	
}
