package ma.cdgep.datacap.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DATA_CAP_ENF_CONJ")
public class DataCapConjDemEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "MOIS_ANNEE")
	private String moisAnnee;

	@Column(name = "DOSSIER")
	private Long dossier;

	@Column(name = "MEMBRE")
	private Long membre;

	@Column(name = "SALARIE")
	private String salarie;

	@Column(name = "PENSIONNE")
	private String pensionne;

	@Column(name = "BENEFICIE_AF")
	private String beneficieAF;

	@Column(name = "RESIDE_MAROC")
	private String resideMaroc;
	@Column(name = "CODE_RSU")
	private String codeRsu;

	public DataCapConjDemEntity() {
		super();
	}

	public DataCapConjDemEntity(Long id, String moisAnnee, Long dossier, Long membre, String salarie,
			String pensionne, String beneficieAF, String resideMaroc, String codeRsu) {
		super();
		this.id = id;
		this.moisAnnee = moisAnnee;
		this.dossier = dossier;
		this.membre = membre;
		this.salarie = salarie;
		this.pensionne = pensionne;
		this.beneficieAF = beneficieAF;
		this.resideMaroc = resideMaroc;
		this.codeRsu = codeRsu;
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

}