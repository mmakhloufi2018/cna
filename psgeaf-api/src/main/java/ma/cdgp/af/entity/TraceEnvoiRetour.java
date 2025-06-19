package ma.cdgp.af.entity;

import java.io.Serializable;
import java.sql.Clob;
//import java.util.List;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
//import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
//import javax.persistence.OneToMany;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "AF_TRACE_ENVOI_RETOUR")
public class TraceEnvoiRetour implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "ENVOI_CDG")
	@Lob
	private Clob envoiCdg;

	@Column(name = "RETOUR_RSU")
	@Lob
	private Clob retourRsu;

	@Column(name = "DATE_CDG")
	private Date dateCdg;

	@Column(name = "DATE_PARTENAIRE")
	private Date dateRsu;
	
	@Column(name = "ID_LOT")
	private Long idRetour;
	
	@Column(name = "PARTENAIRE")
	private String paretenaire;
	
	@Column(name = "TYPE_LOT")
	private String typeLot;

	public TraceEnvoiRetour() {
		super();
	}

	public TraceEnvoiRetour(Long id, Clob envoiCdg, Clob retourRsu, Date dateCdg, Date dateRsu) {
		super();
		this.id = id;
		this.envoiCdg = envoiCdg;
		this.retourRsu = retourRsu;
		this.dateCdg = dateCdg;
		this.dateRsu = dateRsu;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Clob getEnvoiCdg() {
		return envoiCdg;
	}

	public void setEnvoiCdg(Clob envoiCdg) {
		this.envoiCdg = envoiCdg;
	}

	public Clob getRetourRsu() {
		return retourRsu;
	}

	public void setRetourRsu(Clob retourRsu) {
		this.retourRsu = retourRsu;
	}

	public Date getDateCdg() {
		return this.dateCdg;
	}

	public void setDateCdg(Date dateCdg) {
		this.dateCdg = dateCdg;
	}

	public Date getDateRsu() {
		return this.dateRsu;
	}

	public void setDateRsu(Date dateRsu) {
		this.dateRsu = dateRsu;
	}

	public Long getIdRetour() {
		return idRetour;
	}

	public void setIdRetour(Long idRetour) {
		this.idRetour = idRetour;
	}
	
	

}
