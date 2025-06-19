package ma.cdgp.af.dto.af.cmr;

import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.entity.cmr.LotSituationCmr;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LotSituationPrsCmrDto {
	@JsonProperty("IdLot")
	private Long idLot;
	@JsonProperty("Personnes")
	private Set<DetailsLotSituationPrsCmrDto> personnes;

	public static LotSituationCmr fromRetourDto(LotSituationPrsCmrDto response, LotSituationCmr savedRR) {
		if (savedRR == null || response == null) {
			return null;
		}
		savedRR.setPersonnes(
				response.getPersonnes() != null
						? response.getPersonnes().stream().map(DetailsLotSituationPrsCmrDto::toEntity)
								.collect(Collectors.toSet())
						: null);
		return savedRR;
	}

	public static LotSituationPrsCmrDto fromEntity(LotSituationCmr savedRR) {
		if (savedRR == null) {
			return null;
		}
		LotSituationPrsCmrDto out = new LotSituationPrsCmrDto();
//		if (savedRR.getLotId() != null) {
//			out.setIdLot(Long.valueOf(savedRR.getLotId()));
//		}
		out.setIdLot(savedRR.getId());
		if (savedRR.getDemandes() != null) {
			out.setPersonnes(savedRR.getDemandes().stream().map(t -> {
				DetailsLotSituationPrsCmrDto det = new DetailsLotSituationPrsCmrDto();
				det.setCin(t.getCin());
				det.setCouvertureAf(t.getCouvertureAf());
				det.setSituation(t.getSituation());
				det.setDateNaissance(t.getDateNaissance());
				return det;
			}).collect(Collectors.toSet()));
		}
		return out;
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
