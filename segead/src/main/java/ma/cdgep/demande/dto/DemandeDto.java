package ma.cdgep.demande.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ma.cdgep.demande.entity.DemandeEntity;
import ma.cdgep.demande.entity.DemandeurEntity;
import ma.cdgep.utils.Utils;

public class DemandeDto {

	public static final String STATUT_RECEPTIONNE = "RECEPTIONNE";
	public static final String TYPE_NOUVELLE = "NOUVELLE";
	public static final String TYPE_ANNULATION = "ANNULATION";
	public static final String STATUT_TRAITE = "TRAITE";
	public static final String STATUT_REJETE = "REJETE";
	public static final String CODE_PRESTATION_AIDE_FAMILLE = "1";
	public static final String CODE_PRESTATION_AIDE_ENFANCE = "2";
	public static final String CODE_PRESTATION_PRIME_NAISSANCE = "3";
	private String dateDemande;
	private String referenceCnss;
	private String immatriculation;
	private DemandeurDto demandeur;

	public String getDateDemande() {
		return dateDemande;
	}

	public void setDateDemande(String dateDemande) {
		this.dateDemande = dateDemande;
	}

	public String getReferenceCnss() {
		return referenceCnss;
	}

	public void setReferenceCnss(String referenceCnss) {
		this.referenceCnss = referenceCnss;
	}

	public String getImmatriculation() {
		return immatriculation;
	}

	public void setImmatriculation(String immatriculation) {
		this.immatriculation = immatriculation;
	}

	public DemandeurDto getDemandeur() {
		return demandeur;
	}

	public void setDemandeur(DemandeurDto demandeur) {
		this.demandeur = demandeur;
	}

	public static DemandeEntity to(DemandeDto dto) {
		if (dto == null)
			return null;
		DemandeEntity entity = new DemandeEntity();

		entity.setDateDemande(Utils.stringToDate(dto.getDateDemande(), Utils.FOURMAT_DATE_STRING));
		entity.setImmatriculation(dto.getImmatriculation());
		entity.setReferenceCnss(dto.getReferenceCnss());
		DemandeurEntity dmd = DemandeurDto.to(dto.getDemandeur());
		if (dmd != null) {
			dmd.setDemande(entity);
			entity.setDemandeur(dmd);
		}
		return entity;
	}

	public static DemandeDto getByImmatriculation(List<DemandeDto> demandes, String immatriculation) {
		if (demandes == null || demandes.size() == 0 || immatriculation == null)
			return null;
		return demandes.stream().filter(a -> a != null && immatriculation.equals(a.getImmatriculation())).findAny()
				.orElse(null);

	}

	public static DemandeDto getByReference(List<DemandeDto> demandes, String reference) {
		if (demandes == null)
			return null;
		return demandes.stream().filter(a -> reference.equals(a.getReferenceCnss())).findAny().orElse(null);

	}

	public static List<DemandeDto> getByIdcs(List<DemandeDto> demandes, List<String> listIdcs) {
		if (demandes == null || demandes.size() == 0)
			return null;
		return demandes.stream().filter(a -> {
			if (a != null) {
				if (a.getDemandeur() != null && listIdcs.contains(a.getDemandeur().getIdcs()))
					return true;
				if (a.getDemandeur() != null && a.getDemandeur().getConjoints() != null
						&& a.getDemandeur().getConjoints().stream()
								.filter(c -> c != null && listIdcs.contains(c.getIdcs())).findAny().isPresent())
					return true;
				if (a.getDemandeur() != null && a.getDemandeur().getEnfants() != null && a.getDemandeur().getEnfants()
						.stream().filter(c -> c != null && listIdcs.contains(c.getIdcs())).findAny().isPresent())
					return true;
				if (a.getDemandeur() != null && a.getDemandeur().getAutreMembres() != null
						&& a.getDemandeur().getAutreMembres().stream()
								.filter(c -> c != null && listIdcs.contains(c.getIdcs())).findAny().isPresent())
					return true;
			}
			return false;
		}).collect(Collectors.toList());

	}

	public static List<String> getListIdcs(List<DemandeDto> demandes) {
		if (demandes == null || demandes.size() == 0)
			return null;
		List<String> ListIdcs = new ArrayList<String>();
		for (DemandeDto demandeDto : demandes) {
			if (demandeDto != null) {
				if (demandeDto.getDemandeur() != null && demandeDto.getDemandeur().getIdcs() != null)
					ListIdcs.add(demandeDto.getDemandeur().getIdcs());

				if (demandeDto.getDemandeur() != null && demandeDto.getDemandeur().getConjoints() != null
						&& demandeDto.getDemandeur().getConjoints().size() > 0)
					for (ConjointDto conjont : demandeDto.getDemandeur().getConjoints()) {
						if (conjont != null && conjont.getIdcs() != null && !conjont.getIdcs().isEmpty())
							ListIdcs.add(conjont.getIdcs());
					}
				if (demandeDto.getDemandeur() != null && demandeDto.getDemandeur().getEnfants() != null
						&& demandeDto.getDemandeur().getEnfants().size() > 0)
					for (EnfantDto enfant : demandeDto.getDemandeur().getEnfants()) {
						if (enfant != null && enfant.getIdcs() != null && !enfant.getIdcs().isEmpty())
							ListIdcs.add(enfant.getIdcs());
					}
				if (demandeDto.getDemandeur() != null && demandeDto.getDemandeur().getAutreMembres() != null
						&& demandeDto.getDemandeur().getAutreMembres().size() > 0)
					for (AutreMembreDto autreMembre : demandeDto.getDemandeur().getAutreMembres()) {
						if (autreMembre != null && autreMembre.getIdcs() != null && !autreMembre.getIdcs().isEmpty())
							ListIdcs.add(autreMembre.getIdcs());
					}
			}
		}
		return ListIdcs;

	}

	public DemandeDto(String dateDemande, String referenceCnss, String immatriculation, DemandeurDto demandeur) {
		super();
		this.dateDemande = dateDemande;
		this.referenceCnss = referenceCnss;
		this.immatriculation = immatriculation;
		this.demandeur = demandeur;
	}

	public static List<String> getReferenceDemandes(List<DemandeDto> demandes) {
		if (demandes == null)
			return null;
		List<String> listReference = new ArrayList<String>();
		for (DemandeDto demande : demandes) {
			if (demande != null)
				listReference.add(demande.getReferenceCnss());
		}
		return listReference;
	}

}
