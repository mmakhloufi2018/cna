package ma.cdgp.af.dto.af;

public class ResponseMassarDto {

	private Boolean isError;
	private String errorCode;
	private String errorMessage;
	private DataMassarDto result;
	public Boolean getIsError() {
		return isError;
	}
	public void setIsError(Boolean isError) {
		this.isError = isError;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public DataMassarDto getResult() {
		return result;
	}
	public void setResult(DataMassarDto result) {
		this.result = result;
	}
	public ResponseMassarDto() {
		super();
	}
	
	
}
