package ma.cdgp.af.dto.af.tgr;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.entity.tgr.DemandeNotifTgr;
import ma.cdgp.af.entity.tgr.LotNotifTgr;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LotNotificationTgrDto {


    @JsonProperty("dateLot")
    private String dateLot;
    @JsonProperty("idLot")
    private Long idLot;
    @JsonProperty("listeBeneficiaires")
    private Set<BenefNotificationTgrDto> listeBeneficiaires;

    public static LotNotifTgr fromDto(LotNotificationTgrDto in) {
        if (in == null) {
            return null;
        }
        LotNotifTgr out = new LotNotifTgr();
        out.setDateLot(in.getDateLot());
        out.setIdLot(in.getIdLot());
        out.setDemandes(in.getListeBeneficiaires() != null ? in.getListeBeneficiaires().stream().map(t -> {
            DemandeNotifTgr d = new DemandeNotifTgr();
            d.setStatut(Boolean.valueOf(t.getStatut()));
            d.setCin(t.getCIN());
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
