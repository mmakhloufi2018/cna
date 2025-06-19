package ma.cdgp.af.dto.af.tgr;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BenefNotificationTgrDto {

	@JsonProperty("id")
    private String id;
    
    @JsonProperty("CIN")
    private String CIN;

    @JsonProperty("statut")
    private String statut;
}
