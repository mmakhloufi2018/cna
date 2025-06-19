package ma.rcar.rsu.dto.fef;


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
public class DetailsFileRequestFefDto {
    private String cin;
    private String datePec;
    private String dateExpiration;
    private String etat;
    private String motif;
    private String codeMotif;
    private Integer rang;
    private Boolean isMissingFields;


    public String buildLine() {
        StringBuilder db = new StringBuilder();
        db.append(valueOf(getCin()));
        db.append(";");
        db.append(valueOf(getDatePec()));
        db.append(";");
        db.append(valueOf(getDateExpiration()));
        db.append(";");
        return db.toString();
    }

    public String valueOf(Object o) {
        return o != null ? String.valueOf(o) : "";
    }
}
