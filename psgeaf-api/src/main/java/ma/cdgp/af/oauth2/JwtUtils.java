package ma.cdgp.af.oauth2;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.common.hash.Hashing;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	@Value("${rsu.jwt.app.jwtSecret}")
	private String jwtSecret;

	@Value("${rsu.jwt.app.jwtExpirationMs}")
	private int jwtExpirationMs;

	@Value("${rsu.sec.clientId}")
	private String clientId;

	@Value("${rsu.sec.clientSecret}")
	private String clientSecret;

	@Value("${rsu.sec.grantType}")
	private String grantType;

	@Value("${rsu.sec.acessTokenUri}")
	private String acessTokenUri;

	@Value("${rsu.sec.scope}")
	private String scope;

	@Value("${rsu.client.id}")
	private String clientIdRsu;

	@Value("${rsu.client.secret}")
	private String clientSecretRsu;

	public List<String> generateJwtToken(Authentication authentication) {
		String userPrincipal = (String) authentication.getPrincipal();

		String idToken = UUID.randomUUID().toString();
		Date expireAt = new Date((new Date()).getTime() + jwtExpirationMs);
		String token = Jwts.builder().setSubject((userPrincipal)).setIssuedAt(new Date()).setExpiration(expireAt)
				.signWith(SignatureAlgorithm.HS512, jwtSecret).setId(idToken).compact();
		return Arrays.asList(idToken, token, expireAt.toGMTString());
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (io.jsonwebtoken.SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}

	public String getPassword(String cle) {
//		String cle = "J@NcRfU)jXn2r5u";
		StringBuffer sb = new StringBuffer();
		sb.append(Calendar.getInstance().get(Calendar.YEAR));
		int month = Calendar.getInstance().get(Calendar.MONTH);
		if (String.valueOf((month + 1)).length() == 1) {
			sb.append("0" + (month + 1));
		} else
			sb.append("" + (month + 1));

		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		if (String.valueOf(day).length() == 1) {
			sb.append("0" + String.valueOf(day));
		} else
			sb.append(String.valueOf(day));

//		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
//		if (String.valueOf(hour).length() == 1) {
//			sb.append("0" + String.valueOf(hour));
//		} else
//			sb.append(String.valueOf(hour));

		String pass = sb.toString() + "" + cle;
		System.err.println(pass);
		String sha256hex = Hashing.sha256().hashString(pass, StandardCharsets.UTF_8).toString();
		return sha256hex;
	}
	public String getPasswordPlusHour(String cle) {
//		String cle = "J@NcRfU)jXn2r5u";
		StringBuffer sb = new StringBuffer();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -1);
		sb.append(cal.get(Calendar.YEAR));
		int month = cal.get(Calendar.MONTH);
		if (String.valueOf((month + 1)).length() == 1) {
			sb.append("0" + (month + 1));
		} else
			sb.append("" + (month + 1));

		int day = cal.get(Calendar.DAY_OF_MONTH);
		if (String.valueOf(day).length() == 1) {
			sb.append("0" + String.valueOf(day));
		} else
			sb.append(String.valueOf(day));

//		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
//		if (String.valueOf(hour).length() == 1) {
//			sb.append("0" + String.valueOf(hour));
//		} else
//			sb.append(String.valueOf(hour));

		String pass = sb.toString() + "" + cle;
		System.err.println(pass);
		String sha256hex = Hashing.sha256().hashString(pass, StandardCharsets.UTF_8).toString();
		return sha256hex;
	}
	public String getTokenRSU() {
		// Cliend id and client secret
		RestTemplate restTemplate = new RestTemplate();
		org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
		headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
		HttpEntity<String> request = new HttpEntity<String>(headers);
		String access_token_url = acessTokenUri;
		access_token_url += "?client_id=" + clientId + "&grant_type=" + grantType + "&client_secret=" + clientSecret;
		access_token_url += "&scope=" + scope;
		ResponseEntity<String> response = restTemplate.exchange(access_token_url, HttpMethod.POST, request,
				String.class);
		System.out.println("Access Token Response ---------" + response.getBody());
		return null;
	}

	public String tokenAcess() throws Exception {
		final ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();
		resourceDetails.setClientId(clientId);
		resourceDetails.setClientSecret(clientSecret);
		resourceDetails.setGrantType(grantType);
		resourceDetails.setAccessTokenUri(acessTokenUri);
		resourceDetails.setScope(Arrays.asList(scope));
//		resourceDetails.setAuthenticationScheme(AuthenticationScheme.form);
		disableSslVerification();
		final OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(resourceDetails);
		final OAuth2AccessToken accessToken = oAuth2RestTemplate.getAccessToken();
		final String accessTokenAsString = accessToken.getValue();
		return accessTokenAsString;
	}

	public String generateUniqueId() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		UUID uuid = UUID.randomUUID();
		MessageDigest salt = MessageDigest.getInstance("SHA-256");
		salt.update(UUID.randomUUID().toString().getBytes("UTF-8"));
		String digest = bytesToHex(salt.digest());
		return digest;
	}

	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

	public String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}
		return new String(hexChars);
	}

	private static void disableCertificateChecks(OAuth2RestTemplate oauthTemplate) throws Exception {

		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, new TrustManager[] { new Dumb509TrustManager() }, null);
		ClientHttpRequestFactory requestFactory = new SSLContextRequestFactory(sslContext);

		// This is for OAuth protected resources
		oauthTemplate.setRequestFactory(requestFactory);

		// AuthorizationCodeAccessTokenProvider creates it's own RestTemplate for token
		// operations
//		AuthorizationCodeAccessTokenProvider provider = new AuthorizationCodeAccessTokenProvider();
//		provider.setRequestFactory(requestFactory);
//		oauthTemplate.setAccessTokenProvider(provider);
	}

	public static void disableSslVerification() {
		try {
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
	}


	public String tokenRsuAcess() throws Exception {
		final ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();
		resourceDetails.setClientId(clientIdRsu);
		resourceDetails.setClientSecret(clientSecretRsu);
		resourceDetails.setGrantType("client_credentials");
		resourceDetails.setAccessTokenUri("https://10.2.10.23/token");
		resourceDetails.setScope(Arrays.asList("rsu_social_programs_subscriber"));
		resourceDetails.setAuthenticationScheme(AuthenticationScheme.form);
		disableSslVerification();
		final OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(resourceDetails);
		final OAuth2AccessToken accessToken = oAuth2RestTemplate.getAccessToken();
		final String accessTokenAsString = accessToken.getValue();
		return accessTokenAsString;
	}

}
