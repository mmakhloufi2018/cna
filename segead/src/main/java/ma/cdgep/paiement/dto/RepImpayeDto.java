package ma.cdgep.paiement.dto;

public class RepImpayeDto {
	
	private String referencePaiement;
	private String idcs;
	private String codeResponse;
	private String libResponse;
	public String getReferencePaiement() {
		return referencePaiement;
	}
	public void setReferencePaiement(String referencePaiement) {
		this.referencePaiement = referencePaiement;
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
