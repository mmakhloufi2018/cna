package ma.cdgp.af.entity.cnss;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.entity.CandidatCheckEvent;

@Entity
@Table(name = "DMD_SITUATION_CNSS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DemandeSituationCnss implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	@Column(name = "CIN")
	private String cin;
	
	@Column(name = "IDCS")
	private String idcs;

	@Column(name = "SITUATION")
	private String situation;

	@Column(name = "COUVERTURE_AF")
	private String couvertureAf;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LOT", nullable = true)
	private LotSituationCnss lot;
 
	@Column(name = "DATE_NAISSANCE")
	private String dateNaissance;
	
	@Column(name = "DATE_EFFET")
	private String dateEffet;
	
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CANDIDAT_EVENT", nullable = true)
	private CandidatCheckEvent candidatEvent;
}
