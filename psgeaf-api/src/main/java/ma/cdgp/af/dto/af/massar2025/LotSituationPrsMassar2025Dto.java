package ma.cdgp.af.dto.af.massar2025;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import ma.cdgp.af.entity.massar2025.DemandeSituationMassar2025;
import ma.cdgp.af.entity.massar2025.LotSituationMassar2025;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class LotSituationPrsMassar2025Dto {


	@JsonProperty("isNotes")
	private Boolean isNotes;
	@JsonProperty("codeEleves")
	private Set<String> codeEleves;
	@JsonProperty("Result")
	private Set<DetailsLotSituationPrsMassar2025Dto> Result;
	@JsonProperty("IsError")
	private Boolean IsError;
	@JsonProperty("ErrorCode")
	private String ErrorCode;
	@JsonProperty("ErrorMessage")
	private String ErrorMessage;
	@JsonProperty("AnneeScolaire")
	private String anneeScolaire;


	public static LotSituationPrsMassar2025Dto fromEntity(LotSituationMassar2025 savedRR) {
		if (savedRR == null) {
			return null;
		}
		LotSituationPrsMassar2025Dto out = new LotSituationPrsMassar2025Dto();
		out.setIsNotes(savedRR.getIsNotes());

		if (savedRR.getDemandes() != null) {
			out.setCodeEleves(savedRR.getDemandes().stream()
					.map(DemandeSituationMassar2025::getNumMassar)
					.collect(Collectors.toSet()));

			DemandeSituationMassar2025 firstDemande = savedRR.getDemandes().iterator().next();
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



	public static LotSituationMassar2025 fromRetourDto(LotSituationPrsMassar2025Dto response, LotSituationMassar2025 savedRR) {
		if (savedRR == null || response == null) {
			return null;
		}
		savedRR.setCodeEleves(response.getResult() != null ? response.getResult().stream()
				.map(DetailsLotSituationPrsMassar2025Dto::toEntity).collect(Collectors.toSet()) : null);
		return savedRR;
	}


	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
