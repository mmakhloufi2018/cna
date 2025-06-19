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

import ma.cdgep.demande.entity.ConjointEntity;

@Entity
@Table(name = "M_CONJOINT")
public class M_ConjointEntity {

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
	@Column(name = "LIEN_PARENTE")
	private String lienParente;
	@Column(name = "DATE_NAISSANCE")
	private Date dateNaissance;
	@Column(name = "NOM_AR")
	private String nomAr;
	@Column(name = "PRENOM_AR")
	private String prenomAr;
	@Column(name = "RESIDE_AU_MAROC")
	private Boolean resideAuMaroc;
	@Column(name = "DATE_DEBUT")
	private Date dateDebut;
	@Column(name = "DATE_FIN")
	private Date dateFin;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private DossierAsdEntity dossierAsd;

	public M_ConjointEntity() {
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

	public Boolean getResideAuMaroc() {
		return resideAuMaroc;
	}

	public void setResideAuMaroc(Boolean resideAuMaroc) {
		this.resideAuMaroc = resideAuMaroc;
	}

	public DossierAsdEntity getDossierAsd() {
		return dossierAsd;
	}

	public void setDossierAsd(DossierAsdEntity dossierAsd) {
		this.dossierAsd = dossierAsd;
	}
	
	public static M_ConjointEntity from(ConjointEntity in) {
		if (in == null)
			return null;

		M_ConjointEntity mConj = new M_ConjointEntity();
		mConj.setIdcs(in.getIdcs());

		mConj.setCin(in.getCin());
		mConj.setNomFr(in.getNomFr());
		mConj.setCodeMenageRsu(in.getCodeMenageRsu());
		mConj.setPrenomFr(in.getPrenomFr());
		mConj.setScoreRsu(in.getScoreRsu());
		mConj.setLienParente(in.getLienParente());
		mConj.setDateNaissance(in.getDateNaissance());
//		mConj.setAdresse(in.getAdresse());
		mConj.setNomAr(in.getNomAr());
		mConj.setPrenomAr(in.getPrenomAr());
		mConj.setResideAuMaroc(in.getResideAuMaroc());
		return mConj;
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
