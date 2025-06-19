package ma.cdgp.af.entity;

import java.io.Serializable;
//import java.util.List;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
//import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "AF_CANDIDAT_EVENT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CandidatCheckEvent implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "CANDIDAT_ID")
	private Long idCandidat;
	
	@Column(name = "PARTENAIRE")
	private String partenaire;
	
	@Column(name = "CIN")
	private String cin;
	
	@Column(name = "IDCS")
	private String idcs;
	
	private Date dateCreation;
	
	@ManyToOne
	@JoinColumn(name = "ID_COLLECTED", nullable = true)
	private CandidatCollected collected;
	
	@PrePersist
	public void beforeSave() {
		this.dateCreation = new Date();
	}
	public static CandidatCheckEvent fromCollected(CandidatCollected in, String partenaire) {
		if (in == null) return null;
		CandidatCheckEvent out = new CandidatCheckEvent();
		out.setIdCandidat(in.getIdCandidat());
		out.setCin(in.getCin());
		out.setIdcs(in.getIdcs());
		out.setPartenaire(partenaire);
		out.setCollected(in);
		return out;
	}
}
