package ma.cdgep.paiement.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PAIEMENT")
public class PaiementEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	private LotPaiementEntity lotPaiement;

	@Column(name = "DATE_DEBUT")
	private Date dateDebut;

	@Column(name = "DATE_FIN")
	private Date dateFin;
	
	@Column(name = "REFERENCE_PAIEMENT")
	private String referencePaiement;

	@Column(name = "REFERENCE_MENAGE")
	private String referenceMenage;

	@Column(name = "MONTANT_OPERATION")
	private Double MontantOperation;

	@Column(name = "FLAG_SUSPENSION")
	private Integer flagSuspension;
	
	@Column(name = "REFERENCE_CNSS")
	private String referenceCnss;
	
	@Column(name = "SCORE")
	private Double score;

	@Column(name = "REFERENCE_DEMANDE")
	private String referenceDemande;


	public PaiementEntity() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LotPaiementEntity getLotPaiement() {
		return lotPaiement;
	}

	public void setLotPaiement(LotPaiementEntity lotPaiement) {
		this.lotPaiement = lotPaiement;
	}

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	public String getReferenceMenage() {
		return referenceMenage;
	}

	public void setReferenceMenage(String referenceMenage) {
		this.referenceMenage = referenceMenage;
	}

	public Double getMontantOperation() {
		return MontantOperation;
	}

	public void setMontantOperation(Double montantOperation) {
		MontantOperation = montantOperation;
	}

	public Integer getFlagSuspension() {
		return flagSuspension;
	}

	public void setFlagSuspension(Integer flagSuspension) {
		this.flagSuspension = flagSuspension;
	}

	public String getReferencePaiement() {
		return referencePaiement;
	}

	public void setReferencePaiement(String referencePaiement) {
		this.referencePaiement = referencePaiement;
	}

	public String getReferenceCnss() {
		return referenceCnss;
	}

	public void setReferenceCnss(String referenceCnss) {
		this.referenceCnss = referenceCnss;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String getReferenceDemande() {
		return referenceDemande;
	}

	public void setReferenceDemande(String referenceDemande) {
		this.referenceDemande = referenceDemande;
	}

}
