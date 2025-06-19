package ma.cdgp.af.dto.af.tgr;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.entity.tgr.LotNotifTgr;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LotNotifTgrDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String partenaire;

	private Date dateCreation;

	private String etatLot;

	private Long idLot;
	
	
	public static LotNotifTgr from (LotNotifTgrDto dto) {
		LotNotifTgr out = new LotNotifTgr();
		out.setId(dto.getId());
		out.setPartenaire(dto.getPartenaire());
		out.setDateCreation(dto.getDateCreation());
		out.setEtatLot(dto.getEtatLot());
		out.setIdLot(dto.getIdLot());
		
		return out;
		
	}
	
	
	public static LotNotifTgrDto to (LotNotifTgr entity) {
		LotNotifTgrDto out = new LotNotifTgrDto();
		out.setId(entity.getId());
		out.setPartenaire(entity.getPartenaire());
		out.setDateCreation(entity.getDateCreation());
		out.setEtatLot(entity.getEtatLot());
		out.setIdLot(entity.getIdLot());
		
		return out;
		
	}
}
