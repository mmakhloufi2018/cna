package ma.cdgp.af.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private JwtUtils jwtUtils;

	@Value("${rsu.cle.pass}")
	private String clePasswordCdg;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String name = authentication.getName();
		String password = authentication.getCredentials().toString();
		String pass = this.jwtUtils.getPassword(clePasswordCdg);
		CustomAuthenticationToken auth = new CustomAuthenticationToken(name, password, null);
		System.err.println(pass);
		if (pass.equals(password)) {
			auth.setAuthenticated(true);
		} else {
			auth.setAuthenticated(false);
		}
		return auth;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(CustomAuthenticationToken.class);
	}
}