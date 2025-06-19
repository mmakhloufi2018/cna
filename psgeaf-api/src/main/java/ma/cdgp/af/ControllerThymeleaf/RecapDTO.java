package ma.cdgp.af.ControllerThymeleaf;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecapDTO {
    private String mois;
    private int nbr;
    private String derniereReponse;
    private double nonEligibles;

}
