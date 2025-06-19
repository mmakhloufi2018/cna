package ma.cdgp.af.dto.af.cmr;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import ma.cdgp.af.entity.cmr.DemandeNotifCmr;
import ma.cdgp.af.entity.cmr.LotNotifCmr;
import ma.cdgp.af.utils.Utils;


import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LotNotificationCmrDto {

    @JsonProperty("dateCreation")
    private String dateCreation;
    @JsonProperty("IdLot")
    private String idLot;
    @JsonProperty("listBeneficiaire")
    private Set<BenefNotificationCmrDto> listBeneficiaire;








    public static LotNotifCmr fromDto(LotNotificationCmrDto in) {
        if (in == null) {
            return null;
        }
        LotNotifCmr out = new LotNotifCmr();
        out.setDateCreation(Utils.stringToDate(in.getDateCreation()));
        out.setIdLot(Long.valueOf(in.getIdLot()));
        out.setDemandes(in.getListBeneficiaire() != null ? in.getListBeneficiaire().stream().map(t -> {
            DemandeNotifCmr d = new DemandeNotifCmr();
            d.setStatut(t.getStatut());
            d.setCin(t.getCin());
            d.setIdcs(t.getIdcs());
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

















