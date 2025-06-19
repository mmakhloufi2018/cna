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

@Entity
@Table(name = "RECOURS")
public class RecoursEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "DATE_DEMANDE")
	private Date dateDemande;

	@Column(name = "REFERENCE")
	private String reference;
	@Column(name = "idcs")
	private String idcs;

	@Column(name = "STATUT")
	private String statut;

	@Column(name = "MOTIF_REJET")
	private String motifRejet;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private LotRecoursEntity lot;

	public RecoursEntity() {
		super();
	}

	public RecoursEntity(Long id, Date dateDemande, String reference, String idcs, String statut, String motifRejet,
			LotRecoursEntity lot) {
		super();
		this.id = id;
		this.dateDemande = dateDemande;
		this.reference = reference;
		this.idcs = idcs;
		this.statut = statut;
		this.motifRejet = motifRejet;
		this.lot = lot;
	}

	public Date getDateDemande() {
		return dateDemande;
	}

	public void setDateDemande(Date dateDemande) {
		this.dateDemande = dateDemande;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getIdcs() {
		return idcs;
	}

	public void setIdcs(String idcs) {
		this.idcs = idcs;
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

	public LotRecoursEntity getLot() {
		return lot;
	}

	public void setLot(LotRecoursEntity lot) {
		this.lot = lot;
	}

}
