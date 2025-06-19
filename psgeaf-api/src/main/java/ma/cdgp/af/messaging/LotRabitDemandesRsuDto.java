package ma.cdgp.af.messaging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.dto.af.rsu.DemandeRsuDto;
import ma.cdgp.af.dto.af.rsu.LotDemandeRsuDto;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LotRabitDemandesRsuDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private String idLot;
    private LotDemandeRsuDto lotDemandeRsuDto;
    private String type;
}
