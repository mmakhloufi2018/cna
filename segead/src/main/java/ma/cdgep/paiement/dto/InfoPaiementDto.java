package ma.cdgep.paiement.dto;

import ma.cdgep.paiement.entity.InfoPaiementEntity;
import ma.cdgep.utils.Utils;

public class InfoPaiementDto {

	private String referencePaiement;
	private String rubrique;

	private String nature;

	private String mois;

	private String echeance;
	private String membre;
	private String typePrestation;
	
	private String montant;

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

	
	public String getMontant() {
		return montant;
	}

	public void setMontant(String montant) {
		this.montant = montant;
	}

	public InfoPaiementDto(String referencePaiement, String rubrique, String nature, String mois, String echeance,
			String membre, String typePrestation, String montant) {
		super();
		this.referencePaiement = referencePaiement;
		this.rubrique = rubrique;
		this.nature = nature;
		this.mois = mois;
		this.echeance = echeance;
		this.membre = membre;
		this.typePrestation = typePrestation;
		this.montant = montant;
	}

	public InfoPaiementDto() {
		super();
	}

	public static InfoPaiementDto from(InfoPaiementEntity in) {
		if (in == null)
			return null;
		return new InfoPaiementDto(in.getReferencePaiement(), in.getRubrique(), in.getNature(), in.getMois(),
				in.getEcheance(), in.getMembre(), in.getTypePrestation(), Utils.roundValue(in.getMontant()) );
	}

}
