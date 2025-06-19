package ma.cdgep.demande.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "LOT_DEMANDE_BRUTE")
public class LotDemandeBruteEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "REFERENCE_LOT")
	private String referenceLot;

	@Column(name = "DATE_LOT")
	private Date dateLot;

	@Lob
	@Column(name = "CONTENU")
	private String contenu;

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

	public Date getDateLot() {
		return dateLot;
	}

	public void setDateLot(Date dateLot) {
		this.dateLot = dateLot;
	}

	public String getContenu() {
		return contenu;
	}

	public void setContenu(String contenu) {
		this.contenu = contenu;
	}

	public LotDemandeBruteEntity(String referenceLot, Date dateLot, String contenu) {
		super();
		this.referenceLot = referenceLot;
		this.dateLot = dateLot;
		this.contenu = contenu;
	}

	public LotDemandeBruteEntity() {
		super();
	}
	
	
}
