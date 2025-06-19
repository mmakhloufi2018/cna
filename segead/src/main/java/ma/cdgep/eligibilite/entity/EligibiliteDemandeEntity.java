package ma.cdgep.eligibilite.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "ELIGIBILITE_DEMANDE")
public class EligibiliteDemandeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "REFERENCE")
	private String reference;
	@Column(name = "STATUSCODE")
	private Integer statusCode;
	@Column(name = "MOTIF")
	private String motif;
	@Column(name = "ERROR")
	private String error;
	@Column(name = "STATUSCNSS")
	private Integer statusCnss;
	
	@JsonProperty("partenaire")
	private String partenaire;
	
	@ManyToOne
	private EligibiliteLotDemandeEntity lot;
	
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
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public EligibiliteLotDemandeEntity getLot() {
		return lot;
	}
	public void setLot(EligibiliteLotDemandeEntity lot) {
		this.lot = lot;
	}
	public Integer getStatusCnss() {
		return statusCnss;
	}
	public void setStatusCnss(Integer statusCnss) {
		this.statusCnss = statusCnss;
	}
	public String getPartenaire() {
		return partenaire;
	}
	public void setPartenaire(String partenaire) {
		this.partenaire = partenaire;
	}

}
