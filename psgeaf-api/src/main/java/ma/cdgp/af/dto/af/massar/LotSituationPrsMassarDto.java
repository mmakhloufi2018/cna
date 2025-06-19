package ma.cdgp.af.dto.af.massar;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.entity.massar.DemandeSituationMassar;
import ma.cdgp.af.entity.massar.LotSituationMassar;

import javax.validation.constraints.Size;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LotSituationPrsMassarDto {


	@JsonProperty("isNotes")
	private Boolean isNotes;
	@JsonProperty("codeEleves")
	private Set<String> codeEleves;
	@JsonProperty("Result")
	private Set<DetailsLotSituationPrsMassarDto> Result;
	@JsonProperty("IsError")
	private Boolean IsError;
	@JsonProperty("ErrorCode")
	private String ErrorCode;
	@JsonProperty("ErrorMessage")
	private String ErrorMessage;
	@JsonProperty("AnneeScolaire")
	private String anneeScolaire;


	public static LotSituationPrsMassarDto fromEntity(LotSituationMassar savedRR) {
		if (savedRR == null) {
			return null;
		}
		LotSituationPrsMassarDto out = new LotSituationPrsMassarDto();
		out.setIsNotes(savedRR.getIsNotes());

		if (savedRR.getDemandes() != null) {
			out.setCodeEleves(savedRR.getDemandes().stream()
					.map(DemandeSituationMassar::getNumMassar)
					.collect(Collectors.toSet()));

			DemandeSituationMassar firstDemande = savedRR.getDemandes().iterator().next();
			if (firstDemande != null && firstDemande.getAnneeScolaire() != null) {
				out.setAnneeScolaire(firstDemande.getAnneeScolaire());
			} else {
				out.setAnneeScolaire(String.valueOf(0));
			}
		} else {
			out.setCodeEleves(null);
			out.setAnneeScolaire(String.valueOf(0));
		}

		return out;
	}



	public static LotSituationMassar fromRetourDto(LotSituationPrsMassarDto response, LotSituationMassar savedRR) {
		if (savedRR == null || response == null) {
			return null;
		}
		savedRR.setCodeEleves(response.getResult() != null ? response.getResult().stream()
				.map(DetailsLotSituationPrsMassarDto::toEntity).collect(Collectors.toSet()) : null);
		return savedRR;
	}


	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
