package ma.cdgp.af.dto.af.cnss;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AcquittementSituationCnssDto  {
	@JsonProperty("dateLot")
	private String dateLot;
	@JsonProperty("referenceLot")
	private String referenceLot;
	@JsonProperty("status")
	private String status;
	@JsonProperty("message")
	private String message;
	@JsonProperty("messageType")
	private String messageType;
	public Boolean isAcquittement() {
		return true;
	}
	
	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
