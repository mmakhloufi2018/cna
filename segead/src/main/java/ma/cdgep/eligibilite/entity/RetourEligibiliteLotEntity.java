package ma.cdgep.eligibilite.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "RETOUR_ELIGIBILITE_LOT")
public class RetourEligibiliteLotEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "STATUT")
	private String statut;

	@Column(name = "REFERENCE")
	private String reference;

	@Column(name = "ID_LOT")
	private Long idLot;

	@Column(name = "MESSAGE")
	@Lob
	private String message;

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

	public Long getIdLot() {
		return idLot;
	}

	public void setIdLot(Long idLot) {
		this.idLot = idLot;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

}
