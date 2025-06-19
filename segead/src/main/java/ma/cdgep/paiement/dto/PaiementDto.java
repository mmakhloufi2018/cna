package ma.cdgep.paiement.dto;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonProperty;

import ma.cdgep.paiement.entity.PaiementEntity;
import ma.cdgep.utils.Utils;

public class PaiementDto {

	@JsonProperty("refepaie")
	private String referencePaiement;
	@JsonProperty("refefami")
	private String referenceMenage;
	@JsonProperty("montoper")
	private Double MontantOperation;
	@JsonProperty("datedebu")
	private String dateDebut;
	@JsonProperty("date_fin")
	private String dateFin;
	@JsonProperty("flagsusp")
	private Integer flagSuspension;
	
	
//	@JsonProperty("refecnss")
//	private String referenceCnss;
	@JsonProperty("refedema")
	private String referenceDemande;

	
	@JsonProperty( "score")
	private Double score;

	public PaiementDto() {
		super();
	}

	public String getReferencePaiement() {
		return referencePaiement;
	}

	public void setReferencePaiement(String referencePaiement) {
		this.referencePaiement = referencePaiement;
	}

	public String getReferenceMenage() {
		return referenceMenage;
	}

	public void setReferenceMenage(String referenceMenage) {
		this.referenceMenage = referenceMenage;
	}

	public Double getMontantOperation() {
		return MontantOperation;
	}

	public void setMontantOperation(Double montantOperation) {
		MontantOperation = montantOperation;
	}

	public String getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(String dateDebut) {
		this.dateDebut = dateDebut;
	}

	public String getDateFin() {
		return dateFin;
	}

	public void setDateFin(String dateFin) {
		this.dateFin = dateFin;
	}

	public Integer getFlagSuspension() {
		return flagSuspension;
	}

	public void setFlagSuspension(Integer flagSuspension) {
		this.flagSuspension = flagSuspension;
	}
	
	public static PaiementDto from(PaiementEntity in) {
		if (in == null)
			return null;
		PaiementDto out = new PaiementDto();
		out.setDateDebut(Utils.dateToString(in.getDateDebut(), Utils.FOURMAT_DATE_STRING));
		out.setDateFin(Utils.dateToString(in.getDateFin(), Utils.FOURMAT_DATE_STRING));
		out.setFlagSuspension(in.getFlagSuspension());
		out.setMontantOperation(in.getMontantOperation());
		out.setReferenceMenage(in.getReferenceMenage());
		out.setReferencePaiement(in.getReferencePaiement());
//		out.setReferenceCnss(in.getReferenceCnss());
		out.setScore(in.getScore());
		out.setReferenceDemande(in.getReferenceDemande());
		return out;
	}

//	public String getReferenceCnss() {
//		return referenceCnss;
//	}
//
//	public void setReferenceCnss(String referenceCnss) {
//		this.referenceCnss = referenceCnss;
//	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String getReferenceDemande() {
		return referenceDemande;
	}

	public void setReferenceDemande(String referenceDemande) {
		this.referenceDemande = referenceDemande;
	}

}
