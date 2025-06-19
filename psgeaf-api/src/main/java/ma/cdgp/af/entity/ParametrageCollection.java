package ma.cdgp.af.entity;

import java.io.Serializable;
import java.sql.Clob;
//import java.util.List;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
//import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
//import javax.persistence.OneToMany;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PARAM_COLLECT_AF")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParametrageCollection implements Serializable {

	private static final long serialVersionUID = 1L;
	
 
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "PARTENAIRE")
	private String partenaire;
	
	@Column(name = "MAX_SIZE")
	private Long maxSize;
	
	@Column(name = "ACTIVE_COLLECT")
	private Boolean activeCollect;
	
	
	@Column(name = "ACTIVE_RETRY_KO")
	private Boolean activeRetryKo;

	@Column(name = "RATE_COLLECT")
	private Long rateCollect;


	@Column(name = "USE_RABBIT")
	private Boolean useRabbit;


	@Override
	public String toString() {
		return "ParametrageCollection [id=" + id + ", partenaire=" + partenaire + ", maxSize=" + maxSize
				+ ", nbrLotRetry=" + ", rateCollect=" + rateCollect + ", rateRetry="
				+ ", nbrRetryMax=" + ", activeCollect=" + activeCollect + ", activeRetryKo="
				+ activeRetryKo + "]";
	}


}
