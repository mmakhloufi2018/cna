package ma.cdgp.af.utils;

import org.springframework.context.ApplicationEvent;

public class AlEvent extends ApplicationEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String partenaire;
	private String event;
	private Object data;
	
    public AlEvent(Object data, String partenaire, String event) {
        super(data);
        this.data = data;
        this.partenaire = partenaire;
        this.event = event;
    }

	public String getPartenaire() {
		return partenaire;
	}

	public void setPartenaire(String partenaire) {
		this.partenaire = partenaire;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "AlEvent{partenaire='" + partenaire + "', event='" + event + "'}";
	}

}