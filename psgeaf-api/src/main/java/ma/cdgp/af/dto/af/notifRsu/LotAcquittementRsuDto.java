package ma.cdgp.af.dto.af.notifRsu;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class LotAcquittementRsuDto {
	@JsonProperty("idTransaction")
	private Long idTransaction;
	@JsonProperty("listeBeneficiaires")
	private Set<AcquiBenefNotificationRsu> listeBeneficiaires;
}
