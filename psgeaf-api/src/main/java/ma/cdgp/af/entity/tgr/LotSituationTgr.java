package ma.cdgp.af.entity.tgr;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.collections.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.entity.LotSituationGen;

@Entity
@Table(name = "LOT_SITUATION_TGR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LotSituationTgr  extends LotSituationGen implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	@Column(name = "LOT_ID")
	private String lotId;

	@Column(name = "PARETENAIRE")
	private String partenaire;

	@Column(name = "DATE_CREATION")
	private Date dateCreation;

	@Column(name = "DATE_REPONSE")
	private Date dateReponse;

	@JsonManagedReference
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "lot", cascade = CascadeType.ALL)
	private Set<DetailsLotSituationTgr> personnes;
	
	@JsonManagedReference
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "lot", cascade = CascadeType.ALL)
	private Set<DemandeSituationTgr> demandes;
	
	@Column(name = "ETAT_LOT")
	private String etatLot;
	
	@Column(name = "DATE_LOT")	
	private String dateLot;
	
	@Column(name = "CODE_RETOUR")
	private String codeRetour;
	
	@Column(name = "MSG_RETOUR", length = 1000)
	private String messageRetour;
	
 
	@Column(name = "LOCKED_BATCH")
	private Boolean locked;
	public void setters() {
		if (CollectionUtils.isNotEmpty(getPersonnes())) {
			getPersonnes().forEach(r -> {
				r.setLot(this);
			});
		}
		
		if (CollectionUtils.isNotEmpty(getDemandes())) {
			getDemandes().forEach(r -> {
				r.setLot(this);
			});
		}
	}
	@Override
	public Long getIdLot() {
		return getId();
	}



	@Override
	public String getType() {
		return getPartenaire();
	}
	
}
