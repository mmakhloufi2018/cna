package ma.cdgp.af.oauth2;

import javax.net.ssl.X509TrustManager;

public class Dumb509TrustManager implements X509TrustManager {

	@Override
	public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {

	}

	@Override
	public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {

	}

	@Override
	public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		return null;
	}
}