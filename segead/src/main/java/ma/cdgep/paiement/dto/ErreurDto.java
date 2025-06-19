package ma.cdgep.paiement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErreurDto {

	@JsonProperty("messageType")
	private String messageType;
	@JsonProperty("status")
	private String status;
	@JsonProperty("message")
	private String message;
	
	

	public ErreurDto(String messageType, String status, String message) {
		super();
		this.messageType = messageType;
		this.status = status;
		this.message = message;
	}

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

}
