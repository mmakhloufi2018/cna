package ma.cdgep.eligibilite.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "ELIGIBILITE_LOT_DEMANDE")
public class EligibiliteLotDemandeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "REFERENCE")
	private String reference;
	
	@Column(name = "STATUS_CODE")
	private Integer statusCode;
	
	@Column(name = "MOTIF")
	private String motif;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "STATUS_RETOUR")
	private String statusRetour;
	
	@Column(name = "ERROR_RETOUR")
	private String errorRetour;
	
	@Column(name = "ETAT")
	private String etat;
	
	@Column(name = "ECHEANCE")
	private String echeance;
	
	@OneToMany(mappedBy = "lot", fetch = FetchType.EAGER)
	private List<EligibiliteDemandeEntity> demandes;

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

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getMotif() {
		return motif;
	}

	public void setMotif(String motif) {
		this.motif = motif;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<EligibiliteDemandeEntity> getDemandes() {
		return demandes;
	}

	public void setDemandes(List<EligibiliteDemandeEntity> demandes) {
		this.demandes = demandes;
	}

	public String getStatusRetour() {
		return statusRetour;
	}

	public void setStatusRetour(String statusRetour) {
		this.statusRetour = statusRetour;
	}

	public String getErrorRetour() {
		return errorRetour;
	}

	public void setErrorRetour(String errorRetour) {
		this.errorRetour = errorRetour;
	}

	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	public String getEcheance() {
		return echeance;
	}

	public void setEcheance(String echeance) {
		this.echeance = echeance;
	}

}
