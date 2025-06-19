package ma.cdgp.af.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequestRsu {

    @JsonProperty("password")
    private String password;

    @JsonProperty("userName")
    private String userName;



    public String getPassword() {
        return password;
    }



    public void setPassword(String password) {
        this.password = password;
    }



    public String getUserName() {
        return userName;
    }



    public void setUserName(String userName) {
        this.userName = userName;
    }



    public LoginRequestRsu() {
        super();
    }
}
