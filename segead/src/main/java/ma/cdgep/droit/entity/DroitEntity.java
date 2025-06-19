package ma.cdgep.droit.entity;

import java.util.Date;
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

import ma.cdgep.dossier.entity.DossierAsdEntity;

@Entity
@Table(name = "DROIT")
public class DroitEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "DATE_CREATION")
	private Date dateCreation;
	@Column(name = "DATE_EFFET")
	private Date dateEffet;
	@Column(name = "DATE_FIN")
	private Date dateFin;
	@Column(name = "DATE_CREATION_FIN")
	private Date dateCreationFin;
	@Column(name = "MONTANT")
	private Double montant;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "droit")
	private List<DetailDroitEntity> detailDroits;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private DossierAsdEntity dossierAsd;

	public DroitEntity() {
		super();
	}

	

	public DroitEntity(Long id, Date dateCreation, Date dateEffet, Date dateFin, Date dateCreationFin, Double montant,
			List<DetailDroitEntity> detailDroits) {
		super();
		this.id = id;
		this.dateCreation = dateCreation;
		this.dateEffet = dateEffet;
		this.dateFin = dateFin;
		this.dateCreationFin = dateCreationFin;
		this.montant = montant;
		this.detailDroits = detailDroits;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public Date getDateEffet() {
		return dateEffet;
	}

	public void setDateEffet(Date dateEffet) {
		this.dateEffet = dateEffet;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	public Date getDateCreationFin() {
		return dateCreationFin;
	}

	public void setDateCreationFin(Date dateCreationFin) {
		this.dateCreationFin = dateCreationFin;
	}

	public Double getMontant() {
		return montant;
	}

	public void setMontant(Double montant) {
		this.montant = montant;
	}



	public List<DetailDroitEntity> getDetailDroits() {
		return detailDroits;
	}



	public void setDetailDroits(List<DetailDroitEntity> detailDroits) {
		this.detailDroits = detailDroits;
	}



	public DossierAsdEntity getDossierAsd() {
		return dossierAsd;
	}



	public void setDossierAsd(DossierAsdEntity dossierAsd) {
		this.dossierAsd = dossierAsd;
	}
	
	

}
