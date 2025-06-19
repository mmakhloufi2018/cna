package ma.cdgep.datacap.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DATA_CAP_ENF_ENF")
public class DataCapEnfEnfEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "MOIS_ANNEE")
	private String moisAnnee;

	@Column(name = "DOSSIER")
	private Long dossier;

	@Column(name = "MEMBRE")
	private Long membre;

	@Column(name = "CODE_RSU")
	private String codeRsu;

	@Column(name = "RESIDE_MAROC")
	private String resideMaroc;

	@Column(name = "AGE")
	private Integer age;

	@Column(name = "IDCS_MERE")
	private String idcsMere;

	@Column(name = "IDCS_PERE")
	private String idcsPere;

	@Column(name = "EST_HANDICAPE")
	private String estHandicape;

	@Column(name = "ORPHELIN_PERE")
	private String orphelinPere;

	public DataCapEnfEnfEntity() {
		super();
	}

	public DataCapEnfEnfEntity(Long id, String moisAnnee, Long dossier, Long membre, String codeRsu,
			String resideMaroc, Integer age, String idcsMere, String idcsPere, String estHandicape,
			String orphelinPere) {
		super();
		this.id = id;
		this.moisAnnee = moisAnnee;
		this.dossier = dossier;
		this.membre = membre;
		this.codeRsu = codeRsu;
		this.resideMaroc = resideMaroc;
		this.age = age;
		this.idcsMere = idcsMere;
		this.idcsPere = idcsPere;
		this.estHandicape = estHandicape;
		this.orphelinPere = orphelinPere;
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

	public String getCodeRsu() {
		return codeRsu;
	}

	public void setCodeRsu(String codeRsu) {
		this.codeRsu = codeRsu;
	}

	public String getResideMaroc() {
		return resideMaroc;
	}

	public void setResideMaroc(String resideMaroc) {
		this.resideMaroc = resideMaroc;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
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

	public String getEstHandicape() {
		return estHandicape;
	}

	public void setEstHandicape(String estHandicape) {
		this.estHandicape = estHandicape;
	}

	public String getOrphelinPere() {
		return orphelinPere;
	}

	public void setOrphelinPere(String orphelinPere) {
		this.orphelinPere = orphelinPere;
	}

}