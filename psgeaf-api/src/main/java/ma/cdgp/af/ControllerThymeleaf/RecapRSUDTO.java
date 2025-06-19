package ma.cdgp.af.ControllerThymeleaf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecapRSUDTO {
    private String mois;
    private String msgRetour;
    private int nbr;

    // Getters and setters
}
