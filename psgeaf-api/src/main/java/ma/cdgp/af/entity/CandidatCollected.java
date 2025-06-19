package ma.cdgp.af.entity;

import java.io.Serializable;
//import java.util.List;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
//import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.dto.af.CandidatInfos;
import ma.cdgp.af.utils.Col;

@Entity
@Table(name = "AF_CANDIDAT_COLLECTED")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CandidatCollected implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "CANDIDAT_ID")
	private Long idCandidat;
	
	@Column(name = "CIN")
	private String cin;
	
	@Column(name = "IDCS")
	private String idcs;
	
	@Column(name = "DATE_NAISSANCE")
	private String dateNaissance;
	
	@Column(name = "GENRE")
	private String genre;
	
	@Column(name = "MOIS_ANNEE")
	private String moisAnnee;
	
	@Column(name = "MASSAR")
	private String massar;

	@Column(name = "CEF")
	private String cef;

	@Column(name = "TYPE")
	private String type;

	@Column(name = "ACTIVE")
	private String active;
	
	@Column(name = "BL_DELETE")
	private Boolean blDelete;
	
	@Column(name = "REFERENCE")
	private String reference;
	
	
	@Column(name = "REQUETE_SC")
	private String requeteSc;
	
	@Column(name = "REQUETE_SP")
	private String requeteSp;
	
	
	@Column(name = "REQUETE_RSU")
	private String requeteRsu;
	
	
	@Column(name = "REQUETE_BR")
	private String requeteBr;
	
	@Column(name = "REQUETE_HA")
	private String requeteHa;
	
	
	@Column(name = "REQUETE_FEF")
	private String requeteFef;
	
	@Column(name = "ID_PERSON")
	private String idPerson;
	
	@Column(name = "ARCHIVE")
	private Boolean archive;
	
	
	private Date dateCreation;
	
	
	@PrePersist
	public void beforeSave() {
		this.dateCreation = new Date();
	}
	public static CandidatCollected fromCandidatInfo(CandidatInfos in, String reference) {
		if (in == null) return null;
		CandidatCollected out = new CandidatCollected();
		out.setIdCandidat(in.getId() != null ? Long.valueOf(in.getId()) : null);
		out.setCin(in.getCin());
		out.setIdcs(in.getIdcs());
		out.setActive(in.getActive());
		out.setCef(in.getCef());
		out.setDateNaissance(in.getDateNaissance());
		out.setGenre(in.getGenre());
		out.setMassar(in.getMassar());
		out.setMoisAnnee(in.getMoisAnnee());
		out.setType(in.getType());
		out.setReference(reference);
		out.setRequeteFef(in.getRequeteFef());
		out.setRequeteBr(in.getRequeteBr());
		out.setRequeteHa(in.getRequeteHa());
		out.setRequeteRsu(in.getRequeteRsu());
		out.setRequeteSc(in.getRequeteSc());
		out.setRequeteSp(in.getRequeteSp());
		out.setIdPerson(in.getIdPerson());
		
		return out;
	}
}
