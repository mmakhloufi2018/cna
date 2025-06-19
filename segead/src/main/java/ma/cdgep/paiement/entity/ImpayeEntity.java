package ma.cdgep.paiement.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "IMAPYE")
public class ImpayeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "REFERENCE_PAIEMENT")
	private String referencePaiement;

	@Column(name = "IDCS")
	private String idcs;

	@Column(name = "MONTANT")
	private Double montant;

	@Column(name = "ECHEANCE")
	private String echeance;

	@ManyToOne
	private LotImpayesEntity lotImpayes;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReferencePaiement() {
		return referencePaiement;
	}

	public void setReferencePaiement(String referencePaiement) {
		this.referencePaiement = referencePaiement;
	}

	public String getIdcs() {
		return idcs;
	}

	public void setIdcs(String idcs) {
		this.idcs = idcs;
	}

	public Double getMontant() {
		return montant;
	}

	public void setMontant(Double montant) {
		this.montant = montant;
	}

	public String getEcheance() {
		return echeance;
	}

	public void setEcheance(String echeance) {
		this.echeance = echeance;
	}

	public LotImpayesEntity getLotImpayes() {
		return lotImpayes;
	}

	public void setLotImpayes(LotImpayesEntity lotImpayes) {
		this.lotImpayes = lotImpayes;
	}
	
}
