package ma.cdgep.paiement.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "LOT_PAIEMENT")
public class LotPaiementEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "DATE_ECHEANCE")
	private Date dateEcheance;
	
	@Column(name = "REFERENCE_ECHEANCE")
	private String referenceEcheance;
	
	@Column(name = "TYPE_PRESTATION")
	private String typePrestation;
	
	@Column(name = "NUMERO_LOT")
	private Integer numeroLot;
	
	@OneToMany(mappedBy = "lotPaiement", fetch = FetchType.EAGER)
	Set<PaiementEntity> paiements;
	
	@Column(name = "STATUT")
	private String statut;
	
	@Column(name = "REFERENCE_CNSS")
	private String referenceCnss;
	
	@Column(name = "MESSAGE")
	private String message;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDateEcheance() {
		return dateEcheance;
	}

	public void setDateEcheance(Date dateEcheance) {
		this.dateEcheance = dateEcheance;
	}

	public String getReferenceEcheance() {
		return referenceEcheance;
	}

	public void setReferenceEcheance(String referenceEcheance) {
		this.referenceEcheance = referenceEcheance;
	}

	public String getTypePrestation() {
		return typePrestation;
	}

	public void setTypePrestation(String typePrestation) {
		this.typePrestation = typePrestation;
	}

	public Integer getNumeroLot() {
		return numeroLot;
	}

	public void setNumeroLot(Integer numeroLot) {
		this.numeroLot = numeroLot;
	}

	public Set<PaiementEntity> getPaiements() {
		return paiements;
	}

	public void setPaiements(Set<PaiementEntity> paiements) {
		this.paiements = paiements;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public String getReferenceCnss() {
		return referenceCnss;
	}

	public void setReferenceCnss(String referenceCnss) {
		this.referenceCnss = referenceCnss;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
