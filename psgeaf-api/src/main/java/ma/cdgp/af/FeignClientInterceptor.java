package ma.cdgp.af;

import java.nio.charset.Charset;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * @author joreich
 *
 */
@Component
public class FeignClientInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate requestTemplate) {
		Map<String, String> mapSecret;
		try {
//			mapSecret = GateWaySecurity.getSecret();
//			requestTemplate.header("GATEWAY-SEC", mapSecret.get("SECRET"));
//			requestTemplate.header("GATEWAY-CIPH", mapSecret.get("CIPHER"));
//			requestTemplate.header(HttpHeaders.AUTHORIZATION, "Basic MzBhZ2lseXMwMXYyOmghYSZ7Mzk0Xj0re1JAcGg=");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
