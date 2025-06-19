package ma.cdgp.af.dto.af.rsu;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DemandeRsuDto {


    @JsonProperty("idcs")
    private String idcs;

    @JsonProperty("genre")
    private String genre;

    @JsonProperty("dateNaissance")
    private String dateNaissance;

    @JsonProperty("chefMenage")
    private Boolean chefMenage;


    public DemandeRsuDto(String idcs, String dateNaissance, boolean chefMenage, String genre) {
        this.idcs = idcs;
        this.dateNaissance = dateNaissance;
        this.chefMenage = chefMenage;
        this.genre = genre;
    }
}








