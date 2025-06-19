package ma.rcar.rsu.dto.mhai;

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
public class DetailsFileRequestMhaiDto {
    private String idEtudiant;
    private String numMassar;
    private String cnie;
    private String dateNaissance;
    private String nationalite;
    private String nomFr;
    private String prenomFr;
    private String nomAr;
    private String prenomAr;
    private String anneeScolaire;
    private String scolarise;
    private String boursier;
    private String niveau;
    private String montant;
    private String etat;
    private String motif;
    private Integer rang;
    private String codeMotif;
    private Boolean isMissingFields;


    public String buildLine() {
        StringBuilder db = new StringBuilder();
        db.append(valueOf(getIdEtudiant()));
        db.append(";");
        db.append(valueOf(getNumMassar()));
        db.append(";");
        db.append(valueOf(getCnie()));
        db.append(";");
        db.append(valueOf(getDateNaissance()));
        db.append(";");
        db.append(valueOf(getNomFr()));
        db.append(";");
        db.append(valueOf(getPrenomFr()));
        db.append(";");
        db.append(valueOf(getNomAr()));
        db.append(";");
        db.append(valueOf(getPrenomAr()));
        db.append(";");
        db.append(valueOf(getAnneeScolaire()));
        db.append(";");
        db.append(valueOf(getBoursier()));
        db.append(";");
        db.append(valueOf(getScolarise()));
        db.append(";");
        db.append(valueOf(getMontant()));
        db.append(";");
        db.append(valueOf(getNationalite()));
        db.append(";");
        db.append(valueOf(getNiveau()));
        db.append(";");


        return db.toString();
    }

    public String valueOf(Object o) {
        return o != null ? String.valueOf(o) : "";
    }
}
