package ma.cdgep.paiement.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import ma.cdgep.paiement.entity.LotPaiementEntity;
import ma.cdgep.utils.Utils;

public class LotPaiementDto {

	@JsonProperty("dateeche")
	private String dateEcheance;

	@JsonProperty("refeeche")
	private String referenceEcheance;

	@JsonProperty("typepres")
	private String typePrestation;

	@JsonProperty("nume_lot")
	private Integer numeroLot;

	@JsonProperty("paiements")
	private Set<PaiementDto> paiements;

	public LotPaiementDto() {
		super();
	}

	public String getDateEcheance() {
		return dateEcheance;
	}

	public void setDateEcheance(String dateEcheance) {
		this.dateEcheance = dateEcheance;
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

	public Integer getNumeroLot() {
		return numeroLot;
	}

	public void setNumeroLot(Integer numeroLot) {
		this.numeroLot = numeroLot;
	}

	public Set<PaiementDto> getPaiements() {
		return paiements;
	}

	public void setPaiements(Set<PaiementDto> paiements) {
		this.paiements = paiements;
	}

	public static LotPaiementDto from(LotPaiementEntity in) {
		if (in == null)
			return null;
		LotPaiementDto out = new LotPaiementDto();

		out.setDateEcheance(Utils.dateToString(in.getDateEcheance(), Utils.FOURMAT_DATE_STRING));
		out.setNumeroLot(in.getNumeroLot());
		out.setReferenceEcheance(in.getReferenceEcheance());
		out.setTypePrestation(in.getTypePrestation());
		if (in.getPaiements() != null)
			out.setPaiements(in.getPaiements().stream().map(PaiementDto::from).collect(Collectors.toSet()));

		return out;
	}
}
