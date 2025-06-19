package ma.cdgep.droit.entity;

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
@Table(name = "DETAIL_DROIT")
public class DetailDroitEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "RUBRIQUE")
	private String rubrique;
	@Column(name = "MONTANT")
	private Double montant;
	@Column(name = "OFFSET")
	private String offset;
	
	@Column(name = "MEMBRE")
	private Long membre;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private DroitEntity droit;

	public DetailDroitEntity() {
		super();
	}

	public DetailDroitEntity(Long id, String rubrique, Double montant, String offset, DroitEntity droit) {
		super();
		this.id = id;
		this.rubrique = rubrique;
		this.montant = montant;
		this.offset = offset;
		this.droit = droit;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRubrique() {
		return rubrique;
	}

	public void setRubrique(String rubrique) {
		this.rubrique = rubrique;
	}

	public Double getMontant() {
		return montant;
	}

	public void setMontant(Double montant) {
		this.montant = montant;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public DroitEntity getDroit() {
		return droit;
	}

	public void setDroit(DroitEntity droit) {
		this.droit = droit;
	}

	public Long getMembre() {
		return membre;
	}

	public void setMembre(Long membre) {
		this.membre = membre;
	}
	
	
	
	

}
