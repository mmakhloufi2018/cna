package ma.cdgp.af.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JwtTokenRsu {

    @JsonProperty("idToken")
    private String idToken;
    @JsonProperty("accessToken")
    private String accessToken;
    @JsonProperty("dateExpiration")
    private String dateExpiration;

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public JwtTokenRsu() {
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

    public JwtTokenRsu(String idToken, String accessToken, String dateExpiration) {
        super();
        this.idToken = idToken;
        this.accessToken = accessToken;
        this.dateExpiration = dateExpiration;
    }

}
