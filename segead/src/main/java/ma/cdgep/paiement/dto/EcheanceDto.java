package ma.cdgep.paiement.dto;

import ma.cdgep.paiement.entity.EcheanceEntity;

public class EcheanceDto {

	private String reference;
	private String annee;
	private String mois;
	private String statut;
	private Integer nombreTotalBeneficiare;
	private Double montantTotalEcheance;
	private Integer nombreLot;
	private String type;

	public EcheanceDto(String reference, String annee, String statut, Integer nombreTotalBeneficiare, Double montantTotalEcheance,
			Integer nombreLot, String mois, String type) {
		super();
		this.reference = reference;
		this.annee = annee;
		this.statut = statut;
		this.nombreTotalBeneficiare = nombreTotalBeneficiare;
		this.montantTotalEcheance = montantTotalEcheance;
		this.nombreLot = nombreLot;
		this.mois = mois;
		this.type = type;
	}

	public EcheanceDto() {
		super();
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
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

	public static EcheanceDto from(EcheanceEntity in) {
		if (in == null)
			return null;
		return new EcheanceDto(in.getReference(), in.getAnnee(), in.getStatut(), in.getNombreTotalBenef(),
				in.getMontantTotalEcheance(), in.getNombreLot(), in.getMois(), in.getType());
	}

	public Integer getNombreTotalBeneficiare() {
		return nombreTotalBeneficiare;
	}

	public void setNombreTotalBeneficiare(Integer nombreTotalBeneficiare) {
		this.nombreTotalBeneficiare = nombreTotalBeneficiare;
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

}
