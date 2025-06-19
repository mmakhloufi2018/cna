package ma.rcar.rsu.dto.onousc.paiement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetailsFileRequestPaiementDto {

	private String numMassar;
	private String cnie;
	private String dateNaissance;
	private String nom;
	private String prenom;
	private String anneeScolaire;
	private String typeBource;
	private String montant;
	private String dateEffet;
	private String dateFin;
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
		db.append(valueOf(getTypeBource()));
		db.append("|");
		db.append(valueOf(getMontant()));
		db.append("|");
		db.append(valueOf(getDateEffet()));
		db.append("|");
		db.append(valueOf(getDateFin()));
		db.append("|");
		return db.toString();
	}

	public String valueOf(Object o) {
		return o != null ? String.valueOf(o) : "";
	}

}
