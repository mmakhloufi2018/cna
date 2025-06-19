package ma.cdgep.demande.dto;

import ma.cdgep.demande.entity.AutreMembreEntity;
import ma.cdgep.utils.Utils;

public class AutreMembreDto {

	private String cin;
	private String idcs;
	private String dateNaissance;
	private String lienParente;
	private String codeMenageRsu;
	private String nomAr;
	private String nomFr;
	private String prenomAr;
	private String prenomFr;
	private String scoreRsu;
	private String resideAuMaroc;

	public String getCin() {
		return cin;
	}

	public void setCin(String cin) {
		this.cin = cin;
	}

	public String getIdcs() {
		return idcs;
	}

	public void setIdcs(String idcs) {
		this.idcs = idcs;
	}

	public String getDateNaissance() {
		return dateNaissance;
	}

	public void setDateNaissance(String dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public String getLienParente() {
		return lienParente;
	}

	public void setLienParente(String lienParente) {
		this.lienParente = lienParente;
	}

	public String getCodeMenageRsu() {
		return codeMenageRsu;
	}

	public void setCodeMenageRsu(String codeMenageRsu) {
		this.codeMenageRsu = codeMenageRsu;
	}

	public String getNomAr() {
		return nomAr;
	}

	public void setNomAr(String nomAr) {
		this.nomAr = nomAr;
	}

	public String getNomFr() {
		return nomFr;
	}

	public void setNomFr(String nomFr) {
		this.nomFr = nomFr;
	}

	public String getPrenomAr() {
		return prenomAr;
	}

	public void setPrenomAr(String prenomAr) {
		this.prenomAr = prenomAr;
	}

	public String getPrenomFr() {
		return prenomFr;
	}

	public void setPrenomFr(String prenomFr) {
		this.prenomFr = prenomFr;
	}

	public String getScoreRsu() {
		return scoreRsu;
	}

	public void setScoreRsu(String scoreRsu) {
		this.scoreRsu = scoreRsu;
	}

	public String getResideAuMaroc() {
		return resideAuMaroc;
	}

	public void setResideAuMaroc(String resideAuMaroc) {
		this.resideAuMaroc = resideAuMaroc;
	}

	public static AutreMembreEntity to(AutreMembreDto dto) {
		if(dto ==null)
			return null;
		AutreMembreEntity entity = new AutreMembreEntity();
		entity.setCin(dto.getCin());
		entity.setIdcs(dto.getIdcs());
		entity.setDateNaissance(Utils.stringToDate(dto.getDateNaissance(), Utils.FOURMAT_DATE_STRING));
		entity.setLienParente(dto.getLienParente());
		entity.setCodeMenageRsu(dto.getCodeMenageRsu());
		entity.setNomAr(dto.getNomAr());
		entity.setNomFr(dto.getNomFr());
		entity.setPrenomAr(dto.getPrenomAr());
		entity.setPrenomFr(dto.getPrenomFr());
		entity.setScoreRsu(Utils.stringToDouble(dto.getScoreRsu()));
		entity.setResideAuMaroc(Utils.stringToBoolean(dto.getResideAuMaroc()));
		return entity;
	}

	public AutreMembreDto(String cin, String idcs, String dateNaissance, String lienParente, String codeMenageRsu,
			String nomAr, String nomFr, String prenomAr, String prenomFr, String scoreRsu, String resideAuMaroc) {
		super();
		this.cin = cin;
		this.idcs = idcs;
		this.dateNaissance = dateNaissance;
		this.lienParente = lienParente;
		this.codeMenageRsu = codeMenageRsu;
		this.nomAr = nomAr;
		this.nomFr = nomFr;
		this.prenomAr = prenomAr;
		this.prenomFr = prenomFr;
		this.scoreRsu = scoreRsu;
		this.resideAuMaroc = resideAuMaroc;
	}
	
	
}
