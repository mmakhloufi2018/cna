package ma.cdgep.demande.dto;

public class ReponseDemandeMajDto {

	private String referenceCnss;
	private String motif;
	private String statut;

	public String getReferenceCnss() {
		return referenceCnss;
	}

	public void setReferenceCnss(String referenceCnss) {
		this.referenceCnss = referenceCnss;
	}

	public String getMotif() {
		return motif;
	}

	public void setMotif(String motif) {
		this.motif = motif;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public ReponseDemandeMajDto(String referenceCnss, String motif, String statut) {
		super();
		this.referenceCnss = referenceCnss;
		this.motif = motif;
		this.statut = statut;
	}

	public ReponseDemandeMajDto() {
		super();
	}
	

}
