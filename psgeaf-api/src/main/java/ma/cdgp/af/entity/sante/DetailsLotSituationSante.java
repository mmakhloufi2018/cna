package ma.cdgp.af.entity.sante;

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
@Table(name = "DETAILS_LOT_SITUATION_SANTE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetailsLotSituationSante implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	@Column(name = "IDCS")
	private String idcs;

	@Column(name = "TYPE_HANDICAP")
	private Integer typehandicap;

	@Column(name = "DEGRE_HANDICAP")
	private Integer degrehandicap;

	@Column(name = "BESOIN_TIERCE_PERSONNE")
	private Integer besointiercepersonne;
	
	@Column(name = "TRAITEMENT_COUTEUX")
	private Integer traitementcouteux;
	
	@Column(name = "TRAITEMENT_LOBTERME")
	private Integer traitementlobterme;
	
	@Column(name = "BESOIN_TECHNOLOGIE")
	private Integer besointechnologie;

	@Column(name = "DUREE_HANDICAP")
	private Integer dureehandicap;

	@Column(name = "MEDICAL_COMMISION_DATE")
	private String medicalCommissionDate;


	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LOT", nullable = true)
	private LotSituationSante lot;

	@Column(name = "CODE_RETOUR")
	private String codeRetour;

	@Column(name = "MSG_RETOUR", length = 1000)
	private String messageRetour;

	 


}
