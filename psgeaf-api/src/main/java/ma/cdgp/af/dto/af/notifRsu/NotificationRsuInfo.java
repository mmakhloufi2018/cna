package ma.cdgp.af.dto.af.notifRsu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.utils.Col;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRsuInfo {

	@Col(name = "CODE_MENAGE_RSU")
	private String codeMEnage;
	
	@Col(name = "REFERENCE")
	private String reference;
	
	
	@Col(name = "TYPE_PRESTATION")
	private String typePrestation;
	
	
	@Col(name = "ID")
	private String id;
	
	@Col(name = "DATE_EFFET")
	private String dateEffet;
	
	@Col(name = "DATE_FIN")
	private String dateFin;
	
	@Col(name = "IDCS")
	private String idcs;
	
	@Col(name = "DATE_NAISSANCE")
	private String dateNaissance;
	@Col(name = "DATE_NAISSANCE_CHAR")
	private String dateNaissanceChar;
	@Col(name = "GENRE")
	private String genre;
	
	@Col(name = "MONTANT")
	private String montant;
	
	@Col(name = "MOTIF")
	private String motif;
	
	@Col(name = "ACTIF")
	private String actif;
	
}
