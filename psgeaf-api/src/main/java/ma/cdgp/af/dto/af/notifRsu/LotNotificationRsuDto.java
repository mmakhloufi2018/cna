package ma.cdgp.af.dto.af.notifRsu;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.entity.notifRsu.DemandeNotificationRsu;
import ma.cdgp.af.entity.notifRsu.LotNotifRsu;


import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class LotNotificationRsuDto {
	@JsonProperty("idTransaction")
	private Long idTransaction;
	@JsonProperty("listeBeneficiaires")
	private Set<BenefNotificationRsu> listeBeneficiaires;

	public static LotNotifRsu fromDto(LotNotificationRsuDto in) {
		if (in == null) {
			return null;
		}
		LotNotifRsu out = new LotNotifRsu();
		out.setIdTransaction(in.getIdTransaction().toString());
		out.setDemandes(in.getListeBeneficiaires() != null ? in.getListeBeneficiaires().stream().map(t -> {
			DemandeNotificationRsu d = new DemandeNotificationRsu();
			d.setActif(t.getActif());
			d.setBeneficiaireId(t.getBeneficiaireId());
			d.setDateDebut(t.getDateDebut());
			d.setDateFin(t.getDateFin());
			d.setDateNaissance(t.getDateNaissance());
			d.setGenre(t.getGenre());
			d.setIdcs(t.getIdcs());
			d.setMontant(t.getMontant());
			d.setMotif(t.getMotif());
			return d;
		}).collect(Collectors.toSet()) : null);
		return out;
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
