package ma.cdgep.datacap.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DATA_CAP_ENF_ENF_SCOL")
public class DataCapEnfEnfScolEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "MOIS_ANNEE")
	private String moisAnnee;

	@Column(name = "DOSSIER")
	private Long dossier;

	@Column(name = "MEMBRE")
	private Long membre;

	@Column(name = "SCOLARISE")
	private String scolarise;

	@Column(name = "MONTANT_BOURSE")
	private Double montantBourse;

	@Column(name = "DATE_EFFET_SCOLARISE")
	private Date dateEffetScolarise;

	@Column(name = "DATE_EFFET_BOURSE")
	private Date dateEffetBourse;

	public DataCapEnfEnfScolEntity() {
		super();
	}

	public DataCapEnfEnfScolEntity(Long id, String moisAnnee, Long dossier, Long membre, String scolarise,
			Double montantBourse, Date dateEffetScolarise, Date dateEffetBourse) {
		super();
		this.id = id;
		this.moisAnnee = moisAnnee;
		this.dossier = dossier;
		this.membre = membre;
		this.scolarise = scolarise;
		this.montantBourse = montantBourse;
		this.dateEffetScolarise = dateEffetScolarise;
		this.dateEffetBourse = dateEffetBourse;
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

	public String getScolarise() {
		return scolarise;
	}

	public void setScolarise(String scolarise) {
		this.scolarise = scolarise;
	}

	public Double getMontantBourse() {
		return montantBourse;
	}

	public void setMontantBourse(Double montantBourse) {
		this.montantBourse = montantBourse;
	}

	public Date getDateEffetScolarise() {
		return dateEffetScolarise;
	}

	public void setDateEffetScolarise(Date dateEffetScolarise) {
		this.dateEffetScolarise = dateEffetScolarise;
	}

	public Date getDateEffetBourse() {
		return dateEffetBourse;
	}

	public void setDateEffetBourse(Date dateEffetBourse) {
		this.dateEffetBourse = dateEffetBourse;
	}

}