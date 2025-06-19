package ma.cdgp.af.entity.mi;

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
@Table(name = "DMD_SITUATION_MI")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DemandeSituationMi implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	@Column(name = "CIN")
	private String cin;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LOT", nullable = true)
	private LotSituationMi lot;
 
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CANDIDAT_EVENT", nullable = true)
	private CandidatCheckEvent candidatEvent;
}
