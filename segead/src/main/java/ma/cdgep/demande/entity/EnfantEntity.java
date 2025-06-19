package ma.cdgep.demande.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "D_ENFANT")
public class EnfantEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "IDCS")
	private String idcs;
	@Column(name = "CIN")
	private String cin;
	@Column(name = "NOM_FR")
	private String nomFr;
	@Column(name = "CODE_MENAGE_RSU")
	private String codeMenageRsu;
	@Column(name = "PRENOM_FR")
	private String prenomFr;
	@Column(name = "NOM_AR")
	private String nomAr;
	@Column(name = "PRENOM_AR")
	private String prenomAr;
	@Column(name = "SCORE_RSU")
	private Double scoreRsu;
	@Column(name = "LIEN_PARENTE")
	private String lienParente;
	@Column(name = "DATE_NAISSANCE")
	private Date dateNaissance;
	@Column(name = "IDCS_MERE")
	private String idcsMere;
	@Column(name = "IDCS_PERE")
	private String idcsPere;
	@Column(name = "CIN_PERE")
	private String cinPere;
	@Column(name = "CIN_MERE")
	private String cinMere;
	@Column(name = "SCOLARISE")
	private String scolarise;
	@Column(name = "TYPE_SCOLARITE")
	private String typeScolarite;
	@Column(name = "TYPE_IDENTIFIANT_SCOLARITE")
	private String typeIdentifiantScolarite;
	@Column(name = "IDENTIFIANT_SCOLARITE")
	private String identifiantScolarite;
	@Column(name = "EST_HANDICAPE")
	private Boolean estHandicape;
	@Column(name = "EST_ORPHELIN_PERE")
	private Boolean estOrphelinPere;
	@Column(name = "RESIDE_AU_MAROC")
	private Boolean resideAuMaroc;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private DemandeurEntity demandeur;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getNomFr() {
		return nomFr;
	}
	public void setNomFr(String nomFr) {
		this.nomFr = nomFr;
	}
	public String getCodeMenageRsu() {
		return codeMenageRsu;
	}
	public void setCodeMenageRsu(String codeMenageRsu) {
		this.codeMenageRsu = codeMenageRsu;
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
	public Double getScoreRsu() {
		return scoreRsu;
	}
	public void setScoreRsu(Double scoreRsu) {
		this.scoreRsu = scoreRsu;
	}
	public String getLienParente() {
		return lienParente;
	}
	public void setLienParente(String lienParente) {
		this.lienParente = lienParente;
	}
	public Date getDateNaissance() {
		return dateNaissance;
	}
	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}
	public String getIdcsMere() {
		return idcsMere;
	}
	public void setIdcsMere(String idcsMere) {
		this.idcsMere = idcsMere;
	}
	public String getIdcsPere() {
		return idcsPere;
	}
	public void setIdcsPere(String idcsPere) {
		this.idcsPere = idcsPere;
	}
	public String getCinPere() {
		return cinPere;
	}
	public void setCinPere(String cinPere) {
		this.cinPere = cinPere;
	}
	public String getCinMere() {
		return cinMere;
	}
	public void setCinMere(String cinMere) {
		this.cinMere = cinMere;
	}
	public String getScolarise() {
		return scolarise;
	}
	public void setScolarise(String scolarise) {
		this.scolarise = scolarise;
	}
	public String getTypeScolarite() {
		return typeScolarite;
	}
	public void setTypeScolarite(String typeScolarite) {
		this.typeScolarite = typeScolarite;
	}
	public String getTypeIdentifiantScolarite() {
		return typeIdentifiantScolarite;
	}
	public void setTypeIdentifiantScolarite(String typeIdentifiantScolarite) {
		this.typeIdentifiantScolarite = typeIdentifiantScolarite;
	}
	public String getIdentifiantScolarite() {
		return identifiantScolarite;
	}
	public void setIdentifiantScolarite(String identifiantScolarite) {
		this.identifiantScolarite = identifiantScolarite;
	}
	public Boolean getEstHandicape() {
		return estHandicape;
	}
	public void setEstHandicape(Boolean estHandicape) {
		this.estHandicape = estHandicape;
	}
	public Boolean getEstOrphelinPere() {
		return estOrphelinPere;
	}
	public void setEstOrphelinPere(Boolean estOrphelinPere) {
		this.estOrphelinPere = estOrphelinPere;
	}
	public Boolean getResideAuMaroc() {
		return resideAuMaroc;
	}
	public void setResideAuMaroc(Boolean resideAuMaroc) {
		this.resideAuMaroc = resideAuMaroc;
	}
	public DemandeurEntity getDemandeur() {
		return demandeur;
	}
	public void setDemandeur(DemandeurEntity demandeur) {
		this.demandeur = demandeur;
	}


}
