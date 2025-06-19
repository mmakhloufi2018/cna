package ma.cdgep.eligibilite.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import ma.cdgep.eligibilite.entity.EligibiliteDemandeEntity;

public class EligibiliteDemandeDto {

	private String reference;
	private String motif;
	private String error;
	@JsonProperty("statusCode")
	private Integer statusCnss;
	
	@JsonProperty("rejectionOrganization")
	private String partenaire;

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}


	public String getMotif() {
		return motif;
	}

	public void setMotif(String motif) {
		this.motif = motif;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public static EligibiliteDemandeDto from(EligibiliteDemandeEntity in) {

		if (in == null)
			return null;

		EligibiliteDemandeDto out = new EligibiliteDemandeDto();
		out.setError((110 == in.getStatusCnss()) ? "-" : in.getError());
		out.setMotif((110 == in.getStatusCnss()) ? "-" : in.getMotif());
		out.setReference(in.getReference());
//		out.setStatusCode(in.getStatusCode());
		out.setPartenaire(in.getPartenaire() != null ?  in.getPartenaire() : "");
		out.setStatusCnss(in.getStatusCnss());

		return out;
	}

	public Integer getStatusCnss() {
		return statusCnss;
	}

	public void setStatusCnss(Integer statusCnss) {
		this.statusCnss = statusCnss;
	}

	public String getPartenaire() {
		return partenaire;
	}

	public void setPartenaire(String partenaire) {
		this.partenaire = partenaire;
	}

}
