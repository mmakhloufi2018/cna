package ma.cdgp.af.dto.af;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.utils.Col;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DecesInfos {

	@Col(name = "IDT")
	private String id;
	
	@Col(name = "CNIE")
	private String cnie;
	
	@Col(name = "DATE_NAISSANCE_STR")
	private String dateDeces;

	@Col(name = "ETAT_CIVIL")
	private String etatCivil;

	@Col(name = "DATE_NAISSANCE_STR")
	private String dateNaissance;

	@Col(name = "NOM_FR")
	private String nom;

	@Col(name = "PRENOM_FR")
	private String prenom;

	 
}
