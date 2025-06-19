package ma.cdgp.af.oauth2;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import org.springframework.http.client.SimpleClientHttpRequestFactory;

public class SSLContextRequestFactory extends SimpleClientHttpRequestFactory {

	private final SSLContext sslContext;

	public SSLContextRequestFactory(SSLContext sslContext) {
		this.sslContext = sslContext;
	}

	@Override
	protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
		if (connection instanceof HttpsURLConnection) {
			((HttpsURLConnection) connection).setSSLSocketFactory(sslContext.getSocketFactory());
		}
		super.prepareConnection(connection, httpMethod);
	}
}