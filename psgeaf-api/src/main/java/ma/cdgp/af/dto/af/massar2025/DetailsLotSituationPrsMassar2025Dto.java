package ma.cdgp.af.dto.af.massar2025;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.entity.massar2025.DetailsLotSituationMassar2025;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetailsLotSituationPrsMassar2025Dto {

    @JsonProperty("CodeEleve")
    private String CodeEleve;
    @JsonProperty("CinEleve")
    private String cinEleve;
    @JsonProperty("Genre")
    private String genre;
    @JsonProperty("CodeNationalite")
    private String codeNationalite;
    @JsonProperty("Email")
    private String email;
    @JsonProperty("TypeCandidat")
    private String typeCandidat;
    @JsonProperty("nomEleveFr")
    private String nomEleveFr;
    @JsonProperty("nomEleveAr")
    private String nomEleveAr;
    @JsonProperty("prenomEleveFr")
    private String prenomEleveFr;
    @JsonProperty("prenomEleveAr")
    private String prenomEleveAr;
    @JsonProperty("dateNaisEleve")
    private String dateNaisEleve;
    @JsonProperty("CodeNiveau")
    private String CodeNiveau;
    @JsonProperty("CodeEtablissement")
    private String codeEtablissement;
    @JsonProperty("CodeProvince")
    private String codeProvince;
    @JsonProperty("CinTuteur")
    private String CinTuteur;
    @JsonProperty("CinPere")
    private String CinPere;
    @JsonProperty("CinMere")
    private String CinMere;
    @JsonProperty("LieuNaissance")
    private String lieuNaissance;
    @JsonProperty("LastSituationScolarisation")
    private String lastSituationScolarisation;
    @JsonProperty("MoyenneGenerale")
    private String moyenneGenerale;
    @JsonProperty("ResultatFinAnnee")
    private String resultatFinAnnee;
    @JsonProperty("SituationScolarisation")
    private String SituationScolarisation;
    @JsonProperty("AnneeScolaire")
    private String anneeScolaire;



    public static DetailsLotSituationMassar2025 toEntity(DetailsLotSituationPrsMassar2025Dto in) {
        if (in == null) {
            return null;
        }
        DetailsLotSituationMassar2025 out = new DetailsLotSituationMassar2025();
        out.setCodeEleve(in.getCodeEleve());
        out.setNomEleveFr(in.getNomEleveFr());
        out.setNomEleveAr(in.getNomEleveAr());
        out.setPrenomEleveFr(in.getPrenomEleveFr());
        out.setPrenomEleveAr(in.getPrenomEleveAr());
        out.setDateNaisEleve(in.getDateNaisEleve());
        out.setCodeNiveau(in.getCodeNiveau());
        out.setCinTuteur(in.getCinTuteur());
        out.setCinPere(in.getCinPere());
        out.setCinMere(in.getCinMere());
        out.setSituationScolarisation(in.getSituationScolarisation());
        out.setAnneeScolaire(in.getAnneeScolaire());
        out.setLieuNaissance(in.getLieuNaissance());
        out.setLastSituationScolarisation(in.getLastSituationScolarisation());
        out.setMoyenneGenerale(in.getMoyenneGenerale());
        out.setResultatFinAnnee(in.getResultatFinAnnee());
        out.setCodeEtablissement(in.getCodeEtablissement());
        out.setCodeProvince(in.getCodeProvince());
        out.setCinEleve(in.getCinEleve());
        out.setGenre(in.getGenre());
        out.setCodeNationalite(in.getCodeNationalite());
        out.setEmail(in.getEmail());
        out.setTypeCandidat(in.getTypeCandidat());
        return out;
    }


    public static DetailsLotSituationPrsMassar2025Dto fromEntity(DetailsLotSituationMassar2025 in) {
        if (in == null) {
            return null;
        }
        DetailsLotSituationPrsMassar2025Dto out = new DetailsLotSituationPrsMassar2025Dto();
        out.setCodeEleve(in.getCodeEleve());
        out.setNomEleveFr(in.getNomEleveFr());
        out.setNomEleveAr(in.getNomEleveAr());
        out.setPrenomEleveFr(in.getPrenomEleveFr());
        out.setPrenomEleveAr(in.getPrenomEleveAr());
        out.setDateNaisEleve(in.getDateNaisEleve());
        out.setCodeNiveau(in.getCodeNiveau());
        out.setCinTuteur(in.getCinTuteur());
        out.setCinPere(in.getCinPere());
        out.setCinMere(in.getCinMere());
        out.setSituationScolarisation(in.getSituationScolarisation());
        out.setAnneeScolaire(in.getAnneeScolaire());
        return out;
    }

}
