package ma.cdgep.demande.dto;

import java.util.List;

public class LotDemandeAnnuleDto {

	private String referenceLot;
	private String dateLot;
	private List<DemandeAnnuleeDto> demandes;

	public String getReferenceLot() {
		return referenceLot;
	}

	public void setReferenceLot(String referenceLot) {
		this.referenceLot = referenceLot;
	}

	public String getDateLot() {
		return dateLot;
	}

	public void setDateLot(String dateLot) {
		this.dateLot = dateLot;
	}

	public List<DemandeAnnuleeDto> getDemandes() {
		return demandes;
	}

	public void setDemandes(List<DemandeAnnuleeDto> demandes) {
		this.demandes = demandes;
	}


}
