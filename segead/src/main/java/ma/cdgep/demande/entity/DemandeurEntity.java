package ma.cdgep.demande.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "D_DEMANDEUR")
public class DemandeurEntity {

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

	@OneToOne(cascade = CascadeType.ALL)
	private DemandeEntity demande;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "demandeur")
	private List<ConjointEntity> conjoints;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "demandeur")
	private List<EnfantEntity> enfants;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "demandeur")
	private List<AutreMembreEntity> autreMembres;

	public DemandeurEntity() {
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

	public DemandeEntity getDemande() {
		return demande;
	}

	public void setDemande(DemandeEntity demande) {
		this.demande = demande;
	}

	public List<ConjointEntity> getConjoints() {
		return conjoints;
	}

	public void setConjoints(List<ConjointEntity> conjoints) {
		this.conjoints = conjoints;
	}

	public List<EnfantEntity> getEnfants() {
		return enfants;
	}

	public void setEnfants(List<EnfantEntity> enfants) {
		this.enfants = enfants;
	}

	public List<AutreMembreEntity> getAutreMembres() {
		return autreMembres;
	}

	public void setAutreMembres(List<AutreMembreEntity> autreMembres) {
		this.autreMembres = autreMembres;
	}

	public DemandeurEntity(String idcs, DemandeEntity demande) {
		super();
		this.idcs = idcs;
		this.demande = demande;
	}
	
	

}
