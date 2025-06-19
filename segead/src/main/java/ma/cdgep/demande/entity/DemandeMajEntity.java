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

import ma.cdgep.paiement.entity.EcheanceEntity;

@Entity
@Table(name = "DEMANDE_MAJ")
public class DemandeMajEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "DATE_DEMANDE")
	private Date dateDemande;

	@Column(name = "STATUT")
	private String statut;

	@Column(name = "MOTIF_REJET")
	private String motifRejet;
	
	@Column(name = "REFERENCE_CNSS")
	private String referenceCnss;
	
	@Column(name = "IDCS_ENFANT")
	private String idcsEnfant;
	
	@Column(name = "EST_HANDICAPE")
	private String estHandicape;
	
	@Column(name = "IDENTIFIANT_SCOLARITE")
	private String identifiantScolarite;
	
	@Column(name = "SCOLARISE")
	private String scolarise;
	
	@Column(name = "TYPE_IDENTIFIANTSCOLARITE")
	private String typeIdentifiantScolarite;
	
	@Column(name = "EST_ORPHELIN_PERE")
	private String estOrphelinPere;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private EcheanceEntity echeance;

	public DemandeMajEntity() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDateDemande() {
		return dateDemande;
	}

	public void setDateDemande(Date dateDemande) {
		this.dateDemande = dateDemande;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public String getMotifRejet() {
		return motifRejet;
	}

	public void setMotifRejet(String motifRejet) {
		this.motifRejet = motifRejet;
	}

	public String getReferenceCnss() {
		return referenceCnss;
	}

	public void setReferenceCnss(String referenceCnss) {
		this.referenceCnss = referenceCnss;
	}

	public String getIdcsEnfant() {
		return idcsEnfant;
	}

	public void setIdcsEnfant(String idcsEnfant) {
		this.idcsEnfant = idcsEnfant;
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

	public String getScolarise() {
		return scolarise;
	}

	public void setScolarise(String scolarise) {
		this.scolarise = scolarise;
	}

	public String getTypeIdentifiantScolarite() {
		return typeIdentifiantScolarite;
	}

	public void setTypeIdentifiantScolarite(String typeIdentifiantScolarite) {
		this.typeIdentifiantScolarite = typeIdentifiantScolarite;
	}

	public String getEstOrphelinPere() {
		return estOrphelinPere;
	}

	public void setEstOrphelinPere(String estOrphelinPere) {
		this.estOrphelinPere = estOrphelinPere;
	}

	public EcheanceEntity getEcheance() {
		return echeance;
	}

	public void setEcheance(EcheanceEntity echeance) {
		this.echeance = echeance;
	}

	public DemandeMajEntity(String statut, String motifRejet, String referenceCnss, String idcsEnfant,
			String estHandicape, String identifiantScolarite, String scolarise, String typeIdentifiantScolarite,
			String estOrphelinPere, EcheanceEntity echeance) {
		super();
		this.statut = statut;
		this.motifRejet = motifRejet;
		this.referenceCnss = referenceCnss;
		this.idcsEnfant = idcsEnfant;
		this.estHandicape = estHandicape;
		this.identifiantScolarite = identifiantScolarite;
		this.scolarise = scolarise;
		this.typeIdentifiantScolarite = typeIdentifiantScolarite;
		this.estOrphelinPere = estOrphelinPere;
		this.echeance = echeance;
	}
	
	
	

	

}
