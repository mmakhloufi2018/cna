package ma.cdgp.af.dto.af.cmr;


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
public class BenefNotificationCmrDto {

    @JsonProperty("CIN")
    private String cin;
    @JsonProperty("IDCS")
    private String idcs;
    @JsonProperty("Statut")
    private String statut;

}












