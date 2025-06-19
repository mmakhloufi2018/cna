package ma.cdgp.af.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "LOT_SITUATION_HISTO")
public class RetourTracabilite implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "ID_LOT_SP", nullable = true)
	private LotSituationGen retour;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "MESSAGE", length = 1000)
	private String message;

	@Column(name = "DATE_EVENT")
	private Date dateEvent;

	public RetourTracabilite(Long id2) {
		this.id = id2;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LotSituationGen getRetour() {
		return retour;
	}

	public void setRetour(LotSituationGen retour) {
		this.retour = retour;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getDateEvent() {
		return dateEvent;
	}

	public void setDateEvent(Date dateEvent) {
		this.dateEvent = dateEvent;
	}

	public RetourTracabilite() {
		super();
	}

	public RetourTracabilite(Long id, LotSituationGen retour, String status, String message, Date dateEvent) {
		super();
		this.id = id;
		this.retour = retour;
		this.status = status;
		this.message = message;
		this.dateEvent = dateEvent;
	}

}
