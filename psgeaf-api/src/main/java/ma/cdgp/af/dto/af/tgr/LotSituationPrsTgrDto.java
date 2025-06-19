package ma.cdgp.af.dto.af.tgr;

import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.entity.tgr.LotSituationTgr;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LotSituationPrsTgrDto {
	@JsonProperty("idLot")
	private Long idLot;
	@JsonProperty("codeMotif")
	private String codeMotif;
	
	@JsonProperty("libeMotif")
	private String libeMotif;
	
	@JsonProperty("dateLot")
	private String dateLot;
	
	
	@JsonProperty("personnes")
	private Set<DetailsLotSituationPrsTgrDto> personnes;

	public static LotSituationTgr fromRetourDto(LotSituationPrsTgrDto response, LotSituationTgr savedRR) {
		if (savedRR == null || response == null) {
			return null;
		}
		savedRR.setPersonnes(
				response.getPersonnes() != null
						? response.getPersonnes().stream().map(DetailsLotSituationPrsTgrDto::toEntity)
								.collect(Collectors.toSet())
						: null);
		return savedRR;
	}

	public static LotSituationPrsTgrDto fromEntity(LotSituationTgr savedRR) {
		if (savedRR == null) {
			return null;
		}
		LotSituationPrsTgrDto out = new LotSituationPrsTgrDto();
		out.setIdLot(savedRR.getId());
		out.setDateLot(savedRR.getDateLot());
		if (savedRR.getDemandes() != null) {
			out.setPersonnes(savedRR.getDemandes().stream().map(t -> {
				DetailsLotSituationPrsTgrDto det = new DetailsLotSituationPrsTgrDto();
				det.setCin(t.getCin());
//				det.setCouvertureAf(t.getCouvertureAf());
//				det.setSituation(t.getSituation());
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
