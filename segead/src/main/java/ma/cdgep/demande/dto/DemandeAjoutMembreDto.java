package ma.cdgep.demande.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ma.cdgep.demande.entity.DemandeAjoutMembreEntity;

public class DemandeAjoutMembreDto {

	private String referenceCnss;
	private String referenceDemande;
	private String idcsDemandeur;
	private List<EnfantDto> enfants;
	private List<ConjointDto> conjoints;

	public DemandeAjoutMembreDto(String referenceCnss, String referenceDemande, String idcsDemandeur,
			List<EnfantDto> enfants, List<ConjointDto> conjoints) {
		super();
		this.referenceCnss = referenceCnss;
		this.referenceDemande = referenceDemande;
		this.idcsDemandeur = idcsDemandeur;
		this.enfants = enfants;
		this.conjoints = conjoints;
	}

	public DemandeAjoutMembreDto() {
		super();
	}

	public String getReferenceCnss() {
		return referenceCnss;
	}

	public void setReferenceCnss(String referenceCnss) {
		this.referenceCnss = referenceCnss;
	}

	public String getReferenceDemande() {
		return referenceDemande;
	}

	public void setReferenceDemande(String referenceDemande) {
		this.referenceDemande = referenceDemande;
	}

	public String getIdcsDemandeur() {
		return idcsDemandeur;
	}

	public void setIdcsDemandeur(String idcsDemandeur) {
		this.idcsDemandeur = idcsDemandeur;
	}

	public List<EnfantDto> getEnfants() {
		return enfants;
	}

	public void setEnfants(List<EnfantDto> enfants) {
		this.enfants = enfants;
	}

	public List<ConjointDto> getConjoints() {
		return conjoints;
	}

	public void setConjoints(List<ConjointDto> conjoints) {
		this.conjoints = conjoints;
	}

	public static DemandeAjoutMembreEntity to(DemandeAjoutMembreDto dto) {
		if (dto == null)
			return null;
		DemandeAjoutMembreEntity entity = new DemandeAjoutMembreEntity();

		entity.setReferenceCnss(dto.getReferenceCnss());
		entity.setReferenceDemande(dto.getReferenceDemande());
		entity.setIdcsDemandeur(dto.getIdcsDemandeur());
		if (dto.getEnfants() != null) {
			entity.setEnfants(dto.getEnfants().stream().map(EnfantDto::to_A).collect(Collectors.toList()));
			entity.getEnfants().forEach(e -> e.setDemande(entity));
		}
		if (dto.getConjoints() != null) {
			entity.setConjoints(dto.getConjoints().stream().map(ConjointDto::to_A).collect(Collectors.toList()));
			entity.getConjoints().forEach(e -> e.setDemande(entity));
		}
		return entity;
	}

	public static List<String> getReferenceDemandes(List<DemandeAjoutMembreDto> demandes) {
		if (demandes == null)
			return null;
		List<String> listReference = new ArrayList<String>();
		for (DemandeAjoutMembreDto demande : demandes) {
			listReference.add(demande.getReferenceCnss());
		}
		return listReference;
	}

	public static DemandeAjoutMembreDto getByIdcs(List<DemandeAjoutMembreDto> demandes, String idcs) {
		if (demandes == null)
			return null;
		return demandes.stream().filter(a -> idcs.equals(a.getIdcsDemandeur())).findAny().orElse(null);

	}
	public static DemandeAjoutMembreDto getByReferenceDemande(List<DemandeAjoutMembreDto> demandes, String referenceDemande) {
		if (demandes == null)
			return null;
		return demandes.stream().filter(a -> referenceDemande.equals(a.getReferenceDemande())).findAny().orElse(null);
		
	}

	public static boolean existeMembre(DemandeAjoutMembreDto d) {
		if (d != null && ((d.getEnfants() != null && d.getEnfants().size() > 0)
				|| (d.getConjoints() != null && d.getConjoints().size() > 0)))
			return true;
		return false;
	}
	
	public static boolean existeMembreEnfant(DemandeAjoutMembreDto d) {
		if (d != null && d.getEnfants() != null && d.getEnfants().size() > 0)
			return true;
		return false;
	}
}
