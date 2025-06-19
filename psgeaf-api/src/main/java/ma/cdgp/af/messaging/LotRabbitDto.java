package ma.cdgp.af.messaging;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LotRabbitDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String idLot;
	private String type;

}
