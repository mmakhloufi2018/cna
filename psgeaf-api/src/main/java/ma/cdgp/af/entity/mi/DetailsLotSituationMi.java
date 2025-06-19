package ma.cdgp.af.entity.mi;

import java.io.Serializable;
import java.util.stream.Collectors;

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
import ma.cdgp.af.dto.af.cmr.DetailsLotSituationPrsCmrDto;
import ma.cdgp.af.dto.af.cmr.LotSituationPrsCmrDto;

@Entity
@Table(name = "DETAILS_LOT_SITUATION_MI")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetailsLotSituationMi implements Serializable {

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

	@Column(name = "CODE_RETOUR")
	private String codeRetour;

	@Column(name = "MSG_RETOUR")
	private String messageRetour;

	@Column(name = "DATE_NAISSANCE")
	private String dateNaissance;
	
	@Column(name = "DATE_DECES")
	private String dateDeces;
	
	@Column 
	private String etatCivil;
	 
	@Column 
	private String nom;
	
	@Column 
	private String prenom;

	@Column 
	private String situation;

}
