package ma.cdgep.demande.dto;

import java.util.List;

public class DemandeRejeteeDto {

	private String dateDemande;
	private String referenceCnss;
	private String immatriculation;
	private String statutDemande;
	private String motifDemande;
	
	public static String MESSAGE_REFERENCE_OBLIGATOIRE = "RD01"; //"La référence CNSS est obligatoire"; 
	public static String MESSAGE_DATE_ANNULATION_OBLIGATOIRE = "RD21";//"La date d’annulation pour le dossier n’est pas fournie"; 
	public static String MESSAGE_DOSSIER_OUVERT = "RD03";//"Il existe un dossier ouvert pour cette personne"; 
	public static String MESSAGE_DEMANDE_DOIT_ETRE_OUVERTE	 = "RD23";//"La référence doit être ouverte "; 
	public static String MESSAGE_REFERENCE_DEMANDE_EXISTE = "RD02";//"La référence de la demande existe déjà"; 
	public static String MESSAGE_REFERENCE_DEMANDE_INEXISTANTE = "RD22";//"La référence de la demande est inexistante"; 
	public static String MESSAGE_REFERENCE_NON_LIE_DOSSIER_OUVERT = "RD61";//"La référence de la demande est inexistante"; 
	public static String MESSAGE_IDCS_EXIST = "RD09";//"L’IDCS n’est pas fourni pour certains membres"; 
	
	public static String MESSAGE_DATE_DEMANNDE_OBLIGATOIRE = "RD04";//"La date de la demande est obligatoire"; 
	public static String MESSAGE_PRESTATION_OBLIGATOIRE = "RD06";//"La prestation est obligatoire"; 
	public static String MESSAGE_IDCS_DEMANDEUR_OBLIGATOIRE = "RD08";//"L'IDCS du demandeur est obligatoire"; 
	public static String MESSAGE_IDCS_CORRESPOND_PAS_A_CELUI_DEMANDEUR = "RD70";//"L’IDCS ne correspond pas à celui du demandeur"; 
	public static String MESSAGE_RD31= "RD71";//"Le recours ne concerne pas une demande recevable par la CNRA"; 
	public static String MESSAGE_RD60= "RD60";//"Ajout Membre : Discordance entre IDCS et référence CNSS"; 
	public static String MESSAGE_RD61= "RD61";//"Ajout Membre : Référence CNSS n'est lié à aucun dossier ouvert"; 
	public static String MESSAGE_RD62= "RD62";//"Ajout Membre : Aucun membre n'est envoyé"; 
	public static String MESSAGE_RD63= "RD63";//"Un enfant ne peut pas être affecté à une demande allocation forfaitaire"; 
	public static String MESSAGE_RD64= "RD64";//"Un autre conjoint est toujours lié au demandeur (pour les demandeurs femme)"; 
	public static String MESSAGE_RD65= "RD65";//"Enfant est déjà membre dans cette demande"; 
	public static String MESSAGE_RD66= "RD65";//"Conjoint est déjà membre dans cette demande"; 
	public static String MESSAGE_RD32= "RD72";//"L'idcs ne correspond pas à la reference"; 
	public static String MESSAGE_SCORE_RSU_NON_RENSEIGNE = "RD11";
	public static String MESSAGE_CODE_MENAGE_RSU_N_EST_PAS_RENSEIGNE = "RD12";
	public static String MESSAGE_LIEN_PARENTE_N_EST_PAS_RENSEIGNE = "RD13";//Lien de parenté n'est pas renseigné pour tous les enfants
	public static String MESSAGE_L_ETAT_MATRIMONIAL_DU_DEMANDEUR_N_EST_PAS_RENSEIGNE = "RD15";//l'état matrimonial du demandeur n'est pas renseigné
	public static String MESSAGE_L_ETAT_MATRIMONIAL_DU_DEMANDEUR_M_AVEC_0_CONJOINT = "RD34";//l'état matrimonial du demandeur n'est pas renseigné
	public static String MESSAGE_IDENTIFIANT_SCOLARITE_NON_CONFORME = "RD27";//Identifiant de scolarité non conforme
	public static String MESSAGE_INCOHERENCE_ENTRE_GENRE_DEMANDEUR_CIN_MERE_PERE = "RD41";//Identifiant de scolarité non conforme
	public static String MESSAGE_AGE_PLUS_6_MOIS = "RD30";//Identifiant de scolarité non conforme
	public static String MESSAGE_DATE_NAISSANCE_BEFORE_011223 = "RD31";//Identifiant de scolarité non conforme
	public static String MESSAGE_CIN_MERE_EGALE_CIN_DEMANDEUR_GENRE_DEMANDEUR_H = "RD42";//Identifiant de scolarité non conforme
	public static String MESSAGE_DONNEES_HANDICAP_NON_CONFORME = "RD28";
	public static String MESSAGE_DONNEES_ORPHELIN_NON_CONFORME = "RD29";
	public static String MESSAGE_MEMBRE_AGE_MOINS_21 = "RD20";//Membre autre que le demandeur âgé de moins de 21 ans
	public static String MESSAGE_MEMBRES_N_ONT_PAS_MEME_CODE_MENAGE = "RD18";//Les membres n'ont pas tous le même code ménage RSU du demandeur
	public static String MESSAGE_DEMANDEUR_N_EST_PAS_CHEF_MENAGE = "RD19";//Le demandeur n'est pas le chef de ménage RSU
	public static String MESSAGE_IDCS_DECLARE_PLUSIEURS_FOIS = "RE710";//Le demandeur n'est pas le chef de ménage RSU
	public static String MESSAGE_IDCS_MEMBRE_OBLIGATOIRE = "RD09";//"L'IDCS des membres est obligatoire"; 
	public static String MESSAGE_CIN_OBLIGATOIRE = "RD24";//"L'IDCS des membres est obligatoire"; 
	public static String MESSAGE_DECLARATION_ENFNANT_OBLIGATOIRE = "RD25";//"L'IDCS des membres est obligatoire"; 
	public static String MESSAGE_DEMANDE_FOR_AVEC_ENFANT = "RD800";//"L'IDCS des membres est obligatoire"; 
	public static String MESSAGE_GENRE_ERRONE = "RD26";//"L'IDCS des membres est obligatoire"; 
	public static String MESSAGE_DATE_NAISSANCE_DEMANDEUR_OBLIGATOIRE = "RD10";//"La date de naisssance du demandeur est obligatoire"; 
	public static String MESSAGE_DATE_NAISSANCE_MEMBRE_OBLIGATOIRE = "RD10";//"La date de naissance de tous les membres est obligatoire"; 
	public static String MESSAGE_DATE_FORMAT_ERRONEE = "RD05";//"La date doit être sous le format dd/MM/YYYY"; 
	public static String MESSAGE_DATE_NAISSANCE_OBLIGATOIRE = "RD10";//"La date de naisssance est obligatoire pour les membres"; 
	
	public String getDateDemande() {
		return dateDemande;
	}
	public void setDateDemande(String dateDemande) {
		this.dateDemande = dateDemande;
	}
	public String getReferenceCnss() {
		return referenceCnss;
	}
	public void setReferenceCnss(String referenceCnss) {
		this.referenceCnss = referenceCnss;
	}
	public String getImmatriculation() {
		return immatriculation;
	}
	public void setImmatriculation(String immatriculation) {
		this.immatriculation = immatriculation;
	}
	public String getStatutDemande() {
		return statutDemande;
	}
	public void setStatutDemande(String statutDemande) {
		this.statutDemande = statutDemande;
	}
	public String getMotifDemande() {
		return motifDemande;
	}
	public void setMotifDemande(String motifDemande) {
		this.motifDemande = motifDemande;
	}

	public static DemandeRejeteeDto getByReference(List<DemandeRejeteeDto> demandes, String reference) {
		if (demandes == null || demandes.size() == 0 || reference == null)
			return null;
		return demandes.stream().filter(a -> a != null && reference.equals(a.getReferenceCnss())).findAny().orElse(null);

	}

	@Override
	public String toString() {
		return "date Demande=" + dateDemande + ", referenceCnss=" + referenceCnss
				+ ", immatriculation=" + immatriculation + ", statut=" + statutDemande + ", motif="
				+ motifDemande + " ;";
	}
	
	

}
