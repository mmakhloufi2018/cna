package ma.cdgep.dossier.entity;

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

import ma.cdgep.demande.entity.DemandeurEntity;

@Entity
@Table(name = "M_DEMANDEUR")
public class M_DemandeurEntity {

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
	@Column(name = "SCORE_RSU")
	private Double scoreRsu;
	@Column(name = "GENRE")
	private String genre;
	@Column(name = "DATE_NAISSANCE")
	private Date dateNaissance;
	@Column(name = "NOM_AR")
	private String nomAr;
	@Column(name = "PRENOM_AR")
	private String prenomAr;
	@Column(name = "ADRESSE")
	private String adresse;
	@Column(name = "ETAT_MATRIMONIAL")
	private String etatMatrimonial;
	@Column(name = "RESIDE_AU_MAROC")
	private Boolean resideAuMaroc;

	@Column(name = "EST_CHEF_MENAGE_RSU")
	private Boolean estChefMenageRSU;
	
	@Column(name = "DATE_DEBUT")
	private Date dateDebut;
	@Column(name = "DATE_FIN")
	private Date dateFin;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private DossierAsdEntity dossierAsd;

	public M_DemandeurEntity() {
		super();
	}

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

	public Double getScoreRsu() {
		return scoreRsu;
	}

	public void setScoreRsu(Double scoreRsu) {
		this.scoreRsu = scoreRsu;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public Date getDateNaissance() {
		return dateNaissance;
	}

	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
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

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getEtatMatrimonial() {
		return etatMatrimonial;
	}

	public void setEtatMatrimonial(String etatMatrimonial) {
		this.etatMatrimonial = etatMatrimonial;
	}

	public Boolean getResideAuMaroc() {
		return resideAuMaroc;
	}

	public void setResideAuMaroc(Boolean resideAuMaroc) {
		this.resideAuMaroc = resideAuMaroc;
	}

	public Boolean getEstChefMenageRSU() {
		return estChefMenageRSU;
	}

	public void setEstChefMenageRSU(Boolean estChefMenageRSU) {
		this.estChefMenageRSU = estChefMenageRSU;
	}

	public DossierAsdEntity getDossierAsd() {
		return dossierAsd;
	}

	public void setDossierAsd(DossierAsdEntity dossierAsd) {
		this.dossierAsd = dossierAsd;
	}
	
	public static M_DemandeurEntity from(DemandeurEntity in) {
		if(in == null)
			return null;
		M_DemandeurEntity out = new M_DemandeurEntity();
		out.setIdcs(in.getIdcs());
		out.setCin(in.getCin());
		out.setNomFr(in.getNomFr());
		out.setCodeMenageRsu(in.getCodeMenageRsu());
		out.setPrenomFr(in.getPrenomFr());
//		out.setGsm(in.getGsm());
		out.setScoreRsu(in.getScoreRsu());
//		out.setRib(in.getRib());
		out.setGenre(in.getGenre());
		out.setDateNaissance(in.getDateNaissance());
		out.setNomAr(in.getNomAr());
		out.setPrenomAr(in.getPrenomAr());
		out.setAdresse(in.getAdresse());
//		out.setEtatMatrilonial(in.getEtatMatrilonial());
		return out;
	}

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}
	
	

}
