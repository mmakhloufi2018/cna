package ma.cdgep.demande.entity;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ma.cdgep.demande.dto.DemandeAjoutMembreDto;
import ma.cdgep.demande.dto.LotAjoutMembreDto;
import ma.cdgep.paiement.entity.EcheanceEntity;
import ma.cdgep.utils.Utils;
@Entity
@Table(name = "LOT_AJOUT_MEMBRE")
public class LotAjoutMembreEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "REFERENCE_LOT")
	private String referenceLot;

	@Column(name = "DATE_LOT")
	private Date dateLot;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lot", cascade = CascadeType.ALL)
	private List<DemandeAjoutMembreEntity> demandes;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private EcheanceEntity echeance;
	
	@Column(name = "TOTAL_DOSSIERS")
	private Integer totalDossiers;

	@Column(name = "TOTAL_ACCEPTES")
	private Integer totalAcceptes;

	@Column(name = "TOTAL_REJETES")
	private Integer totalRejetes;

	@Column(name = "STATUT")
	private String statut;

	@Column(name = "MOTIF")
	private String motif;

	public String getReferenceLot() {
		return referenceLot;
	}

	public void setReferenceLot(String referenceLot) {
		this.referenceLot = referenceLot;
	}

	public Date getDateLot() {
		return dateLot;
	}

	public void setDateLot(Date dateLot) {
		this.dateLot = dateLot;
	}

	public List<DemandeAjoutMembreEntity> getDemandes() {
		return demandes;
	}

	public void setDemandes(List<DemandeAjoutMembreEntity> demandes) {
		this.demandes = demandes;
	}

	public static LotAjoutMembreEntity to(LotAjoutMembreDto lotDto) {
		if (lotDto == null)
			return null;
		LotAjoutMembreEntity entity = new LotAjoutMembreEntity();

		entity.setDateLot(Utils.stringToDate(lotDto.getDateLot(), Utils.FOURMAT_DATE_STRING));
		entity.setReferenceLot(lotDto.getReferenceLot());

		if (lotDto.getDemandes() != null) {

			entity.setDemandes(
					lotDto.getDemandes().stream().map(DemandeAjoutMembreDto::to).collect(Collectors.toList()));

			entity.getDemandes().forEach(c -> c.setLot(entity));
		}
		return entity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EcheanceEntity getEcheance() {
		return echeance;
	}

	public void setEcheance(EcheanceEntity echeance) {
		this.echeance = echeance;
	}

	public Integer getTotalDossiers() {
		return totalDossiers;
	}

	public void setTotalDossiers(Integer totalDossiers) {
		this.totalDossiers = totalDossiers;
	}

	public Integer getTotalAcceptes() {
		return totalAcceptes;
	}

	public void setTotalAcceptes(Integer totalAcceptes) {
		this.totalAcceptes = totalAcceptes;
	}

	public Integer getTotalRejetes() {
		return totalRejetes;
	}

	public void setTotalRejetes(Integer totalRejetes) {
		this.totalRejetes = totalRejetes;
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

	
}
