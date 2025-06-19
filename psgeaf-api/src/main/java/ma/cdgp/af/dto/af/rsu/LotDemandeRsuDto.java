package ma.cdgp.af.dto.af.rsu;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.cdgp.af.entity.rsu.CollectedDemandesRsu;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LotDemandeRsuDto {

    @JsonProperty("idTransaction")
    private Long idTransaction;

    @JsonProperty("demandes")
    private Set<DemandeRsuDto> demandes;




    public static LotDemandeRsuDto convertToDto(List<CollectedDemandesRsu> entities) {
        LotDemandeRsuDto tmpLot = new LotDemandeRsuDto();
        tmpLot.setIdTransaction(Long.valueOf(Calendar.getInstance().getTimeInMillis() + ""));
        Set<DemandeRsuDto> dtoSet = entities.stream().map(entity -> new DemandeRsuDto(
                String.valueOf(entity.getIdcs()),
                new SimpleDateFormat("dd/MM/yyyy").format(entity.getDateNaissance()),
                entity.getChefMenage(),
                entity.getGenre())
        ).collect(Collectors.toSet());
        tmpLot.setDemandes(dtoSet);
        return tmpLot;
    }
}
