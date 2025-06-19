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
public class CandidatInfos {

	@Col(name = "ID")
	private String id;
	
	@Col(name = "IDCS")
	private String idcs;

	@Col(name = "CIN")
	private String cin;

	@Col(name = "DATE_NAISSANCE")
	private String dateNaissance;

	@Col(name = "GENRE")
	private String genre;

	@Col(name = "MOIS_ANNEE")
	private String moisAnnee;

	@Col(name = "MASSAR")
	private String massar;

	@Col(name = "CEF")
	private String cef;

	@Col(name = "TYPE")
	private String type;

	@Col(name = "ACTIVE")
	private String active;
	
	
	@Col(name = "REQUETE_SC")
	private String requeteSc;
	
	@Col(name = "REQUETE_SP")
	private String requeteSp;
	
	
	@Col(name = "REQUETE_RSU")
	private String requeteRsu;
	
	
	@Col(name = "REQUETE_BR")
	private String requeteBr;
	
	@Col(name = "REQUETE_HA")
	private String requeteHa;
	
	
	@Col(name = "REQUETE_FEF")
	private String requeteFef;
	
	@Col(name = "ID_PERSON")
	private String idPerson;
}
