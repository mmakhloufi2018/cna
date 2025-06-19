package ma.cdgep.paiement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface BeneficiareDto {
	
	public String getCin();
	public String getIdcs();
	public String getCodeResponse();
	public String getLibResponse();
	
	
	

}
