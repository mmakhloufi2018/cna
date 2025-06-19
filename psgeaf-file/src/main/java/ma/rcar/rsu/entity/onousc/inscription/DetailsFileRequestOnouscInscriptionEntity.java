/**
 * 
 */
package ma.rcar.rsu.entity.onousc.inscription;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import jakarta.persistence.*;

import org.apache.commons.beanutils.BeanUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.rcar.rsu.dto.onousc.inscription.DetailsFileRequestInscriptionDto;


/**
 * @author BAKHALED Ibrahim.
 *
 */



@Entity
@Table(name = "DETAILS_FILE_ONOUSC_INSCRIP")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetailsFileRequestOnouscInscriptionEntity implements Serializable {
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

	@Column(name = "SCOLARISE")
	private Boolean scolarise;

	@Column(name = "BOURSIER")
	private Boolean boursier;

	@Column(name = "ETAT")
	private String etat;

	@Column(name = "MOTIF")
	private String motif;

	@ManyToOne
	@JoinColumn(name = "FILE_REQUEST_ONOUSC_ID", nullable = false)
	private FileRequestOnouscInscriptionEntity fileRequest;

	@Column(name = "RANG")
	private Integer rang;

	@Column(name = "CODE_MOTIF")
	private String codeMotif;

	public static DetailsFileRequestOnouscInscriptionEntity from(DetailsFileRequestInscriptionDto in) {
		if (in == null)
			return null;
		DetailsFileRequestOnouscInscriptionEntity out = new DetailsFileRequestOnouscInscriptionEntity();
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
