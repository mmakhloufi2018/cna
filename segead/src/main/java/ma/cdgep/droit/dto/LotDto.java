package ma.cdgep.droit.dto;

import java.util.List;

public class LotDto {
	
	private String referenceLot;
	private String dateLot;
	private List<DroitDto> demandes;
	
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
	public List<DroitDto> getDemandes() {
		return demandes;
	}
	public void setDemandes(List<DroitDto> demandes) {
		this.demandes = demandes;
	}
	
	
}
