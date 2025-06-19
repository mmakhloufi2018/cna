package ma.cdgep.paiement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BeneficiareRetourDto {

	public String cin;
	public String idcs;
	public String codeResponse;
	public String libResponse;

	public BeneficiareRetourDto() {
		super();
	}

	public String getCin() {
		return cin;
	}

	public void setCin(String cin) {
		this.cin = cin;
	}

	public String getIdcs() {
		return idcs;
	}

	public void setIdcs(String idcs) {
		this.idcs = idcs;
	}

	public String getCodeResponse() {
		return codeResponse;
	}

	public void setCodeResponse(String codeResponse) {
		this.codeResponse = codeResponse;
	}

	public String getLibResponse() {
		return libResponse;
	}

	public void setLibResponse(String libResponse) {
		this.libResponse = libResponse;
	}

}
