package ma.cdgp.af.dto.af.rsu;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AquittementDemandeRsuDto {

    @JsonProperty("idTransaction")
    private String idTransaction;

    @JsonProperty("demandes")
    private Set<DetailsDemandeRsuDto> demandes;

}

