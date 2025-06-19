package ma.cdgep.paiement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SyntheseLotDto {

	@JsonProperty("nume_lot")
	private String numeroLot;
	@JsonProperty("nomblots")
	private String nombreLots;
	@JsonProperty("nomnlign")
	private String nombreLignes;
	@JsonProperty("montota")
	private String montantTotal;
	@JsonProperty("refeeche")
	private String referenceEcheance;
	@JsonProperty("refecnss")
	private String refecnss;

	public SyntheseLotDto() {
		super();
	}

	public String getNumeroLot() {
		return numeroLot;
	}

	public void setNumeroLot(String numeroLot) {
		this.numeroLot = numeroLot;
	}

	public String getNombreLots() {
		return nombreLots;
	}

	public void setNombreLots(String nombreLots) {
		this.nombreLots = nombreLots;
	}

	public String getNombreLignes() {
		return nombreLignes;
	}

	public void setNombreLignes(String nombreLignes) {
		this.nombreLignes = nombreLignes;
	}

	public String getMontantTotal() {
		return montantTotal;
	}

	public void setMontantTotal(String montantTotal) {
		this.montantTotal = montantTotal;
	}

	public String getReferenceEcheance() {
		return referenceEcheance;
	}

	public void setReferenceEcheance(String referenceEcheance) {
		this.referenceEcheance = referenceEcheance;
	}

	public String getRefecnss() {
		return refecnss;
	}

	public void setRefecnss(String refecnss) {
		this.refecnss = refecnss;
	}


}
