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
public class BenefNotificationRsu {

	@JsonProperty("beneficiaireId")
	private Long beneficiaireId;
	@JsonProperty("dateDebut")
	private String dateDebut;
	@JsonProperty("dateFin")
	private String dateFin;
	@JsonProperty("idcs")
	private Long idcs;
	@JsonProperty("dateNaissance")
	private String dateNaissance;
	@JsonProperty("genre")
	private String genre;
	@JsonProperty("montant")
	private Double montant;

	@JsonProperty("motif")
	private String motif;
	@JsonProperty("actif")
	private Boolean actif;

}
