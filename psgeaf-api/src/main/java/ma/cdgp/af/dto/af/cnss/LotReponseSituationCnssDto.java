package ma.cdgp.af.dto.af.cnss;

import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
@JsonInclude(Include.NON_NULL)
public class LotReponseSituationCnssDto {
	@JsonProperty("iden_lot")
	private Long referenceLot;
	@JsonProperty("date_lot")
	private String dateLot;
	@JsonProperty("tablbene")
	private Set<DetailsLotSituationPrsCnssDto> personnes;
	@JsonProperty("tablresu")
	private Set<DetailsLotSituationPrsCnssDto> resultats;
	@JsonProperty("status")
	private String status;
	@JsonProperty("error")
	private String message;
	@JsonProperty("messageType")
	private String messageType;
	
	
	
	
	public static LotSituationCnss fromRetourDto(LotReponseSituationCnssDto response, LotSituationCnss savedRR) {
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

	public static LotReponseSituationCnssDto fromEntity(LotSituationCnss savedRR) {
		if (savedRR == null) {
			return null;
		}
		LotReponseSituationCnssDto out = new LotReponseSituationCnssDto();
		if (savedRR.getLotId() != null) {
			out.setReferenceLot(Long.valueOf(savedRR.getLotId()));;
		}
		out.setDateLot(savedRR.getDateLot());
		if (savedRR.getDemandes() != null) {
			out.setPersonnes(savedRR.getDemandes().stream().map(t -> {
				DetailsLotSituationPrsCnssDto det = new DetailsLotSituationPrsCnssDto();
				det.setCin(t.getCin());
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

	public Boolean isAcquittement() {
		return status != null && message != null && message.trim().length() > 0;
	}
}
