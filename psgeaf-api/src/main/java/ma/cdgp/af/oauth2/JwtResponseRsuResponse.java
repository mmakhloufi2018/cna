package ma.cdgp.af.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JwtResponseRsuResponse {
	@JsonProperty("statut")
	private Boolean statut;
	@JsonProperty("message")
	private String message;
	@JsonProperty("token")
    private JwtTokenRsuResponse token;




	public JwtResponseRsuResponse(Boolean statut, String message) {
		super();
		this.statut = statut;
		this.message = message;
	}


	public JwtResponseRsuResponse(Boolean statut, String message, JwtTokenRsuResponse token) {
		super();
		this.statut = statut;
		this.message = message;
		this.token = token;
	}


	public JwtResponseRsuResponse() {
		super();
	}

	public Boolean getStatut() {
		return statut;
	}

	public void setStatut(Boolean statut) {
		this.statut = statut;
	}

 

	 
	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public JwtTokenRsuResponse getToken() {
		return token;
	}

	public void setToken(JwtTokenRsuResponse token) {
		this.token = token;
	}



}
