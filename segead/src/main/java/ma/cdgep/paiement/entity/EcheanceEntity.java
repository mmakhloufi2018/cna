package ma.cdgep.paiement.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ma.cdgep.demande.entity.LotDemandeEntity;

@Entity
@Table(name = "ECHEANCE")
public class EcheanceEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "REFERENCE")
	private String reference;
	@Column(name = "MOIS")
	private String mois;
	@Column(name = "TYPE")
	private String type;
	@Column(name = "ANNE")
	private String annee;
	@Column(name = "STATUT")
	private String statut;
	@Column(name = "NBR_TOTAL_BENEF")
	private Integer nombreTotalBenef;
	@Column(name = "MNT_TOTAL_ECHEANCE")
	private Double montantTotalEcheance;
	@Column(name = "NBR_LOT")
	private Integer nombreLot;
	
	@Column(name = "RECEPTION_LOT_AUTORISE")
	private String receptionLotAutorise;
	
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "echeance", cascade = CascadeType.ALL)
	private List<LotDemandeEntity> lots;
	
	public EcheanceEntity(Long id, String reference, String mois, String type, String annee, String statut,
			Integer nombreTotalBenef, Double montantTotalEcheance, Integer nombreLot) {
		super();
		this.id = id;
		this.reference = reference;
		this.mois = mois;
		this.type = type;
		this.annee = annee;
		this.statut = statut;
		this.nombreTotalBenef = nombreTotalBenef;
		this.montantTotalEcheance = montantTotalEcheance;
		this.nombreLot = nombreLot;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getMois() {
		return mois;
	}
	public void setMois(String mois) {
		this.mois = mois;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAnnee() {
		return annee;
	}
	public void setAnnee(String annee) {
		this.annee = annee;
	}
	public String getStatut() {
		return statut;
	}
	public void setStatut(String statut) {
		this.statut = statut;
	}
	public Integer getNombreTotalBenef() {
		return nombreTotalBenef;
	}
	public void setNombreTotalBenef(Integer nombreTotalBenef) {
		this.nombreTotalBenef = nombreTotalBenef;
	}
	public Double getMontantTotalEcheance() {
		return montantTotalEcheance;
	}
	public void setMontantTotalEcheance(Double montantTotalEcheance) {
		this.montantTotalEcheance = montantTotalEcheance;
	}
	public Integer getNombreLot() {
		return nombreLot;
	}
	public void setNombreLot(Integer nombreLot) {
		this.nombreLot = nombreLot;
	}
	public String getReceptionLotAutorise() {
		return receptionLotAutorise;
	}
	public void setReceptionLotAutorise(String receptionLotAutorise) {
		this.receptionLotAutorise = receptionLotAutorise;
	}
	public List<LotDemandeEntity> getLots() {
		return lots;
	}
	public void setLots(List<LotDemandeEntity> lots) {
		this.lots = lots;
	}
	public EcheanceEntity() {
		super();
	}
	public EcheanceEntity(Long id) {
		super();
		this.id = id;
	}
	

}
