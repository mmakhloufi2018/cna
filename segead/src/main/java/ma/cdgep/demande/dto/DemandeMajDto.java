package ma.cdgep.demande.dto;

import java.util.ArrayList;
import java.util.List;

import ma.cdgep.demande.entity.DemandeMajEntity;

public class DemandeMajDto {

	private String referenceCnss;
	private String idcsEnfant;
	private String estHandicape;
	private String identifiantScolarite;
	private String scolarise;
	private String typeIdentifiantScolarite;
	private String estOrphelinPere;

	public String getIdcsEnfant() {
		return idcsEnfant;
	}

	public void setIdcsEnfant(String idcsEnfant) {
		this.idcsEnfant = idcsEnfant;
	}

	public String getEstHandicape() {
		return estHandicape;
	}

	public void setEstHandicape(String estHandicape) {
		this.estHandicape = estHandicape;
	}

	public String getIdentifiantScolarite() {
		return identifiantScolarite;
	}

	public void setIdentifiantScolarite(String identifiantScolarite) {
		this.identifiantScolarite = identifiantScolarite;
	}

	public String getScolarise() {
		return scolarise;
	}

	public void setScolarise(String scolarise) {
		this.scolarise = scolarise;
	}

	public String getTypeIdentifiantScolarite() {
		return typeIdentifiantScolarite;
	}

	public void setTypeIdentifiantScolarite(String typeIdentifiantScolarite) {
		this.typeIdentifiantScolarite = typeIdentifiantScolarite;
	}

	public String getEstOrphelinPere() {
		return estOrphelinPere;
	}

	public void setEstOrphelinPere(String estOrphelinPere) {
		this.estOrphelinPere = estOrphelinPere;
	}

	public String getReferenceCnss() {
		return referenceCnss;
	}

	public void setReferenceCnss(String referenceCnss) {
		this.referenceCnss = referenceCnss;
	}

	public static List<String> getReferenceDemandes(List<DemandeMajDto> demandes) {
		if (demandes == null)
			return null;
		List<String> listReference = new ArrayList<String>();
		for (DemandeMajDto demande : demandes) {
			listReference.add(demande.getReferenceCnss());
		}
		return listReference;
	}

	public static List<String> getIdcsDemandes(List<DemandeMajDto> demandes) {
		if (demandes == null)
			return null;
		List<String> idcs = new ArrayList<String>();
		for (DemandeMajDto demande : demandes) {
			idcs.add(demande.getIdcsEnfant());
		}
		return idcs;
	}

	public static DemandeMajDto getByReference(List<DemandeMajDto> demandes, String reference) {
		if (demandes == null)
			return null;
		return demandes.stream().filter(a -> reference.equals(a.getReferenceCnss())).findAny().orElse(null);

	}

	public static DemandeMajDto getByIdcs(List<DemandeMajDto> demandes, String idcs) {
		if (demandes == null)
			return null;
		return demandes.stream().filter(a -> idcs.equals(a.getIdcsEnfant())).findAny().orElse(null);

	}

	public static DemandeMajEntity to(DemandeMajDto dto) {
		if (dto == null)
			return null;
		return new DemandeMajEntity(null, null, dto.getReferenceCnss(), dto.getIdcsEnfant(), dto.getEstHandicape(),
				dto.getIdentifiantScolarite(), dto.getScolarise(), dto.getTypeIdentifiantScolarite(),
				dto.getEstOrphelinPere(), null);
	}

}
