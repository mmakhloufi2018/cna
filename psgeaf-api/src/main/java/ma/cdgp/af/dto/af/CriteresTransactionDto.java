package ma.cdgp.af.dto.af;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CriteresTransactionDto {

	private String cnie;
	private String massar;
	private String cef;
	private Integer pageNumber;
	private Integer pageSize;
	
	 
}
