package ma.cdgep.service;

public class ReponseAlimentation {
	
	String codeReponse ;
	String message;
	
	
	public ReponseAlimentation(String codeReponse, String message) {
		super();
		this.codeReponse = codeReponse;
		this.message = message;
	}
	public String getCodeReponse() {
		return codeReponse;
	}
	public String getMessage() {
		return message;
	}
	

}
