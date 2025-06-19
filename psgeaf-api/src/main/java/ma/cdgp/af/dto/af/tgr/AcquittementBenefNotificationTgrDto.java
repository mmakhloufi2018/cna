package ma.cdgp.af.dto.af.tgr;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.entity.tgr.RetourBeneficiaresAquittementTgr;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AcquittementBenefNotificationTgrDto {


    @JsonProperty("dateLot")
    private String dateLot;

    @JsonProperty("codeMotif")
    private String codeMotif;

    @JsonProperty("libeMotif")
    private String libeMotif;

    @JsonProperty("idLot")
    private String idLot;
    @JsonProperty("nombreEnregistrement")
    private String nombreEnregistrement;
    @JsonProperty("beneficiareNonEnregistrer")
    private Set<RetourBeneficiaresAquittementTgrDto> beneficiareNonEnregistrer;
	@Override
	public String toString() {
		return ":: AcquittementBenefNotificationTgrDto [dateLot=" + dateLot + ", codeMotif=" + codeMotif + ", libeMotif="
				+ libeMotif + ", idLot=" + idLot + ", nombreEnregistrement=" + nombreEnregistrement + "]";
	}
    
    
    
}
