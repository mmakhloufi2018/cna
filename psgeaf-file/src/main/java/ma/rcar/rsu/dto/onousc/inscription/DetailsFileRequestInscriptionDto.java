package ma.rcar.rsu.dto.onousc.inscription;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;




/**
 * @author BAKHALED Ibrahim.
 *
 */


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetailsFileRequestInscriptionDto {
	private String numMassar;
	private String cnie;
	private String dateNaissance;
	private String nom;
	private String prenom;
	private String anneeScolaire;
	private String scolarise;
	private String boursier;
	private String etat;
	private String motif;
	private String codeMotif;
	private Integer rang;

	private Boolean isMissingFields;

	public String buildLine() {
		StringBuilder db = new StringBuilder();
		db.append(valueOf(getNumMassar()));
		db.append("|");
		db.append(valueOf(getCnie()));
		db.append("|");
		db.append(valueOf(getDateNaissance()));
		db.append("|");
		db.append(valueOf(getNom()));
		db.append("|");
		db.append(valueOf(getPrenom()));
		db.append("|");
		db.append(valueOf(getAnneeScolaire()));
		db.append("|");
		db.append(valueOf(getScolarise()));
		db.append("|");
		db.append(valueOf(getBoursier()));
		db.append("|");
		return db.toString();
	}

	public String valueOf(Object o) {
		return o != null ? String.valueOf(o) : "";
	}

}
