package ma.cdgp.af.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JwtToken {
	@JsonProperty("IdToken")
	private String idToken;
	@JsonProperty("AccessToken")
	private String accessToken;
	@JsonProperty("DateExpiration")
	private String dateExpiration;

	public String getIdToken() {
		return idToken;
	}

	public void setIdToken(String idToken) {
		this.idToken = idToken;
	}

	public JwtToken() {
		super();
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getDateExpiration() {
		return dateExpiration;
	}

	public void setDateExpiration(String dateExpiration) {
		this.dateExpiration = dateExpiration;
	}

	public JwtToken(String idToken, String accessToken, String dateExpiration) {
		super();
		this.idToken = idToken;
		this.accessToken = accessToken;
		this.dateExpiration = dateExpiration;
	}

}
