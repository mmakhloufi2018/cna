package ma.cdgep.paiement.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LotInduDto {
	
	private String numLot;
	
	private String dateEcheance;
	
	private List<BeneficiareDto> listBenif;

	public String getNumLot() {
		return numLot;
	}

	public void setNumLot(String numLot) {
		this.numLot = numLot;
	}

	public String getDateEcheance() {
		return dateEcheance;
	}

	public void setDateEcheance(String dateEcheance) {
		this.dateEcheance = dateEcheance;
	}

	public List<BeneficiareDto> getListBenif() {
		return listBenif;
	}

	public void setListBenif(List<BeneficiareDto> listBenif) {
		this.listBenif = listBenif;
	}

}
