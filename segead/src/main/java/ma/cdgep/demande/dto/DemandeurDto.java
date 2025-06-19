package ma.cdgep.demande.dto;

import java.util.List;
import java.util.stream.Collectors;

import ma.cdgep.demande.entity.DemandeurEntity;
import ma.cdgep.utils.Utils;

public class DemandeurDto {
	public static String MASCULIN = "M";
	public static String FEMININ = "F";
	private String idcs;
	private String cin;
	private String codeMenageRsu;
	private String nomFr;
	private String prenomFr;
	private String nomAr;
	private String prenomAr;
	private String etatMatrimonial;
	private String scoreRsu;
	private String genre;
	private String dateNaissance;
	private String adresse;
	private String resideAuMaroc;
	private String estChefMenageRSU;
	private List<ConjointDto> conjoints;
	private List<EnfantDto> enfants;
	private List<AutreMembreDto> autreMembres;

	public DemandeurDto() {
		super();
	}

	public String getIdcs() {
		return idcs;
	}

	public void setIdcs(String idcs) {
		this.idcs = idcs;
	}

	public String getCin() {
		return cin;
	}

	public void setCin(String cin) {
		this.cin = cin;
	}

	public String getCodeMenageRsu() {
		return codeMenageRsu;
	}

	public void setCodeMenageRsu(String codeMenageRsu) {
		this.codeMenageRsu = codeMenageRsu;
	}

	public String getNomFr() {
		return nomFr;
	}

	public void setNomFr(String nomFr) {
		this.nomFr = nomFr;
	}

	public String getPrenomFr() {
		return prenomFr;
	}

	public void setPrenomFr(String prenomFr) {
		this.prenomFr = prenomFr;
	}

	public String getNomAr() {
		return nomAr;
	}

	public void setNomAr(String nomAr) {
		this.nomAr = nomAr;
	}

	public String getPrenomAr() {
		return prenomAr;
	}

	public void setPrenomAr(String prenomAr) {
		this.prenomAr = prenomAr;
	}

	public String getEtatMatrimonial() {
		return etatMatrimonial;
	}

	public void setEtatMatrimonial(String etatMatrimonial) {
		this.etatMatrimonial = etatMatrimonial;
	}

	public String getScoreRsu() {
		return scoreRsu;
	}

	public void setScoreRsu(String scoreRsu) {
		this.scoreRsu = scoreRsu;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getDateNaissance() {
		return dateNaissance;
	}

	public void setDateNaissance(String dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getResideAuMaroc() {
		return resideAuMaroc;
	}

	public void setResideAuMaroc(String resideAuMaroc) {
		this.resideAuMaroc = resideAuMaroc;
	}

	public String getEstChefMenageRSU() {
		return estChefMenageRSU;
	}

	public void setEstChefMenageRSU(String estChefMenageRSU) {
		this.estChefMenageRSU = estChefMenageRSU;
	}

	public List<ConjointDto> getConjoints() {
		return conjoints;
	}

	public void setConjoints(List<ConjointDto> conjoints) {
		this.conjoints = conjoints;
	}

	public List<EnfantDto> getEnfants() {
		return enfants;
	}

	public void setEnfants(List<EnfantDto> enfants) {
		this.enfants = enfants;
	}

	public List<AutreMembreDto> getAutreMembres() {
		return autreMembres;
	}

	public void setAutreMembres(List<AutreMembreDto> autreMembres) {
		this.autreMembres = autreMembres;
	}

	public static DemandeurEntity to(DemandeurDto dto) {
		if (dto == null)
			return null;
		DemandeurEntity entity = new DemandeurEntity();
		entity.setIdcs(dto.getIdcs());
		entity.setCin(dto.getCin());
		entity.setCodeMenageRsu(dto.getCodeMenageRsu());
		entity.setNomFr(dto.getNomFr());
		entity.setPrenomFr(dto.getPrenomFr());
		entity.setNomAr(dto.getNomAr());
		entity.setPrenomAr(dto.getPrenomAr());
		entity.setEtatMatrimonial(dto.getEtatMatrimonial());
		entity.setScoreRsu(Utils.stringToDouble(dto.getScoreRsu()));
		entity.setGenre(dto.getGenre());
		entity.setDateNaissance(Utils.stringToDate(dto.getDateNaissance(), Utils.FOURMAT_DATE_STRING));
		entity.setAdresse(dto.getAdresse());
		entity.setResideAuMaroc(Utils.stringToBoolean(dto.getResideAuMaroc()));
		entity.setEstChefMenageRSU(Utils.stringToBoolean(dto.getEstChefMenageRSU()));

		if (dto.getConjoints() != null) {

			entity.setConjoints(dto.getConjoints().stream().map(ConjointDto::to).collect(Collectors.toList()));

			entity.getConjoints().forEach(c -> c.setDemandeur(entity));
		}

		if (dto.getEnfants() != null) {

			entity.setEnfants(dto.getEnfants().stream().map(EnfantDto::to).collect(Collectors.toList()));

			entity.getEnfants().forEach(c -> c.setDemandeur(entity));
		}

		if (dto.getAutreMembres() != null) {

			entity.setAutreMembres(dto.getAutreMembres().stream().map(AutreMembreDto::to).collect(Collectors.toList()));

			entity.getAutreMembres().forEach(c -> c.setDemandeur(entity));
		}

		return entity;
	}

	public DemandeurDto(String idcs, String cin, String codeMenageRsu, String nomFr, String prenomFr, String nomAr,
			String prenomAr, String etatMatrimonial, String scoreRsu, String genre, String dateNaissance,
			String adresse, String resideAuMaroc, String estChefMenageRSU, List<ConjointDto> conjoints,
			List<EnfantDto> enfants, List<AutreMembreDto> autreMembres) {
		super();
		this.idcs = idcs;
		this.cin = cin;
		this.codeMenageRsu = codeMenageRsu;
		this.nomFr = nomFr;
		this.prenomFr = prenomFr;
		this.nomAr = nomAr;
		this.prenomAr = prenomAr;
		this.etatMatrimonial = etatMatrimonial;
		this.scoreRsu = scoreRsu;
		this.genre = genre;
		this.dateNaissance = dateNaissance;
		this.adresse = adresse;
		this.resideAuMaroc = resideAuMaroc;
		this.estChefMenageRSU = estChefMenageRSU;
		this.conjoints = conjoints;
		this.enfants = enfants;
		this.autreMembres = autreMembres;
	}

}
