package ma.cdgp.af.dto.af.tgr;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RetourBeneficiaresAquittementTgrDto {


    @JsonProperty("cin")
    private String cin;
    @JsonProperty("codeMotif")
    private String codeMotif;
    @JsonProperty("libeMotif")
    private String libeMotif;
}
