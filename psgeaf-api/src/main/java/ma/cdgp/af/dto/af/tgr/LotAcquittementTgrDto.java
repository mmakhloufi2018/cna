package ma.cdgp.af.dto.af.tgr;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LotAcquittementTgrDto {

    @JsonProperty("dateLot")
    private String dateLot;

    @JsonProperty("idLot")
    private Long idLot;

    @JsonProperty("codeMotif")
    private String codeMotif;

    @JsonProperty("libeMotif")
    private String libeMotif;

    @JsonProperty("nombreEnregistrement")
    private String nombreEnregistrement;

    @JsonProperty("beneficiareNonEnregistrer")
    private Set<AcquittementBenefNotificationTgrDto> beneficiareNonEnregistrer;
}


















//        {
//        "dateLot": "01122023",
//        "idLot": 12345689,
//        "codeMotif": "0000",
//        "libeMotif": "SUCCES",
//        "nombreEnregistrement": "2",
//        "beneficiareNonEnregistrer": []
//        }
//
//
//
//        {
//        "dateLot": "01122023",
//        "idLot": 12345689,
//        "codeMotif": "0000",
//        "libeMotif": "SUCCES",
//        "nombreEnregistrement": "1",
//        "beneficiareNonEnregistrer": [
//        {
//        "cin": "FC18799",
//        "codeMotif": "0007",
//        "libeMotif": "CODE STATUT NON RECONNU PAR LE SYSTEME"
//        }
//        ]
//        }