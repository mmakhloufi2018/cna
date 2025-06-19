package ma.cdgp.af.dto.af.cnss;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.entity.cnss.DetailsLotSituationCnss;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetailsLotSituationPrsCnssDto {
	@JsonProperty("numpieid")
	private String cin;
	@JsonProperty("datenais")
	private String dateNaissance;
	@JsonProperty("nom_pren")
	private String nomPrenom;
	@JsonProperty("flagacti")
	private Integer flagActi;
	@JsonProperty("flacouaf")
	private Integer flacouaf;
	@JsonProperty("numeidcs")
	private String idcs;
	
	public static DetailsLotSituationCnss toEntity(DetailsLotSituationPrsCnssDto in) {
		if (in == null) {
			return null;
		}
		DetailsLotSituationCnss out = new DetailsLotSituationCnss();
		out.setCin(in.getCin());
		out.setDateNaissance(in.getDateNaissance());
		out.setNomPrenom(in.getNomPrenom());
		out.setFlacouaf(in.getFlacouaf());
		out.setFlagActi(in.getFlagActi());
		out.setIdcs(in.getIdcs());
		return out;
	}
 
}
