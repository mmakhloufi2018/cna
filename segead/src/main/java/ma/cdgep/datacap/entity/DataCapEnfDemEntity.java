package ma.cdgep.datacap.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DATA_CAP_ENF_DEM")
public class DataCapEnfDemEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "MOIS_ANNEE")
	private String moisAnnee;

	@Column(name = "DOSSIER")
	private Long dossier;

	@Column(name = "MEMBRE")
	private Long membre;

	@Column(name = "SCORE_RSU")
	private Double scoreRSU;

	@Column(name = "SALARIE")
	private String salarie;

	@Column(name = "PENSIONNE")
	private String pensionne;

	@Column(name = "BENEFICIE_AF")
	private String beneficieAF;

	@Column(name = "EST_CHEF_MENAGE")
	private String estChefMenage;
	
	@Column(name = "RESIDE_MAROC")
	private String resideMaroc;
	
	@Column(name = "CODE_RSU")
	private String codeRsu;

	@Column(name = "CLOTURE_DEMANDE")
	private String clotureDemande;

	@Column(name = "BENEFICIE_FEF")
	private String beneficieFef;

	@Column(name = "GENRE")
	private String genre;

	@Column(name = "ETAT_MATRIMONIALE")
	private String etatMatrimoniale;

	public DataCapEnfDemEntity() {
		super();
	}

	public DataCapEnfDemEntity(Long id, String moisAnnee, Long dossier, Long membre, Double scoreRSU,
			String salarie, String pensionne, String beneficieAF, String estChefMenage, String resideMaroc,
			String codeRsu, String clotureDemande, String beneficieFef, String genre, String etatMatrimoniale) {
		super();
		this.id = id;
		this.moisAnnee = moisAnnee;
		this.dossier = dossier;
		this.membre = membre;
		this.scoreRSU = scoreRSU;
		this.salarie = salarie;
		this.pensionne = pensionne;
		this.beneficieAF = beneficieAF;
		this.estChefMenage = estChefMenage;
		this.resideMaroc = resideMaroc;
		this.codeRsu = codeRsu;
		this.clotureDemande = clotureDemande;
		this.beneficieFef = beneficieFef;
		this.genre = genre;
		this.etatMatrimoniale = etatMatrimoniale;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMoisAnnee() {
		return moisAnnee;
	}

	public void setMoisAnnee(String moisAnnee) {
		this.moisAnnee = moisAnnee;
	}

	public Long getDossier() {
		return dossier;
	}

	public void setDossier(Long dossier) {
		this.dossier = dossier;
	}

	public Long getMembre() {
		return membre;
	}

	public void setMembre(Long membre) {
		this.membre = membre;
	}

	public Double getScoreRSU() {
		return scoreRSU;
	}

	public void setScoreRSU(Double scoreRSU) {
		this.scoreRSU = scoreRSU;
	}

	public String getSalarie() {
		return salarie;
	}

	public void setSalarie(String salarie) {
		this.salarie = salarie;
	}

	public String getPensionne() {
		return pensionne;
	}

	public void setPensionne(String pensionne) {
		this.pensionne = pensionne;
	}

	public String getBeneficieAF() {
		return beneficieAF;
	}

	public void setBeneficieAF(String beneficieAF) {
		this.beneficieAF = beneficieAF;
	}

	public String getEstChefMenage() {
		return estChefMenage;
	}

	public void setEstChefMenage(String estChefMenage) {
		this.estChefMenage = estChefMenage;
	}

	public String getResideMaroc() {
		return resideMaroc;
	}

	public void setResideMaroc(String resideMaroc) {
		this.resideMaroc = resideMaroc;
	}

	public String getCodeRsu() {
		return codeRsu;
	}

	public void setCodeRsu(String codeRsu) {
		this.codeRsu = codeRsu;
	}

	public String getClotureDemande() {
		return clotureDemande;
	}

	public void setClotureDemande(String clotureDemande) {
		this.clotureDemande = clotureDemande;
	}

	public String getBeneficieFef() {
		return beneficieFef;
	}

	public void setBeneficieFef(String beneficieFef) {
		this.beneficieFef = beneficieFef;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getEtatMatrimoniale() {
		return etatMatrimoniale;
	}

	public void setEtatMatrimoniale(String etatMatrimoniale) {
		this.etatMatrimoniale = etatMatrimoniale;
	}

}