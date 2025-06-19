/**
 * 
 */
package ma.rcar.rsu.entity.onousc.paiement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.rcar.rsu.dto.onousc.paiement.DetailsFileRequestPaiementDto;
import org.apache.commons.beanutils.BeanUtils;

import jakarta.persistence.*;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;


/**
 * @author BAKHALED Ibrahim.
 *
 */


@Entity(name = "DETAILS_FILE_ONOUSC_PAIEMENT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetailsFileRequestOnouscPaiementEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "NUM_MASSAR")
	private String numMassar;

	@Column(name = "CNIE")
	private String cnie;

	@Column(name = "DATE_NAISSANCE")
	private String dateNaissance;

	@Column(name = "NOM")
	private String nom;

	@Column(name = "PRENOM")
	private String prenom;

	@Column(name = "ANNEE_SCOLAIRE")
	private String anneeScolaire;


	@Column(name = "TYPE_BOURCE")
	private Boolean typeBource;


	@Column(name = "MONTANT")
	private String montant;

	@Column(name = "DATE_EFFET")
	private String dateEffet;

	@Column(name = "DATE_FIN")
	private String dateFin;

	@Column(name = "ETAT")
	private String etat;

	@Column(name = "MOTIF")
	private String motif;

	@ManyToOne
	@JoinColumn(name = "FILE_REQUEST_ONOUSC_ID", nullable = false)
	private FileRequestOnouscPaiementEntity fileRequest;

	@Column(name = "RANG")
	private Integer rang;

	@Column(name = "CODE_MOTIF")
	private String codeMotif;

	public static DetailsFileRequestOnouscPaiementEntity from(DetailsFileRequestPaiementDto in) {
		if (in == null)
			return null;
		DetailsFileRequestOnouscPaiementEntity out = new DetailsFileRequestOnouscPaiementEntity();
		try {
			BeanUtils.copyProperties(out, in);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return out;
	}
}
