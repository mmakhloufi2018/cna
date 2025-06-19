package ma.cdgp.af.messaging;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.dto.af.tgr.LotNotifTgrDto;
import ma.cdgp.af.dto.af.tgr.LotNotificationTgrDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LotNotifTGRRabbitDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LotNotificationTgrDto lot;
	private LotNotifTgrDto lotNotifTgr;

}
