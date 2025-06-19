package ma.cdgp.af.dto.af;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BatchData {

	private Integer id;
	private String name;
	private String partenaire;
	private Date dateLancement;
	private Boolean isRunning;
	private int nbr;
	private String dateReponse;
	private Long rate;
	private String motif;
//	private double demandesPourcentage;
	private double reponsesPourcentage;

	public BatchData(Integer id, String name, String partenaire, Date dateLancement, Boolean isRunning, Long rate, String motif, double reponsesPourcentage) {
		this.id = id;
		this.name = name;
		this.partenaire = partenaire;
		this.dateLancement = dateLancement;
		this.isRunning = isRunning;
		this.rate = rate;
		this.motif = motif;
		this.reponsesPourcentage = reponsesPourcentage;
	}

	public BatchData(Integer id, String name, String partenaire, Date dateLancement, Boolean isRunning, Long rate, String motif) {
		this.id = id;
		this.name = name;
		this.partenaire = partenaire;
		this.dateLancement = dateLancement;
		this.isRunning = isRunning;
		this.rate = rate;
		this.motif = motif;
	}

	public BatchData(Integer id, String name, String partenaire, Date dateLancement, Boolean isRunning, int nbr, String dateReponse, Long rate, String motif, double reponsesPourcentage) {
		this.id = id;
		this.name = name;
		this.partenaire = partenaire;
		this.dateLancement = dateLancement;
		this.isRunning = isRunning;
		this.nbr = nbr;
		this.dateReponse = dateReponse;
		this.rate = rate;
		this.motif = motif;
		this.reponsesPourcentage = reponsesPourcentage;
	}
}
