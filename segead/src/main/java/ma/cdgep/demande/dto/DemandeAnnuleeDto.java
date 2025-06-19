package ma.cdgep.demande.dto;

import java.util.ArrayList;
import java.util.List;

import ma.cdgep.demande.entity.DemandeEntity;
import ma.cdgep.demande.entity.DemandeurEntity;

public class DemandeAnnuleeDto {

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

	public static List<String> getReferenceDemandes(List<DemandeAnnuleeDto> demandes) {
		if (demandes == null)
			return null;
		List<String> listReference = new ArrayList<String>();
		for (DemandeAnnuleeDto demande : demandes) {
			listReference.add(demande.getReferenceCnss());
		}
		return listReference;
	}
	
	public static List<String> getIdcsDemandes(List<DemandeAnnuleeDto> demandes) {
		if (demandes == null)
			return null;
		List<String> idcs = new ArrayList<String>();
		for (DemandeAnnuleeDto demande : demandes) {
			idcs.add(demande.getIdcs());
		}
		return idcs;
	}

	public static DemandeAnnuleeDto getByReference(List<DemandeAnnuleeDto> demandes, String reference) {
		if (demandes == null)
			return null;
		return demandes.stream().filter(a -> reference.equals(a.getReferenceCnss())).findAny().orElse(null);

	}
	
	public static DemandeAnnuleeDto getByIdcs(List<DemandeAnnuleeDto> demandes, String idcs) {
		if (demandes == null)
			return null;
		return demandes.stream().filter(a -> idcs.equals(a.getIdcs())).findAny().orElse(null);

	}

	
	public static DemandeEntity to(DemandeAnnuleeDto dto) {
		if(dto ==null)
			return null;
		DemandeEntity entity = new DemandeEntity();
//		entity.setDateDemande(Utils.stringToDate(dto.getDateAnnulation(), Utils.FOURMAT_DATE_STRING));
		if(dto.getIdcs() != null)
			entity.setDemandeur(new DemandeurEntity(dto.getIdcs(), entity));
		entity.setReferenceCnss(dto.getReferenceCnss());
		return entity;
	}
	
}
