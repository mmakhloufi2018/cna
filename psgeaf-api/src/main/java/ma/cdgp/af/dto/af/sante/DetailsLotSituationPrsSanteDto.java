package ma.cdgp.af.dto.af.sante;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.entity.sante.DetailsLotSituationSante;
import ma.cdgp.af.entity.tgr.DetailsLotSituationTgr;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetailsLotSituationPrsSanteDto {
	@JsonProperty("idcs")
	private String idcs;

	@JsonProperty("typehandicap")
	private Integer typehandicap;

	@JsonProperty("degrehandicap")
	private Integer degrehandicap;

	@JsonProperty("besointiercepersonne")
	private Integer besointiercepersonne;

	@JsonProperty("traitementcouteux")
	private Integer traitementcouteux;

	@JsonProperty("traitementlobterme")
	private Integer traitementlobterme;

	@JsonProperty("besointechnologie")
	private Integer besointechnologie;

	@JsonProperty("dureehandicap")
	private Integer dureehandicap;

	@JsonProperty("medicalCommissionDate")
	private String medicalCommissionDate;

	public static DetailsLotSituationSante toEntity(DetailsLotSituationPrsSanteDto in) {
		if (in == null) {
			return null;
		}
		DetailsLotSituationSante out = new DetailsLotSituationSante();
		out.setIdcs(in.getIdcs());
		out.setBesointechnologie(in.getBesointechnologie());
		out.setBesointiercepersonne(in.getBesointiercepersonne());
		out.setDegrehandicap(in.getDegrehandicap());
		out.setTraitementcouteux(in.getTraitementcouteux());
		out.setTraitementlobterme(in.getTraitementlobterme());
		out.setTypehandicap(in.getTypehandicap());
		out.setDureehandicap(in.getDureehandicap());
		out.setMedicalCommissionDate(in.getMedicalCommissionDate());
		return out;
	}

}
