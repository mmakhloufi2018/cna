package ma.cdgp.af.dto.af.rsu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailsDemandeRsuDto {


    @JsonProperty("idcs")
    private Long idcs;
    @JsonProperty("cnie")
    private String cnie;
    @JsonProperty("codeRetour")
    private String codeRetour;
    @JsonProperty("msgRetour")
    private String msgRetour;
    @JsonProperty("nomAr")
    private String nomAr;
    @JsonProperty("nomFr")
    private String nomFr;
    @JsonProperty("prenomFr")
    private String prenomFr;
    @JsonProperty("prenomAr")
    private String prenomAr;
    @JsonProperty("numTel")
    private String numTel;
    @JsonProperty("lienParente")
    private String lienParente;
    @JsonProperty("prefecture")
    private String prefecture;
    @JsonProperty("commune")
    private String commune;
    @JsonProperty("annexe")
    private String annexe;
    @JsonProperty("codeMenage")
    private String codeMenage;
    @JsonProperty("score")
    private BigDecimal score;
    @JsonProperty("dateScore")
    private String dateScore;
    @JsonProperty("seuil")
    private BigDecimal  seuil;
    @JsonProperty("statutAd")
    private String statutAd;
    @JsonProperty("adresse")
    private String adresse;
    @JsonProperty("etatMatrimonial")
    private String etatMatrimonial;
    @JsonProperty("tailleMenage")
    private Long tailleMenage;
    @JsonProperty("typePieceIdentite")
    private Long typePieceIdentite;

}
