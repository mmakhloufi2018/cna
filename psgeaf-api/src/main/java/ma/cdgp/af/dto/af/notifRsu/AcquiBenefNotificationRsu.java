package ma.cdgp.af.dto.af.notifRsu;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class AcquiBenefNotificationRsu {

	@JsonProperty("beneficiaireId")
	private Long beneficiaireId;
	@JsonProperty("codeRetour")
	private String codeRetour;
	@JsonProperty("messageRetour")
	private String messageRetour;
	 

}
