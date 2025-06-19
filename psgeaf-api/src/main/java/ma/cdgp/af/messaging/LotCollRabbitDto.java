package ma.cdgp.af.messaging;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.dto.af.CandidatInfos;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LotCollRabbitDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<CandidatInfos> subs;
	private String reference;

}
