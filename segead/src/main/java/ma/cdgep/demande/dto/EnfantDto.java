package ma.cdgep.demande.dto;

import ma.cdgep.demande.entity.A_EnfantEntity;
import ma.cdgep.demande.entity.EnfantEntity;
import ma.cdgep.utils.Utils;

public class EnfantDto {

	public static final String TYPE_SCOLARITE_AUTRE = "0";
	public static final String TYPE_SCOLARITE_MASSAR = "1";
	public static final String TYPE_SCOLARITE_CEF = "2";
	private String cin;
	private String idcs;
	private String cinMere;
	private String idcsMere;
	private String cinPere;
	private String idcsPere;
	private String dateNaissance;
	private String estHandicape;
	private String identifiantScolarite;
	private String lienParente;
	private String codeMenageRsu;
	private String nomAr;
	private String nomFr;
	private String prenomAr;
	private String prenomFr;
	private String scolarise;
	private String scoreRsu;
	private String typeIdentifiantScolarite;
	private String typeScolarite;
	private String estOrphelinPere;
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
	public String getCinMere() {
		return cinMere;
	}
	public void setCinMere(String cinMere) {
		this.cinMere = cinMere;
	}
	public String getIdcsMere() {
		return idcsMere;
	}
	public void setIdcsMere(String idcsMere) {
		this.idcsMere = idcsMere;
	}
	public String getCinPere() {
		return cinPere;
	}
	public void setCinPere(String cinPere) {
		this.cinPere = cinPere;
	}
	public String getIdcsPere() {
		return idcsPere;
	}
	public void setIdcsPere(String idcsPere) {
		this.idcsPere = idcsPere;
	}
	public String getDateNaissance() {
		return dateNaissance;
	}
	public void setDateNaissance(String dateNaissance) {
		this.dateNaissance = dateNaissance;
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
	public String getScolarise() {
		return scolarise;
	}
	public void setScolarise(String scolarise) {
		this.scolarise = scolarise;
	}
	public String getScoreRsu() {
		return scoreRsu;
	}
	public void setScoreRsu(String scoreRsu) {
		this.scoreRsu = scoreRsu;
	}
	public String getTypeIdentifiantScolarite() {
		return typeIdentifiantScolarite;
	}
	public void setTypeIdentifiantScolarite(String typeIdentifiantScolarite) {
		this.typeIdentifiantScolarite = typeIdentifiantScolarite;
	}
	public String getTypeScolarite() {
		return typeScolarite;
	}
	public void setTypeScolarite(String typeScolarite) {
		this.typeScolarite = typeScolarite;
	}
	public String getEstOrphelinPere() {
		return estOrphelinPere;
	}
	public void setEstOrphelinPere(String estOrphelinPere) {
		this.estOrphelinPere = estOrphelinPere;
	}
	public String getResideAuMaroc() {
		return resideAuMaroc;
	}
	public void setResideAuMaroc(String resideAuMaroc) {
		this.resideAuMaroc = resideAuMaroc;
	}

	
	public static EnfantEntity to(EnfantDto dto) {
		if(dto ==null)
			return null;
		EnfantEntity entity = new EnfantEntity();
        entity.setCin(dto.getCin());
        entity.setIdcs(dto.getIdcs());
        entity.setCinMere(dto.getCinMere());
        entity.setIdcsMere(dto.getIdcsMere());
        entity.setCinPere(dto.getCinPere());
        entity.setIdcsPere(dto.getIdcsPere());
        entity.setDateNaissance(Utils.stringToDate( dto.getDateNaissance(), Utils.FOURMAT_DATE_STRING));
        entity.setEstHandicape(Utils.stringToBoolean(dto.getEstHandicape()));
        entity.setIdentifiantScolarite(dto.getIdentifiantScolarite());
        entity.setLienParente(dto.getLienParente());
        entity.setCodeMenageRsu(dto.getCodeMenageRsu());
        entity.setNomAr(dto.getNomAr());
        entity.setNomFr(dto.getNomFr());
        entity.setPrenomAr(dto.getPrenomAr());
        entity.setPrenomFr(dto.getPrenomFr());
        entity.setScolarise(dto.getScolarise());
        entity.setScoreRsu(Utils.stringToDouble(dto.getScoreRsu()));
        entity.setTypeIdentifiantScolarite(dto.getTypeIdentifiantScolarite());
        entity.setTypeScolarite(dto.getTypeScolarite());
        entity.setEstOrphelinPere(Utils.stringToBoolean(dto.getEstOrphelinPere()));
        entity.setResideAuMaroc(Utils.stringToBoolean(dto.getResideAuMaroc()));
        return entity;
    }
	public static A_EnfantEntity to_A(EnfantDto dto) {
		if(dto ==null)
			return null;
		A_EnfantEntity entity = new A_EnfantEntity();
        entity.setCin(dto.getCin());
        entity.setIdcs(dto.getIdcs());
        entity.setCinMere(dto.getCinMere());
        entity.setIdcsMere(dto.getIdcsMere());
        entity.setCinPere(dto.getCinPere());
        entity.setIdcsPere(dto.getIdcsPere());
        entity.setDateNaissance(Utils.stringToDate( dto.getDateNaissance(), Utils.FOURMAT_DATE_STRING));
        entity.setEstHandicape(Utils.stringToBoolean(dto.getEstHandicape()));
        entity.setIdentifiantScolarite(dto.getIdentifiantScolarite());
        entity.setLienParente(dto.getLienParente());
        entity.setCodeMenageRsu(dto.getCodeMenageRsu());
        entity.setNomAr(dto.getNomAr());
        entity.setNomFr(dto.getNomFr());
        entity.setPrenomAr(dto.getPrenomAr());
        entity.setPrenomFr(dto.getPrenomFr());
        entity.setScolarise(dto.getScolarise());
        entity.setScoreRsu(Utils.stringToDouble(dto.getScoreRsu()));
        entity.setTypeIdentifiantScolarite(dto.getTypeIdentifiantScolarite());
        entity.setTypeScolarite(dto.getTypeScolarite());
        entity.setEstOrphelinPere(Utils.stringToBoolean(dto.getEstOrphelinPere()));
        entity.setResideAuMaroc(Utils.stringToBoolean(dto.getResideAuMaroc()));
        return entity;
    }
	public EnfantDto(String cin, String idcs, String cinMere, String idcsMere, String cinPere, String idcsPere,
			String dateNaissance, String estHandicape, String identifiantScolarite, String lienParente,
			String codeMenageRsu, String nomAr, String nomFr, String prenomAr, String prenomFr, String scolarise,
			String scoreRsu, String typeIdentifiantScolarite, String typeScolarite, String estOrphelinPere,
			String resideAuMaroc) {
		super();
		this.cin = cin;
		this.idcs = idcs;
		this.cinMere = cinMere;
		this.idcsMere = idcsMere;
		this.cinPere = cinPere;
		this.idcsPere = idcsPere;
		this.dateNaissance = dateNaissance;
		this.estHandicape = estHandicape;
		this.identifiantScolarite = identifiantScolarite;
		this.lienParente = lienParente;
		this.codeMenageRsu = codeMenageRsu;
		this.nomAr = nomAr;
		this.nomFr = nomFr;
		this.prenomAr = prenomAr;
		this.prenomFr = prenomFr;
		this.scolarise = scolarise;
		this.scoreRsu = scoreRsu;
		this.typeIdentifiantScolarite = typeIdentifiantScolarite;
		this.typeScolarite = typeScolarite;
		this.estOrphelinPere = estOrphelinPere;
		this.resideAuMaroc = resideAuMaroc;
	}
	
	
}
