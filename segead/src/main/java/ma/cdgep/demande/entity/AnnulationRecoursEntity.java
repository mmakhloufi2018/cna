package ma.cdgep.demande.entity;

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
@Table(name = "DEMANDE_ANNULATION_RECOURS")
public class AnnulationRecoursEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;


	@Column(name = "REFERENCE_CNSS")
	private String referenceCnss;
	
	@Column(name = "idcs")
	private String idcs;

	@Column(name = "STATUT")
	private String statut;

	@Column(name = "MOTIF")
	private String motif;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private LotAnnulationRecoursEntity lot;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReferenceCnss() {
		return referenceCnss;
	}

	public void setReferenceCnss(String referenceCnss) {
		this.referenceCnss = referenceCnss;
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

	public String getMotif() {
		return motif;
	}

	public void setMotif(String motif) {
		this.motif = motif;
	}

	public LotAnnulationRecoursEntity getLot() {
		return lot;
	}

	public void setLot(LotAnnulationRecoursEntity lot) {
		this.lot = lot;
	}
	
	


}
