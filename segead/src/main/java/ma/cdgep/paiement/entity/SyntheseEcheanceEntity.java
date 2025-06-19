package ma.cdgep.paiement.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import ma.cdgep.paiement.dto.ReponseSyntheseDto;
import ma.cdgep.paiement.dto.SyntheseEncheanceEnvoyeeProj;

@Entity
@Table(name = "SYNTHESE_ECHEANCE")
public class SyntheseEcheanceEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "REFERENCE_ECHEANCE")
	private String referenceEcheance;

	@Column(name = "TYPE_PRESTATION")
	private String typePrestation;

	@Column(name = "NOMBRE_LOT_ENVOYE")
	private Integer nombreLotEnvoye;

	@Column(name = "NOMBRE_LIGNES_ENVOYE")
	private Integer nombreLigneEnvoye;

	@Column(name = "MONTANT_TOTAL_ENVOYE")
	private Double montantTotalEnvoye;

	@Column(name = "NOMBRE_LOT_RECU")
	private Integer nombreLotRecu;

	@Column(name = "NOMBRE_LIGNES_RECU")
	private Integer nombreLigneRecu;

	@Column(name = "MONTANT_TOTAL_RECU")
	private Double montantTotalRecu;

	public SyntheseEcheanceEntity(Long id, String referenceEcheance, String typePrestation, Integer nombreLotEnvoye,
			Integer nombreLigneEnvoye, Double montantTotalEnvoye, Integer nombreLotRecu, Integer nombreLigneRecu,
			Double montantTotalRecu) {
		super();
		this.id = id;
		this.referenceEcheance = referenceEcheance;
		this.typePrestation = typePrestation;
		this.nombreLotEnvoye = nombreLotEnvoye;
		this.nombreLigneEnvoye = nombreLigneEnvoye;
		this.montantTotalEnvoye = montantTotalEnvoye;
		this.nombreLotRecu = nombreLotRecu;
		this.nombreLigneRecu = nombreLigneRecu;
		this.montantTotalRecu = montantTotalRecu;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Integer getNombreLotEnvoye() {
		return nombreLotEnvoye;
	}

	public void setNombreLotEnvoye(Integer nombreLotEnvoye) {
		this.nombreLotEnvoye = nombreLotEnvoye;
	}

	public Integer getNombreLigneEnvoye() {
		return nombreLigneEnvoye;
	}

	public void setNombreLigneEnvoye(Integer nombreLigneEnvoye) {
		this.nombreLigneEnvoye = nombreLigneEnvoye;
	}

	public Double getMontantTotalEnvoye() {
		return montantTotalEnvoye;
	}

	public void setMontantTotalEnvoye(Double montantTotalEnvoye) {
		this.montantTotalEnvoye = montantTotalEnvoye;
	}

	public Integer getNombreLotRecu() {
		return nombreLotRecu;
	}

	public void setNombreLotRecu(Integer nombreLotRecu) {
		this.nombreLotRecu = nombreLotRecu;
	}

	public Integer getNombreLigneRecu() {
		return nombreLigneRecu;
	}

	public void setNombreLigneRecu(Integer nombreLigneRecu) {
		this.nombreLigneRecu = nombreLigneRecu;
	}

	public Double getMontantTotalRecu() {
		return montantTotalRecu;
	}

	public void setMontantTotalRecu(Double montantTotalRecu) {
		this.montantTotalRecu = montantTotalRecu;
	}

	public static SyntheseEcheanceEntity to(String referenceEcheance, String typePrestation, ReponseSyntheseDto dto,
			SyntheseEncheanceEnvoyeeProj syntheseEncheanceEnvoyeeProj) {

		if (dto == null)
			return null;

		SyntheseEcheanceEntity entity = new SyntheseEcheanceEntity(null, referenceEcheance, typePrestation,
				syntheseEncheanceEnvoyeeProj.getNombreLotEnvoye(), syntheseEncheanceEnvoyeeProj.getNombreLigneEnvoyee(), 
				syntheseEncheanceEnvoyeeProj.getMontantTotalEnvoye(),
				Integer.valueOf(dto.getSyntheseDto().getNombreLots()),
				Integer.valueOf(dto.getSyntheseDto().getNombreLignes()),
				Double.valueOf(dto.getSyntheseDto().getMontantTotal()));

		return entity;
	}

}
