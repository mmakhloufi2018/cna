package ma.cdgep.demande.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import ma.cdgep.demande.dto.DemandeDto;
import ma.cdgep.dossier.entity.DossierAsdEntity;
import ma.cdgep.dossier.entity.M_AutreMembreEntity;
import ma.cdgep.dossier.entity.M_ConjointEntity;
import ma.cdgep.dossier.entity.M_DemandeurEntity;
import ma.cdgep.dossier.entity.M_EnfantEntity;

@Entity
@Table(name = "DEMANDE")
public class DemandeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "DATE_DEMANDE")
	private Date dateDemande;

	@Column(name = "REFERENCE_CNSS")
	private String referenceCnss;

	@Column(name = "IMMATRICULATION")
	private String immatriculation;

	@Column(name = "TYPE_PRESTATION")
	private String typePrestation;

	@Column(name = "TYPE_DEMANDE")
	private String typeDemande;

	@Column(name = "STATUT")
	private String statut;

	@Column(name = "MOTIF_REJET")
	private String motifRejet;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private LotDemandeEntity lot;

	@OneToOne(cascade = CascadeType.ALL)
	private DemandeurEntity demandeur;

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

	public String getReferenceCnss() {
		return referenceCnss;
	}

	public void setReferenceCnss(String referenceCnss) {
		this.referenceCnss = referenceCnss;
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

	public String getTypeDemande() {
		return typeDemande;
	}

	public void setTypeDemande(String typeDemande) {
		this.typeDemande = typeDemande;
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

	public LotDemandeEntity getLot() {
		return lot;
	}

	public void setLot(LotDemandeEntity lot) {
		this.lot = lot;
	}

	public DemandeurEntity getDemandeur() {
		return demandeur;
	}

	public void setDemandeur(DemandeurEntity demandeur) {
		this.demandeur = demandeur;
	}

	public static DossierAsdEntity to(DemandeEntity in) {
		if (in == null)
			return null;

		List<M_ConjointEntity> listMConjouts = new ArrayList<M_ConjointEntity>();
		if (in.getDemandeur() != null && in.getDemandeur().getConjoints() != null) {
			listMConjouts = in.getDemandeur().getConjoints().stream().map(M_ConjointEntity::from)
					.collect(Collectors.toList());
		}
		List<M_EnfantEntity> listMEnfant = new ArrayList<M_EnfantEntity>();
		if (in.getDemandeur() != null && in.getDemandeur().getEnfants() != null) {
			listMEnfant = in.getDemandeur().getEnfants().stream().map(M_EnfantEntity::from)
					.collect(Collectors.toList());
		}
		List<M_AutreMembreEntity> listMAutres = new ArrayList<M_AutreMembreEntity>();
		if (in.getDemandeur() != null && in.getDemandeur().getAutreMembres() != null) {
			listMAutres = in.getDemandeur().getAutreMembres().stream().map(M_AutreMembreEntity::from)
					.collect(Collectors.toList());
		}
//		Long id, String reference, String immatriculation, String typePrestation, String statut,
//		Date dateCreation, Date dateCloture, String codeMenageRsu, List<M_ConjointEntity> membreConjoints,
//		List<M_EnfantEntity> membreEntants, List<M_AutreMembreEntity> membreAutreMembres,
//		M_DemandeurEntity membreDemandeur
		DossierAsdEntity out = new DossierAsdEntity(null, in.getReferenceCnss(), in.getImmatriculation(), // Immatriculation
				in.getTypePrestation(), DemandeDto.STATUT_TRAITE, in.getDateDemande(), null,
				in.getDemandeur().getCodeMenageRsu(), listMConjouts, listMEnfant, listMAutres,
				M_DemandeurEntity.from(in.getDemandeur()));
		if (out.getMembreDemandeur() != null) {
			out.getMembreDemandeur().setDateDebut(in.getDateDemande());
			out.getMembreDemandeur().setDossierAsd(out);
		}
		if (out.getMembreConjoints() != null)
			out.getMembreConjoints().forEach(a -> {
				a.setDossierAsd(out);
				a.setDateDebut(in.getDateDemande());
			});
		if (out.getMembreEntants() != null)
			out.getMembreEntants().forEach(a -> {
				a.setDossierAsd(out);
				a.setDateDebut(in.getDateDemande());
			});
		if (out.getMembreAutreMembres() != null)
			out.getMembreAutreMembres().forEach(a -> {
				a.setDossierAsd(out);
				a.setDateDebut(in.getDateDemande());
			});

		return out;
	}

	public static List<String> getReferenceDemandes(List<DemandeEntity> demandes) {
		if(demandes == null)
			return null;
		List<String> listReference = new ArrayList<String>();
		for (DemandeEntity demande : demandes) {
			listReference.add(demande.getReferenceCnss());
		}
		return listReference;
	}

}
