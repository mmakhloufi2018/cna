package ma.cdgp.af;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.utils.Col;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ControleInfos {

	@Col(name = "ID")
	private String id;

	@Col(name = "CODE")
	private String code;

	@Col(name = "LIBELLE")
	private String libelle;

	@Col(name = "TYPE_CHECK")
	private String typeCheck;

	@Col(name = "PARTENAIRE")
	private String partenaire;
 
}
