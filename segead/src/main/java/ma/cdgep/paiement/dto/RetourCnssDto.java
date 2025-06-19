package ma.cdgep.paiement.dto;

public class RetourCnssDto {

	private String messageType;
	private String status;
	private String message;
	private String refecnss;

	public RetourCnssDto() {
		super();
	}

	public RetourCnssDto(String messageType, String status, String message, String refecnss) {
		super();
		this.messageType = messageType;
		this.status = status;
		this.message = message;
		this.refecnss = refecnss;
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

	public String getRefecnss() {
		return refecnss;
	}

	public void setRefecnss(String refecnss) {
		this.refecnss = refecnss;
	}
	
	

}
