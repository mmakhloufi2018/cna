package ma.cdgp.af.oauth2;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class CustomAuthenticationToken extends AbstractAuthenticationToken {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Object principal;
	private String credential;

	public CustomAuthenticationToken(Object dbUser, String password,
			Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = dbUser;
		this.credential = password;
	}

	@Override
	public Object getCredentials() {
		return credential;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}
}