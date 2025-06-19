package ma.cdgp.af.entity.notifRsu;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.entity.LotSituationGen;
import org.apache.commons.collections.CollectionUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "RSU_LOT_NOTIF")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LotNotifRsu  extends LotSituationGen implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	@Column(name = "PARETENAIRE")
	private String partenaire;
	
	@Column(name = "ID_TRANSACTION")
	private String idTransaction;

	@Column(name = "DATE_CREATION")
	private Date dateCreation;

	@Column(name = "DATE_REPONSE")
	private Date dateReponse;

	@JsonManagedReference
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "lot", cascade = CascadeType.ALL)
	private Set<DetailsNotificationRsu> personnes;
	
	@JsonManagedReference
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "lot", cascade = CascadeType.ALL)
	private Set<DemandeNotificationRsu> demandes;
	
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
