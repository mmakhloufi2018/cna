package ma.rcar.rsu.dto.mesrsi;


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
public class DetailsFileRequestMesrsiDto {
    private String numMassar;
    private String cnie;
    private String dateNaissance;
    private String nom;
    private String prenom;
    private String anneeScolaire;
    private String etat;
    private String motif;
    private String codeMotif;
    private Integer rang;
    private Boolean isMissingFields;

    public String buildLine() {
        StringBuilder db = new StringBuilder();
        db.append(valueOf(getNumMassar()));
        db.append("\t");
        db.append(valueOf(getCnie()));
        db.append("\t");
        db.append(valueOf(getDateNaissance()));
        db.append("\t");
        db.append(valueOf(getNom()));
        db.append("\t");
        db.append(valueOf(getPrenom()));
        db.append("\t");
        db.append(valueOf(getAnneeScolaire()));
        db.append("\t");
        return db.toString();
    }

    public String valueOf(Object o) {
        return o != null ? String.valueOf(o) : "";
    }
}
