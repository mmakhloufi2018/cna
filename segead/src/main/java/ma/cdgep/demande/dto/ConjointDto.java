package ma.cdgep.demande.dto;

import ma.cdgep.demande.entity.A_ConjointEntity;
import ma.cdgep.demande.entity.ConjointEntity;
import ma.cdgep.utils.Utils;

public class ConjointDto {

	private String idcs;
	private String cin;
	private String codeMenageRsu;
	private String nomFr;
	private String prenomFr;
	private String nomAr;
	private String prenomAr;
	private String scoreRsu;
	private String dateNaissance;
	private String resideAuMaroc;
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
	public String getScoreRsu() {
		return scoreRsu;
	}
	public void setScoreRsu(String scoreRsu) {
		this.scoreRsu = scoreRsu;
	}
	public String getDateNaissance() {
		return dateNaissance;
	}
	public void setDateNaissance(String dateNaissance) {
		this.dateNaissance = dateNaissance;
	}
	public String getResideAuMaroc() {
		return resideAuMaroc;
	}
	public void setResideAuMaroc(String resideAuMaroc) {
		this.resideAuMaroc = resideAuMaroc;
	}
	
	public static ConjointEntity to(ConjointDto dto) {
		if(dto ==null)
			return null;
		ConjointEntity entity = new ConjointEntity();
		entity.setCin(dto.getCin());
		entity.setIdcs(dto.getIdcs());
		entity.setDateNaissance(Utils.stringToDate(dto.getDateNaissance(), Utils.FOURMAT_DATE_STRING));
		entity.setCodeMenageRsu(dto.getCodeMenageRsu());
		entity.setNomAr(dto.getNomAr());
		entity.setNomFr(dto.getNomFr());
		entity.setPrenomAr(dto.getPrenomAr());
		entity.setPrenomFr(dto.getPrenomFr());
		entity.setScoreRsu(Utils.stringToDouble(dto.getScoreRsu()));
		entity.setResideAuMaroc(Utils.stringToBoolean(dto.getResideAuMaroc()));
		return entity;
	}
	
	public static A_ConjointEntity to_A(ConjointDto dto) {
		if(dto ==null)
			return null;
		A_ConjointEntity entity = new A_ConjointEntity();
		entity.setCin(dto.getCin());
		entity.setIdcs(dto.getIdcs());
		entity.setDateNaissance(Utils.stringToDate(dto.getDateNaissance(), Utils.FOURMAT_DATE_STRING));
		entity.setCodeMenageRsu(dto.getCodeMenageRsu());
		entity.setNomAr(dto.getNomAr());
		entity.setNomFr(dto.getNomFr());
		entity.setPrenomAr(dto.getPrenomAr());
		entity.setPrenomFr(dto.getPrenomFr());
		entity.setScoreRsu(Utils.stringToDouble(dto.getScoreRsu()));
		entity.setResideAuMaroc(Utils.stringToBoolean(dto.getResideAuMaroc()));
		return entity;
	}
	
	public ConjointDto(String idcs, String cin, String codeMenageRsu, String nomFr, String prenomFr, String nomAr,
			String prenomAr, String scoreRsu, String dateNaissance, String resideAuMaroc) {
		super();
		this.idcs = idcs;
		this.cin = cin;
		this.codeMenageRsu = codeMenageRsu;
		this.nomFr = nomFr;
		this.prenomFr = prenomFr;
		this.nomAr = nomAr;
		this.prenomAr = prenomAr;
		this.scoreRsu = scoreRsu;
		this.dateNaissance = dateNaissance;
		this.resideAuMaroc = resideAuMaroc;
	}
	
	
}
