package ma.cdgp.af.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.MessageBody;

@Service
public class MailTools {
	@Value("${mail.cdg.login}")
	private String login;
	@Value("${mail.cdg.password}")
	private String password;
	@Value("${mail.cdg.domaine}")
	private String domaine;
	@Value("${mail.cdg.service}")
	private String service;
	@Value("${mail.cdg.to}")
	private String to;
	@Value("${mail.cdg.cc}")
	private String cc;

	private ExchangeService connect() throws URISyntaxException {

		ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
		ExchangeCredentials credentials = new WebCredentials(login, password, domaine);
		service.setCredentials(credentials);
		service.setUrl(new URI("https://" + service + "/ews/exchange.asmx"));
		return service;

	}

	private void disconnect(ExchangeService service) {
		service.close();
	}

	public void sendMail(String subject, String message) throws URISyntaxException {

		ExchangeService service = connect();

		try {
			EmailMessage msg = new EmailMessage(service);

			msg.setSubject(subject);

			MessageBody body = new MessageBody(BodyType.HTML, message);

			msg.setBody(body);

			Arrays.asList(to.split(";")).forEach((to) -> {

				try {
					msg.getToRecipients().add(to);

				} catch (Exception e) {
					e.printStackTrace();

				}

			});

			if (StringUtils.hasText(cc)) {

				Arrays.asList(cc.split(";")).forEach((cc) -> {
					try {
						msg.getCcRecipients().add(cc);
					} catch (Exception e) {

						e.printStackTrace();

					}

				});

			}

			msg.sendAndSaveCopy();

		} catch (Exception e) {

			e.printStackTrace();

		}
		service.close();

	}

	private String escape(String s) {

		StringBuilder builder = new StringBuilder();

		boolean previousWasASpace = false;

		for (char c : s.toCharArray()) {

			if (c == ' ') {

				if (previousWasASpace) {

					builder.append("&nbsp;");

					previousWasASpace = false;

					continue;

				}

				previousWasASpace = true;

			} else {

				previousWasASpace = false;

			}

			switch (c) {

			case '<':

				builder.append("&lt;");

				break;

			case '>':

				builder.append("&gt;");

				break;

			case '&':

				builder.append("&amp;");

				break;

			case '"':

				builder.append("&quot;");

				break;

			case '\n':

				builder.append("<br>");

				break;

			// We need Tab support here, because we print StackTraces as HTML

			case '\t':

				builder.append("&nbsp; &nbsp; &nbsp;");

				break;

			default:

				if (c < 128) {

					builder.append(c);

				} else {

					builder.append("&#").append((int) c).append(";");

				}

			}

		}

		return builder.toString();

	}

}