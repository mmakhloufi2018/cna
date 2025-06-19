package ma.cdgep.demande.dto;

import ma.cdgep.demande.entity.AnnulationRecoursEntity;
import ma.cdgep.demande.entity.DemandeEntity;
import ma.cdgep.demande.entity.DemandeurEntity;

public class AnnulationRecoursDto {

	private String referenceCnss;
	
	private String idcs;

	private String statut;

	private String motif;

	public String getReferenceCnss() {
		return referenceCnss;
	}

	public void setReferenceCnss(String referenceCnss) {
		this.referenceCnss = referenceCnss;
	}

	public String getIdcs() {
		return idcs;
	}

	public void setIdcs(String idcs) {
		this.idcs = idcs;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public String getMotif() {
		return motif;
	}

	public void setMotif(String motif) {
		this.motif = motif;
	}

	public static AnnulationRecoursEntity to(AnnulationRecoursDto dto) {
		if(dto ==null)
			return null;
		AnnulationRecoursEntity entity = new AnnulationRecoursEntity();
//		entity.setDateDemande(Utils.stringToDate(dto.getDateAnnulation(), Utils.FOURMAT_DATE_STRING));
		entity.setIdcs(dto.getIdcs());
		entity.setReferenceCnss(dto.getReferenceCnss());
		return entity;
	}

}
