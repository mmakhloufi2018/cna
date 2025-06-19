package ma.cdgp.af.dto.af.cnss;

import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.entity.cnss.LotSituationCnss;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LotSituationPrsCnssDto {
	@JsonProperty("iden_lot")
	private Long referenceLot;
	@JsonProperty("date_lot")
	private String dateLot;
	@JsonProperty("tablbene")
	private Set<DetailsLotSituationPrsCnssDto> personnes;

	public static LotSituationCnss fromRetourDto(LotSituationPrsCnssDto response, LotSituationCnss savedRR) {
		if (savedRR == null || response == null) {
			return null;
		}
		savedRR.setDateLot(response.getDateLot());
		savedRR.setPersonnes(
				response.getPersonnes() != null
						? response.getPersonnes().stream().map(DetailsLotSituationPrsCnssDto::toEntity)
								.collect(Collectors.toSet())
						: null);
		return savedRR;
	}
	
	public static LotSituationCnss fromResponseDto(LotReponseSituationCnssDto response, LotSituationCnss savedRR) {
		if (savedRR == null || response == null) {
			return null;
		}
		savedRR.setDateLot(response.getDateLot());
		savedRR.setPersonnes(
				response.getResultats() != null
						? response.getResultats().stream().map(DetailsLotSituationPrsCnssDto::toEntity)
								.collect(Collectors.toSet())
						: null);
		return savedRR;
	}
	
	

	public static LotSituationPrsCnssDto fromEntity(LotSituationCnss savedRR) {
		if (savedRR == null) {
			return null;
		}
		LotSituationPrsCnssDto out = new LotSituationPrsCnssDto();
		out.setReferenceLot(savedRR.getId());
		out.setDateLot(savedRR.getDateLot());
		if (savedRR.getDemandes() != null) {
			out.setPersonnes(savedRR.getDemandes().stream().map(t -> {
				DetailsLotSituationPrsCnssDto det = new DetailsLotSituationPrsCnssDto();
				det.setCin(t.getCin());
				det.setDateNaissance(t.getDateNaissance());
				det.setIdcs(t.getIdcs());
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
