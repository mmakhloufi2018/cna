package ma.cdgp.af.entity.notifRsu;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "RSU_REPONSE_NOTIF")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetailsNotificationRsu implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;
 
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LOT", nullable = true)
	private LotNotifRsu lot;

	@Column 
	private String beneficiaireId;
	@Column 
	private String codeRetour;
	@Column 
	private String messageRetour;

 
	

}
