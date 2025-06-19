package ma.cdgp.af.dto.af.sante;

import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.entity.sante.LotSituationSante;
import ma.cdgp.af.entity.tgr.LotSituationTgr;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LotSituationPrsSanteDto {
	@JsonProperty("idLot")
	private Long idLot;
	@JsonProperty("key")
	private String key;
	@JsonProperty("dateLot")
	private String dateLot;
	@JsonProperty("personnes")
	private Set<DetailsLotSituationPrsSanteDto> personnes;

	public static LotSituationSante fromRetourDto(LotSituationPrsSanteDto response, LotSituationSante savedRR) {
		if (savedRR == null || response == null) {
			return null;
		}

		savedRR.setPersonnes(
				response.getPersonnes().stream().map(DetailsLotSituationPrsSanteDto::toEntity)
								.collect(Collectors.toSet()));
		return savedRR;
	}

	public static LotSituationPrsSanteDto fromEntity(LotSituationSante savedRR) {
		if (savedRR == null) {
			return null;
		}
		LotSituationPrsSanteDto out = new LotSituationPrsSanteDto();
//		if (savedRR.getLotId() != null) {
//			out.setIdLot(Long.valueOf(savedRR.getLotId()));
//		}
		out.setDateLot(savedRR.getDateLot());
		out.setIdLot(savedRR.getId());
		if (savedRR.getDemandes() != null) {
			out.setPersonnes(savedRR.getDemandes().stream().map(in -> {
				DetailsLotSituationPrsSanteDto d = new DetailsLotSituationPrsSanteDto();
				d.setIdcs(in.getIdcs());
				return d;
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
