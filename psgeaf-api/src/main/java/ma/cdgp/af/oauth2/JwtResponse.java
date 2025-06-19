package ma.cdgp.af.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JwtResponse {
	@JsonProperty("Statut")
	private Boolean statut;
	@JsonProperty("Message")
	private String message;
	@JsonProperty("Token")
	private JwtToken token;




	public JwtResponse(Boolean statut, String message) {
		super();
		this.statut = statut;
		this.message = message;
	}


	public JwtResponse(Boolean statut, String message, JwtToken token) {
		super();
		this.statut = statut;
		this.message = message;
		this.token = token;
	}


	public JwtResponse() {
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


	public JwtToken getToken() {
		return token;
	}

	public void setToken(JwtToken token) {
		this.token = token;
	}

}
