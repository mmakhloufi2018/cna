package ma.cdgp.af.dto.af.rcar;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.entity.rcar.DetailsLotSituationRcar;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetailsLotSituationPrsRcarDto {
	@JsonProperty("cin")
	private String cin;
	@JsonProperty("type")
	private String type;
	
	public static DetailsLotSituationRcar toEntity(DetailsLotSituationPrsRcarDto in) {
		if (in == null) {
			return null;
		}
		DetailsLotSituationRcar out = new DetailsLotSituationRcar();
		out.setCin(in.getCin());
		out.setSituation(in.getType());
		return out;
	}
}
