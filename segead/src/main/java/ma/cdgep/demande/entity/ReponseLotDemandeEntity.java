package ma.cdgep.demande.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "REPONSE_LOT_DEMANDE")
public class ReponseLotDemandeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "REFERENCE_LOT")
	private String referenceLot;
	@Column(name = "DATE_LOT")
	private String dateLot;
	@Column(name = "PRESTATION")
	private String prestation;
	@Column(name = "MOTIF")
	private String motif;
	@Column(name = "STATUT")
	private String statut;
	@Column(name = "TOTAL_ACCEPTES")
	private Integer totalAcceptes;
	@Column(name = "TOTAL_DOSSIERS")
	private Integer totalDossiers;
	@Column(name = "TOTAL_REJETES")
	private Integer totalRejetes;
	@Column(name = "DEMANDES_REJETEES")
	private String demandesRejetees;

	public ReponseLotDemandeEntity(Long id, String referenceLot, String dateLot, String prestation, String motif,
			String statut, Integer totalAcceptes, Integer totalDossiers, Integer totalRejetes,
			String demandesRejetees) {
		super();
		this.id = id;
		this.referenceLot = referenceLot;
		this.dateLot = dateLot;
		this.prestation = prestation;
		this.motif = motif;
		this.statut = statut;
		this.totalAcceptes = totalAcceptes;
		this.totalDossiers = totalDossiers;
		this.totalRejetes = totalRejetes;
		this.demandesRejetees = demandesRejetees;
	}

	public ReponseLotDemandeEntity() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReferenceLot() {
		return referenceLot;
	}

	public void setReferenceLot(String referenceLot) {
		this.referenceLot = referenceLot;
	}

	public String getDateLot() {
		return dateLot;
	}

	public void setDateLot(String dateLot) {
		this.dateLot = dateLot;
	}

	public String getPrestation() {
		return prestation;
	}

	public void setPrestation(String prestation) {
		this.prestation = prestation;
	}

	public String getMotif() {
		return motif;
	}

	public void setMotif(String motif) {
		this.motif = motif;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public Integer getTotalAcceptes() {
		return totalAcceptes;
	}

	public void setTotalAcceptes(Integer totalAcceptes) {
		this.totalAcceptes = totalAcceptes;
	}

	public Integer getTotalDossiers() {
		return totalDossiers;
	}

	public void setTotalDossiers(Integer totalDossiers) {
		this.totalDossiers = totalDossiers;
	}

	public Integer getTotalRejetes() {
		return totalRejetes;
	}

	public void setTotalRejetes(Integer totalRejetes) {
		this.totalRejetes = totalRejetes;
	}

	public String getDemandesRejetees() {
		return demandesRejetees;
	}

	public void setDemandesRejetees(String demandesRejetees) {
		this.demandesRejetees = demandesRejetees;
	}

}
