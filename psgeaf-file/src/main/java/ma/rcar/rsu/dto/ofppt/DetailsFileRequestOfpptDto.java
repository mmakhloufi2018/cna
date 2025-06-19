package ma.rcar.rsu.dto.ofppt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.rcar.rsu.entity.mesrsi.FileRequestMesrsiEntity;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;





/**
 * @author BAKHALED Ibrahim.
 *
 */




@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetailsFileRequestOfpptDto {
    private String numLot;
    private String cef;
    private String numMassar;
    private String cnie;
    private String dateNaissance;
    private String nom;
    private String prenom;
    private String anneeScolaire;
    private String boursier;
    private String montant;
    private String etat;
    private String motif;
    private Integer rang;
    private String codeMotif;
    private Boolean isMissingFields;


    public String buildLine() {
        StringBuilder db = new StringBuilder();
        db.append(valueOf(getNumLot()));
        db.append(";");
        db.append(valueOf(getCef()));
        db.append(";");
        db.append(valueOf(getNumMassar()));
        db.append(";");
        db.append(valueOf(getCnie()));
        db.append(";");
        db.append(valueOf(getDateNaissance()));
        db.append(";");
        db.append(valueOf(getNom()));
        db.append(";");
        db.append(valueOf(getPrenom()));
        db.append(";");
        db.append(valueOf(getAnneeScolaire()));
        db.append(";");
        db.append(valueOf(getBoursier()));
        db.append(";");
        db.append(valueOf(getMontant()));
        db.append(";");
        return db.toString();
    }

    public String valueOf(Object o) {
        return o != null ? String.valueOf(o) : "";
    }
}
