package ma.cdgp.af.ControllerThymeleaf;
import javax.persistence.*;



@Entity
public class RecapData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String mois;
    private int nbr;
    private String derniereReponse;
    private double nonEligibles;
    private String msgRetour;

    public RecapData(Long id, String type, String mois, int nbr, String derniereReponse, double nonEligibles, String msgRetour) {
        this.id = id;
        this.type = type;
        this.mois = mois;
        this.nbr = nbr;
        this.derniereReponse = derniereReponse;
        this.nonEligibles = nonEligibles;
        this.msgRetour = msgRetour;
    }


    public RecapData() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMois() {
        return mois;
    }

    public void setMois(String mois) {
        this.mois = mois;
    }

    public int getNbr() {
        return nbr;
    }

    public void setNbr(int nbr) {
        this.nbr = nbr;
    }

    public String getDerniereReponse() {
        return derniereReponse;
    }

    public void setDerniereReponse(String derniereReponse) {
        this.derniereReponse = derniereReponse;
    }

    public double getNonEligibles() {
        return nonEligibles;
    }

    public void setNonEligibles(double nonEligibles) {
        this.nonEligibles = nonEligibles;
    }

    public String getMsgRetour() {
        return msgRetour;
    }

    public void setMsgRetour(String msgRetour) {
        this.msgRetour = msgRetour;
    }


    @Override
    public String toString() {
        return "RecapData{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", mois='" + mois + '\'' +
                ", nbr=" + nbr +
                ", derniereReponse='" + derniereReponse + '\'' +
                ", nonEligibles=" + nonEligibles +
                ", msgRetour='" + msgRetour + '\'' +
                '}';
    }
}
