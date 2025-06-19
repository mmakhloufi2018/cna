package ma.cdgp.af.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JwtTokenRsuResponse {
	@JsonProperty("idToken")
	private String idToken;
	@JsonProperty("accessToken")
	private String accessToken;
	@JsonProperty("DateExpiration")
	private String dateExpiration;

	public String getIdToken() {
		return idToken;
	}

	public void setIdToken(String idToken) {
		this.idToken = idToken;
	}

	public JwtTokenRsuResponse() {
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

	public JwtTokenRsuResponse(String idToken, String accessToken, String dateExpiration) {
		super();
		this.idToken = idToken;
		this.accessToken = accessToken;
		this.dateExpiration = dateExpiration;
	}

}
