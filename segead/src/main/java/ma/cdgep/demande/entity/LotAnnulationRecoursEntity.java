package ma.cdgep.demande.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ma.cdgep.paiement.entity.EcheanceEntity;

@Entity
@Table(name = "LOT_ANNULATION_RECOURS")
public class LotAnnulationRecoursEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "DATE_LOT")
	private Date dateLot;

	@Column(name = "REFERENCE_LOT")
	private String referenceLot;

	@Column(name = "TOTAL_DOSSIERS")
	private Integer totalDossiers;

	@Column(name = "TOTAL_ACCEPTES")
	private Integer totalAcceptes;

	@Column(name = "TOTAL_REJETES")
	private Integer totalRejetes;

	@Column(name = "STATUT")
	private String statut;

	@Column(name = "MOTIF")
	private String motif;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private EcheanceEntity echeance;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lot", cascade = CascadeType.ALL)
	private List<AnnulationRecoursEntity> demandes;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDateLot() {
		return dateLot;
	}

	public void setDateLot(Date dateLot) {
		this.dateLot = dateLot;
	}

	public String getReferenceLot() {
		return referenceLot;
	}

	public void setReferenceLot(String referenceLot) {
		this.referenceLot = referenceLot;
	}

	public Integer getTotalDossiers() {
		return totalDossiers;
	}

	public void setTotalDossiers(Integer totalDossiers) {
		this.totalDossiers = totalDossiers;
	}

	public Integer getTotalAcceptes() {
		return totalAcceptes;
	}

	public void setTotalAcceptes(Integer totalAcceptes) {
		this.totalAcceptes = totalAcceptes;
	}

	public Integer getTotalRejetes() {
		return totalRejetes;
	}

	public void setTotalRejetes(Integer totalRejetes) {
		this.totalRejetes = totalRejetes;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public String getMotif() {
		return motif;
	}

	public void setMotif(String motif) {
		this.motif = motif;
	}

	public List<AnnulationRecoursEntity> getDemandes() {
		return demandes;
	}

	public void setDemandes(List<AnnulationRecoursEntity> demandes) {
		this.demandes = demandes;
	}

	public EcheanceEntity getEcheance() {
		return echeance;
	}

	public void setEcheance(EcheanceEntity echeance) {
		this.echeance = echeance;
	}
	
	

}
