package ma.cdgep.eligibilite.dto;

import java.util.List;

public class EligibiliteReponseDto {

	private String reference;
	private String status;
	private String error;
	private List<EligibiliteDemandeDto> rejectedDemands;
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public List<EligibiliteDemandeDto> getRejectedDemands() {
		return rejectedDemands;
	}
	public void setRejectedDemands(List<EligibiliteDemandeDto> rejectedDemands) {
		this.rejectedDemands = rejectedDemands;
	}

}
