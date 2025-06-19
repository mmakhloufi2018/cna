package ma.cdgp.af.dto.af.cmr;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.entity.cmr.DetailsLotSituationCmr;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetailsLotSituationPrsCmrDto {
	@JsonProperty("CIN")
	private String cin;
	@JsonProperty("Situation")
	private String situation;
	@JsonProperty("CouvertureAF")
	private String couvertureAf;
	@JsonProperty("DateNaissance")
	private String dateNaissance;
	@JsonProperty("dateEffet")
	private String dateEffet;
	
	public static DetailsLotSituationCmr toEntity(DetailsLotSituationPrsCmrDto in) {
		if (in == null) {
			return null;
		}
		DetailsLotSituationCmr out = new DetailsLotSituationCmr();
		out.setCin(in.getCin());
		out.setCouvertureAf(in.getCouvertureAf());
		out.setDateNaissance(in.getDateNaissance());
		out.setSituation(in.getSituation());
		out.setDateEffet(in.getDateEffet());
		return out;
	}
}
