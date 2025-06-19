package ma.cdgep.demande.dto;

import java.util.ArrayList;
import java.util.List;

import ma.cdgep.demande.entity.RecoursEntity;

public class DemandeRecoursDto {

	private String referenceCnss;
	private String idcs;

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

	public static List<String> getReferenceDemandes(List<DemandeRecoursDto> demandes) {
		if (demandes == null)
			return null;
		List<String> listReference = new ArrayList<String>();
		for (DemandeRecoursDto demande : demandes) {
			listReference.add(demande.getReferenceCnss());
		}
		return listReference;
	}
	
	public static List<String> getIdcsDemandes(List<DemandeRecoursDto> demandes) {
		if (demandes == null)
			return null;
		List<String> idcs = new ArrayList<String>();
		for (DemandeRecoursDto demande : demandes) {
			idcs.add(demande.getIdcs());
		}
		return idcs;
	}
	
	public static List<String> getIdcsDemandeurs(List<DemandeAjoutMembreDto> demandes) {
		if (demandes == null)
			return null;
		List<String> idcs = new ArrayList<String>();
		for (DemandeAjoutMembreDto demande : demandes) {
			idcs.add(demande.getIdcsDemandeur());
		}
		return idcs;
	}

	public static DemandeRecoursDto getByReference(List<DemandeRecoursDto> demandes, String reference) {
		if (demandes == null)
			return null;
		return demandes.stream().filter(a -> reference.equals(a.getReferenceCnss())).findAny().orElse(null);

	}
	
	public static DemandeRecoursDto getByIdcs(List<DemandeRecoursDto> demandes, String idcs) {
		if (demandes == null)
			return null;
		return demandes.stream().filter(a -> idcs.equals(a.getIdcs())).findAny().orElse(null);

	}
	
	public static DemandeAjoutMembreDto getByIdcsDemandeur(List<DemandeAjoutMembreDto> demandes, String idcs) {
		if (demandes == null)
			return null;
		return demandes.stream().filter(a -> idcs.equals(a.getIdcsDemandeur())).findAny().orElse(null);

	}

	
	public static RecoursEntity to(DemandeRecoursDto dto) {
		if(dto ==null)
			return null;
		RecoursEntity entity = new RecoursEntity();
//		entity.setDateDemande(Utils.stringToDate(dto.getDateAnnulation(), Utils.FOURMAT_DATE_STRING));
		if(dto.getIdcs() != null)
			entity.setIdcs(dto.getIdcs());
		entity.setReference(dto.getReferenceCnss());
		return entity;
	}
	
}
