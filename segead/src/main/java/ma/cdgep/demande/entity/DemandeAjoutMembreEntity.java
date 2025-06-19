package ma.cdgep.demande.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "DEMANDE_AJOUT_MEMBRE")
public class DemandeAjoutMembreEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	
	@Column(name = "reference_Cnss")
	private String referenceCnss;
	@Column(name = "reference_Demande")
	private String referenceDemande;
	@Column(name = "idcs_Demandeur")
	private String idcsDemandeur;

	@Column(name = "STATUT")
	private String statut;

	@Column(name = "MOTIF_REJET")
	private String motifRejet;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "demande", cascade = CascadeType.ALL)
	private List<A_EnfantEntity> enfants;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "demande", cascade = CascadeType.ALL)
	private List<A_ConjointEntity> conjoints;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private LotAjoutMembreEntity lot;

	public DemandeAjoutMembreEntity() {
		super();
	}

	public String getReferenceCnss() {
		return referenceCnss;
	}

	public void setReferenceCnss(String referenceCnss) {
		this.referenceCnss = referenceCnss;
	}

	public String getReferenceDemande() {
		return referenceDemande;
	}

	public void setReferenceDemande(String referenceDemande) {
		this.referenceDemande = referenceDemande;
	}

	public String getIdcsDemandeur() {
		return idcsDemandeur;
	}

	public void setIdcsDemandeur(String idcsDemandeur) {
		this.idcsDemandeur = idcsDemandeur;
	}

	public List<A_EnfantEntity> getEnfants() {
		return enfants;
	}

	public void setEnfants(List<A_EnfantEntity> enfants) {
		this.enfants = enfants;
	}

	public List<A_ConjointEntity> getConjoints() {
		return conjoints;
	}

	public void setConjoints(List<A_ConjointEntity> conjoints) {
		this.conjoints = conjoints;
	}

	public LotAjoutMembreEntity getLot() {
		return lot;
	}

	public void setLot(LotAjoutMembreEntity lot) {
		this.lot = lot;
	}

	public DemandeAjoutMembreEntity(String referenceCnss, String referenceDemande, String idcsDemandeur,
			List<A_EnfantEntity> enfants, List<A_ConjointEntity> conjoints) {
		super();
		this.referenceCnss = referenceCnss;
		this.referenceDemande = referenceDemande;
		this.idcsDemandeur = idcsDemandeur;
		this.enfants = enfants;
		this.conjoints = conjoints;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	
}
