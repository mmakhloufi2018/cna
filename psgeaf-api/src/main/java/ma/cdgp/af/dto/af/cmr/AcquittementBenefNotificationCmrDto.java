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
public class AcquittementBenefNotificationCmrDto {


    @JsonProperty("dateLot")
    private String dateLot;

    @JsonProperty("nombreEnregistrement")
    private String nombreEnregistrement;

    @JsonProperty("IdLot")
    private String IdLot;

    @JsonProperty("cinNonEnregistres")
    private String cinNonEnregistres;

}








