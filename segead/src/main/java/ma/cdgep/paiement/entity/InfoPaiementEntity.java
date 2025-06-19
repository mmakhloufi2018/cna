package ma.cdgep.paiement.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "INFO_PAIEMENT")
public class InfoPaiementEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "REFERENCE_PAIEMENT")
	private String referencePaiement;

	@Column(name = "RUBRIQUE")
	private String rubrique;

	@Column(name = "NATURE")
	private String nature;

	@Column(name = "MOIS")
	private String mois;

	@Column(name = "ECHEANCE")
	private String echeance;
	@Column(name = "MEMBRE")
	private String membre;
	@Column(name = "TYPE_PRESTATION")
	private String typePrestation;

	@Column(name = "MONTANT")
	private Double montant;

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

	public String getRubrique() {
		return rubrique;
	}

	public void setRubrique(String rubrique) {
		this.rubrique = rubrique;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public String getMois() {
		return mois;
	}

	public void setMois(String mois) {
		this.mois = mois;
	}

	public String getEcheance() {
		return echeance;
	}

	public void setEcheance(String echeance) {
		this.echeance = echeance;
	}

	public String getMembre() {
		return membre;
	}

	public void setMembre(String membre) {
		this.membre = membre;
	}

	public String getTypePrestation() {
		return typePrestation;
	}

	public void setTypePrestation(String typePrestation) {
		this.typePrestation = typePrestation;
	}

	public InfoPaiementEntity(Long id, String referencePaiement, String rubrique, String nature, String mois,
			String echeance, String membre, String typePrestation, Double montant) {
		super();
		this.id = id;
		this.referencePaiement = referencePaiement;
		this.rubrique = rubrique;
		this.nature = nature;
		this.mois = mois;
		this.echeance = echeance;
		this.membre = membre;
		this.typePrestation = typePrestation;
		this.montant = montant;
	}

	public InfoPaiementEntity() {
		super();
	}

	public Double getMontant() {
		return montant;
	}

	public void setMontant(Double montant) {
		this.montant = montant;
	}

}
