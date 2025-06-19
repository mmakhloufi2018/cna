package ma.cdgp.af.entity.notifRsu;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "RSU_DMD_NOTIFIS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DemandeNotificationRsu implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	@Column(name = "beneficiaireId")
	private Long beneficiaireId;
	@Column(name = "dateDebut")
	private String dateDebut;
	@Column
	private String dateFin;
	@Column
	private Long idcs;
	@Column
	private String dateNaissance;
	@Column
	private String genre;
	@Column
	private Double montant;

	@Column
	private String motif;
	@Column
	private Boolean actif;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LOT", nullable = true)
	private LotNotifRsu lot;
 
 
}
