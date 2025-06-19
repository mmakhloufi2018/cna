package ma.cdgp.af.feign;

import static feign.Logger.Level.HEADERS;
import static feign.Util.UTF_8;
import static feign.Util.decodeOrDefault;

import java.io.IOException;

import feign.Logger;
import feign.Request;
import feign.Response;
import feign.Util;

public class CustomFeignRequestLogging extends Logger {

	@Override
	protected void logRequest(String configKey, Level logLevel, Request request) {

		if (logLevel.ordinal() >= HEADERS.ordinal()) {
			logRequestLogger(configKey, logLevel, request);
		} else {
			int bodyLength = 0;
			if (request.requestTemplate().body() != null) {
				bodyLength = request.requestTemplate().body().length;
			}
			log(configKey, "---> %s %s HTTP/1.1 (%s-byte body) ", request.httpMethod().name(), request.url(),
					bodyLength);
		}
	}

	private void logRequestLogger(String configKey, Level logLevel, Request request) {
		log(configKey, "REQUEST NAME & URL =========>>> %s %s ", request.httpMethod().name(), request.url());
		if (logLevel.ordinal() >= Level.HEADERS.ordinal()) {

			for (String field : request.headers().keySet()) {
				log(configKey, "%s: %s", field, request.headers().get(field));
			}

			int bodyLength = 0;
			if (request.requestTemplate().body() != null) {
				bodyLength = request.requestTemplate().body().length;
				if (logLevel.ordinal() >= Level.FULL.ordinal()) {
					String bodyText = request.charset() != null
							? new String(request.requestTemplate().body(), request.charset())
							: null;
					log(configKey, ""); // CRLF
					log(configKey, "%s", bodyText != null ? bodyText : "Binary data");
				}
			}
			log(configKey, "====== >>>>  END REQUEST HTTP (%s-byte body)", bodyLength);
		}
	}

	@Override
	protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime)
			throws IOException {
		if (logLevel.ordinal() >= HEADERS.ordinal()) {
			return logAndRebufferResponseLogger(configKey, logLevel, response, elapsedTime);
		} else {
			int status = response.status();
			Request request = response.request();
			log(configKey, "RESPONSE ====== >>>>  %s %s HTTP/1.1 %s (%sms) ", request.httpMethod().name(),
					request.url(), status, elapsedTime);
		}
		return response;
	}

	private Response logAndRebufferResponseLogger(String configKey, Level logLevel, Response response, long elapsedTime)
			throws IOException {
		String reason = response.reason() != null && logLevel.compareTo(Level.NONE) > 0 ? " " + response.reason() : "";
		int status = response.status();
		log(configKey, "RESPONSE STATUS & TIME ====== >>>> %s%s (%sms)", status, reason, elapsedTime);
		if (logLevel.ordinal() >= Level.HEADERS.ordinal()) {

//			for (String field : response.headers().keySet()) {
//				for (String value : valuesOrEmpty(response.headers(), field)) {
//					log(configKey, "%s: %s", field, value);
//				}
//			}

			int bodyLength = 0;
			if (response.body() != null && !(status == 204 || status == 205)) {
				// HTTP 204 No Content "...response MUST NOT include a message-body"
				// HTTP 205 Reset Content "...response MUST NOT include an entity"
				if (logLevel.ordinal() >= Level.FULL.ordinal()) {
					log(configKey, ""); // CRLF
				}
				byte[] bodyData = Util.toByteArray(response.body().asInputStream());
				bodyLength = bodyData.length;
				if (logLevel.ordinal() >= Level.FULL.ordinal() && bodyLength > 0) {
					log(configKey, "%s", decodeOrDefault(bodyData, UTF_8, "Binary data"));
				}
				log(configKey, " END RESPONSE ======>>> (%s-byte body)", bodyLength);
				return response.toBuilder().body(bodyData).build();
			} else {
				log(configKey, "END RESPONSE ======>>> (%s-byte body)", bodyLength);
			}
		}
		return response;
	}

	@Override
	protected void log(String configKey, String format, Object... args) {
		System.err.printf(methodTag(configKey) + format + "%n", args);
	}

	protected String format(String configKey, String format, Object... args) {
		return String.format(methodTag(configKey) + format, args);
	}
}