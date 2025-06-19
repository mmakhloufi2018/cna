package ma.cdgp.af.utils;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class AlEventPublisher {
	private final ApplicationEventPublisher eventPublisher;

	public AlEventPublisher(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void stopService(Object data, String partenaire) {
		AlEvent event = new AlEvent(data != null ? data : "N.A", partenaire, EventEnum.STOP.name());
		eventPublisher.publishEvent(event);
	}
	public void stopServiceRetry(Object data, String partenaire) {
		AlEvent event = new AlEvent(data != null ? data : "N.A", partenaire, EventEnum.STOPRETRY.name());
		eventPublisher.publishEvent(event);
	}
}
