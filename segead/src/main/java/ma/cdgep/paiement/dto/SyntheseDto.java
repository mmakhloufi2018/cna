package ma.cdgep.paiement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL) 
public class SyntheseDto {
	

	@JsonProperty("nomblots")
	private String nombreLots;
	@JsonProperty("nomblign")
	private String nombreLignes;
	@JsonProperty("monttota")
	private String montantTotal;
	
	@JsonProperty("messageType")
	private String messageType;
	@JsonProperty("status")
	private String status;
	@JsonProperty("message")
	private String message;

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public SyntheseDto(String nombreLots, String nombreLignes, String montantTotal) {
		super();
		this.nombreLots = nombreLots;
		this.nombreLignes = nombreLignes;
		this.montantTotal = montantTotal;
	}

	public SyntheseDto() {
		super();
	}

	public String getNombreLots() {
		return nombreLots;
	}

	public void setNombreLots(String nombreLots) {
		this.nombreLots = nombreLots;
	}

	public String getNombreLignes() {
		return nombreLignes;
	}

	public void setNombreLignes(String nombreLignes) {
		this.nombreLignes = nombreLignes;
	}

	public String getMontantTotal() {
		return montantTotal;
	}

	public void setMontantTotal(String montantTotal) {
		this.montantTotal = montantTotal;
	}


}
