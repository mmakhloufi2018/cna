package ma.cdgep.dossier.entity;

import java.util.ArrayList;
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

import ma.cdgep.droit.entity.DroitEntity;

@Entity
@Table(name = "DOSSIER_ASD")
public class DossierAsdEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "REFERENCE")
	private String reference;
	@Column(name = "IMMATRICULATION")
	private String immatriculation;
	@Column(name = "TYPE_PRESTATION")
	private String typePrestation;
	@Column(name = "STATUT")
	private String statut;
	@Column(name = "DATE_CREATION")
	private Date dateCreation;
	@Column(name = "DATE_CLOTURE")
	private Date dateCloture;
	@Column(name = "CODE_MENAGE_RSU")
	private String codeMenageRsu;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "dossierAsd")
	private List<M_ConjointEntity> membreConjoints;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "dossierAsd")
	private List<M_EnfantEntity> membreEntants;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "dossierAsd")
	private List<M_AutreMembreEntity> membreAutreMembres;

	@OneToOne(cascade = CascadeType.ALL)
	private M_DemandeurEntity membreDemandeur;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "dossierAsd")
	private List<DroitEntity> droits;

	public DossierAsdEntity() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getImmatriculation() {
		return immatriculation;
	}

	public void setImmatriculation(String immatriculation) {
		this.immatriculation = immatriculation;
	}

	public String getTypePrestation() {
		return typePrestation;
	}

	public void setTypePrestation(String typePrestation) {
		this.typePrestation = typePrestation;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public Date getDateCloture() {
		return dateCloture;
	}

	public void setDateCloture(Date dateCloture) {
		this.dateCloture = dateCloture;
	}

	public List<M_ConjointEntity> getMembreConjoints() {
		return membreConjoints;
	}

	public void setMembreConjoints(List<M_ConjointEntity> membreConjoints) {
		this.membreConjoints = membreConjoints;
	}

	public List<M_EnfantEntity> getMembreEntants() {
		return membreEntants;
	}

	public void setMembreEntants(List<M_EnfantEntity> membreEntants) {
		this.membreEntants = membreEntants;
	}

	public List<M_AutreMembreEntity> getMembreAutreMembres() {
		return membreAutreMembres;
	}

	public void setMembreAutreMembres(List<M_AutreMembreEntity> membreAutreMembres) {
		this.membreAutreMembres = membreAutreMembres;
	}

	public M_DemandeurEntity getMembreDemandeur() {
		return membreDemandeur;
	}

	public void setMembreDemandeur(M_DemandeurEntity membreDemandeur) {
		this.membreDemandeur = membreDemandeur;
	}

	public String getCodeMenageRsu() {
		return codeMenageRsu;
	}

	public void setCodeMenageRsu(String codeMenageRsu) {
		this.codeMenageRsu = codeMenageRsu;
	}

	public DossierAsdEntity(Long id, String reference, String immatriculation, String typePrestation, String statut,
			Date dateCreation, Date dateCloture, String codeMenageRsu, List<M_ConjointEntity> membreConjoints,
			List<M_EnfantEntity> membreEntants, List<M_AutreMembreEntity> membreAutreMembres,
			M_DemandeurEntity membreDemandeur) {
		super();
		this.id = id;
		this.reference = reference;
		this.immatriculation = immatriculation;
		this.typePrestation = typePrestation;
		this.statut = statut;
		this.dateCreation = dateCreation;
		this.dateCloture = dateCloture;
		this.codeMenageRsu = codeMenageRsu;
		this.membreConjoints = membreConjoints;
		this.membreEntants = membreEntants;
		this.membreAutreMembres = membreAutreMembres;
		this.membreDemandeur = membreDemandeur;
	}
	
	

	public List<DroitEntity> getDroits() {
		return droits;
	}

	public void setDroits(List<DroitEntity> droits) {
		this.droits = droits;
	}

	public static List<String> getImmatriculations(List<DossierAsdEntity> dossiers) {
		if (dossiers == null)
			return null;
		List<String> listImmatriculation = new ArrayList<String>();
		for (DossierAsdEntity dossier : dossiers) {
			listImmatriculation.add(dossier.getImmatriculation());
		}
		return listImmatriculation;
	}
}
