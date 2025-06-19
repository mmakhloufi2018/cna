package ma.cdgp.af.dto.af.rcar;

import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.entity.rcar.LotSituationRcar;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LotSituationPrsRcarDto {
	@JsonProperty("idLot")
	private Long idLot;
	@JsonProperty("personnes")
	private Set<DetailsLotSituationPrsRcarDto> personnes;

	public static LotSituationRcar fromRetourDto(LotSituationPrsRcarDto response, LotSituationRcar savedRR) {
		if (savedRR == null || response == null) {
			return null;
		}
		savedRR.setPersonnes(
				response.getPersonnes() != null
						? response.getPersonnes().stream().map(DetailsLotSituationPrsRcarDto::toEntity)
								.collect(Collectors.toSet())
						: null);
		return savedRR;
	}

	public static LotSituationPrsRcarDto fromEntity(LotSituationRcar savedRR) {
		if (savedRR == null) {
			return null;
		}
		LotSituationPrsRcarDto out = new LotSituationPrsRcarDto();
		out.setIdLot(savedRR.getId());
		return out;
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
