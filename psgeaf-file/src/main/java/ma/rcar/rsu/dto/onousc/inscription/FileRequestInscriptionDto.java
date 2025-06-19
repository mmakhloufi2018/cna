package ma.rcar.rsu.dto.onousc.inscription;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;




/**
 * @author BAKHALED Ibrahim.
 *
 */



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileRequestInscriptionDto {
	private String fileName;
	private Date dateReception;
	private String partenaire;
	private String typeFile;
	private String dateFile;
	private String outboundFileName;
	private List<DetailsFileRequestInscriptionDto> details;

	public List<DetailsFileRequestInscriptionDto> getLignesRejet() {
		if (CollectionUtils.isEmpty(getDetails())) {
			return null;
		}
		return getDetails().stream().filter(l -> l.getEtat() != null && l.getEtat().equalsIgnoreCase("KO"))
				.collect(Collectors.toList());
	}
	
	public List<DetailsFileRequestInscriptionDto> getLignesValides() {
		if (CollectionUtils.isEmpty(getDetails())) {
			return null;
		}
		return getDetails().stream().filter(l -> l.getEtat() != null && l.getEtat().equalsIgnoreCase("OK"))
				.collect(Collectors.toList());
	}

}
