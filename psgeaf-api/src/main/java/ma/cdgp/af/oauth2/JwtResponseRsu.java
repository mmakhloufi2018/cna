package ma.cdgp.af.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JwtResponseRsu {

    @JsonProperty("statut")
    private Boolean statut;
    @JsonProperty("message")
    private String message;
    @JsonProperty("token")
    private JwtTokenRsu token;




    public JwtResponseRsu(Boolean statut, String message) {
        super();
        this.statut = statut;
        this.message = message;
    }


    public JwtResponseRsu(Boolean statut, String message, JwtTokenRsu token) {
        super();
        this.statut = statut;
        this.message = message;
        this.token = token;
    }


    public JwtResponseRsu() {
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


    public JwtTokenRsu getToken() {
        return token;
    }

    public void setToken(JwtTokenRsu token) {
        this.token = token;
    }
}
