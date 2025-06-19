package ma.cdgep.eligibilite.dto;

import java.util.List;
import java.util.stream.Collectors;

import ma.cdgep.eligibilite.entity.EligibiliteLotDemandeEntity;

public class EligibiliteLotDemandeDto {

	private String reference;
	private Integer statusCode;
	private String motif;
	private String status;
	private List<EligibiliteDemandeDto> demands;

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getMotif() {
		return motif;
	}

	public void setMotif(String motif) {
		this.motif = motif;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	

	public static EligibiliteLotDemandeDto from(EligibiliteLotDemandeEntity in) {

		if (in == null)
			return null;

		EligibiliteLotDemandeDto out = new EligibiliteLotDemandeDto();
		out.setMotif(in.getMotif());
		out.setReference(in.getReference());
		out.setStatusCode(in.getStatusCode());
		out.setStatus(in.getStatus());

		out.setDemands(in.getDemandes().stream().map(EligibiliteDemandeDto::from).collect(Collectors.toList()));
		return out;
	}

	public List<EligibiliteDemandeDto> getDemands() {
		return demands;
	}

	public void setDemands(List<EligibiliteDemandeDto> demands) {
		this.demands = demands;
	}

}
