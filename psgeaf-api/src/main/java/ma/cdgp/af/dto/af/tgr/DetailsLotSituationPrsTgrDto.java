package ma.cdgp.af.dto.af.tgr;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.entity.tgr.DetailsLotSituationTgr;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class DetailsLotSituationPrsTgrDto {
	@JsonProperty("CIN")
	private String cin;
	@JsonProperty("cin")
	private String cinRepose;
	@JsonProperty("situation")
	private String situation;
	@JsonProperty("couvertureAF")
	private String couvertureAf;
	@JsonProperty("dateNaissance")
	private String dateNaissance;
	@JsonProperty("nomPrenom")
	private String nomPrenom;
 
	public static DetailsLotSituationTgr toEntity(DetailsLotSituationPrsTgrDto in) {
		if (in == null) {
			return null;
		}
		DetailsLotSituationTgr out = new DetailsLotSituationTgr();
		out.setCin(in.getCinRepose());
		out.setCouvertureAf(in.getCouvertureAf());
		out.setDateNaissance(in.getDateNaissance());
		out.setSituation(in.getSituation());
		out.setNomPrenom(in.getNomPrenom());
		return out;
	}
 
}
