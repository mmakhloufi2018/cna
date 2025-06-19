package ma.cdgep.paiement.dto;

import java.util.ArrayList;
import java.util.List;

import ma.cdgep.repository.PaiementCnss;

public class PaiementCnssDto {
    private String referencePaiement;
    private String montant;
    private List<Detail> details;

    // Getters et Setters

    public PaiementCnssDto() {}

    public PaiementCnssDto(String referencePaiement, String montant, List<Detail> details) {
        this.referencePaiement = referencePaiement;
        this.montant = montant;
        this.details = details;
    }

	public String getReferencePaiement() {
		return referencePaiement;
	}

	public void setReferencePaiement(String referencePaiement) {
		this.referencePaiement = referencePaiement;
	}

	public String getMontant() {
		return montant;
	}

	public void setMontant(String montant) {
		this.montant = montant;
	}

	public List<Detail> getDetails() {
		return details;
	}

	public void setDetails(List<Detail> details) {
		this.details = details;
	}

	public static PaiementCnssDto from(List<PaiementCnss> paiements) {
        if (paiements == null || paiements.isEmpty()) {
            return null;
        }

        // Utiliser le premier paiement pour obtenir la référence et le montant
        PaiementCnss premierPaiement = paiements.get(0);
        String referencePaiement = premierPaiement.getReferencePaiement();
        String montant = premierPaiement.getMontantOperation();

        // Créer la liste des détails
        List<Detail> details = new ArrayList<>();
        for (PaiementCnss paiement : paiements) {
            Detail detail = new Detail(
                paiement.getPeriode(),
                paiement.getIdcs(),
                paiement.getRubrique(),
                paiement.getType(),
                paiement.getMontant(),new ArrayList<Caracteristique>()
            );

            // Ajouter les caractéristiques si nécessaire
//            Caracteristique caract = new Caracteristique("offset", paiement.getOffset());
            if(paiement.getOffset() != null && !paiement.getOffset().isEmpty())
            	detail.getCaracteristiques().addAll(detail.getCaracteristiques(paiement.getOffset()));
            details.add(detail);
        }

        // Créer et retourner le DTO
        return new PaiementCnssDto(referencePaiement, montant, details);
    }
    
    

    // Getters et Setters
}

class Detail {
    private String periode;
    private String idcs;
    private String nature;
    private String type;
    private String montant;
    private List<Caracteristique> caracteristiques;

    // Getters et Setters

    public Detail() {}

    public Detail(String periode, String idcs, String nature, String type, String montant, List<Caracteristique> caracteristiques) {
        this.periode = periode;
        this.idcs = idcs;
        this.nature = nature;
        this.type = type;
        this.montant = montant;
        this.caracteristiques = caracteristiques;
    }

	public String getPeriode() {
		return periode;
	}

	public void setPeriode(String periode) {
		this.periode = periode;
	}

	public String getIdcs() {
		return idcs;
	}

	public void setIdcs(String idcs) {
		this.idcs = idcs;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMontant() {
		return montant;
	}

	public void setMontant(String montant) {
		this.montant = montant;
	}

	public List<Caracteristique> getCaracteristiques() {
		return caracteristiques;
	}

	public void setCaracteristiques(List<Caracteristique> caracteristiques) {
		this.caracteristiques = caracteristiques;
	}

	public  List<Caracteristique> getCaracteristiques(String offset){
		if(offset == null || offset.length() == 0)
			return null;
		List<Caracteristique> Caracteristiques = new ArrayList<Caracteristique>();
		Caracteristiques.add(getDemandeurSalarie(offset));
		Caracteristiques.add(getDemandeurPensionne(offset));
		Caracteristiques.add(getDemandeurBeneficiaireAF(offset));
		Caracteristiques.add(getConjointSalarie(offset));
		Caracteristiques.add(getConjointPensionne(offset));
		Caracteristiques.add(getConjointBeneficiaireAF(offset));
		Caracteristiques.add(getEnfantSalarie(offset));
		Caracteristiques.add(getEnfantPensionne(offset));
		Caracteristiques.add(getEnfantBeneficiaireAF(offset));
		Caracteristiques.add(getEnfantRang(offset));
		Caracteristiques.add(getEnfantAge(offset));
		Caracteristiques.add(getEnfantScolarise(offset));
		Caracteristiques.add(getEnfantOrphelinPere(offset));
		Caracteristiques.add(getEnfantHandicape(offset));
		Caracteristiques.add(getEnfantBourse(offset));
		
		
		return Caracteristiques;
	}

	private Caracteristique getDemandeurSalarie(String offset) {
		String val = null;
		if(offset.contains("DEM:"))
			val = "0".equals(offset.substring(6, 7)) ? "NON" : "OUI";
		if(!offset.contains("DEM:"))
			val = "0".equals(offset.substring(1, 2)) ? "NON" : "OUI";
		return new Caracteristique("DEMANDEUR SALARIE", val);
	}
	
	private Caracteristique getDemandeurPensionne(String offset) {
		String val = null;
		if(offset.contains("DEM:") && offset.length() > 7 )
			val = "0".equals(offset.substring(7, 8)) ? "NON" : "OUI";
		if(!offset.contains("DEM:"))
			val = "0".equals(offset.substring(2, 3)) ? "NON" : "OUI";
		return new Caracteristique("DEMANDEUR PENSIONNE", val);
	}
	
	private Caracteristique getDemandeurBeneficiaireAF(String offset) {
		String val = null;
		if(offset.contains("DEM:") && offset.length() > 8 )
			val = "0".equals(offset.substring(8, 9)) ? "NON" : "OUI";
		if(!offset.contains("DEM:"))
			val = "0".equals(offset.substring(3, 4)) ? "NON" : "OUI";
		return new Caracteristique("DEMANDEUR BENEFICIAIRE AF", val);
	}
	
	
	private Caracteristique getConjointSalarie(String offset) {
		String val = null;
		if(offset.contains("DEM:") && offset.contains("- ENF:") && offset.length() > 6 )
			val = "0".equals(offset.substring(offset.indexOf("- ENF:") -3 , offset.indexOf("- ENF:") -2)) ? "NON" : "OUI";
		if(!offset.contains("DEM:"))
			val = "0".equals(offset.substring(46, 47)) ? "NON" : "OUI";
		return new Caracteristique("CONJOINT SALARIE", val);
	}
	
	private Caracteristique getConjointPensionne(String offset) {
		String val = null;
		if(offset.contains("DEM:") && offset.contains("- ENF:") && offset.length() > 7 )
			val = "0".equals(offset.substring(offset.indexOf("- ENF:") -2 , offset.indexOf("- ENF:") -1)) ? "NON" : "OUI";
		if(!offset.contains("DEM:"))
			val = "0".equals(offset.substring(47, 48)) ? "NON" : "OUI";
		return new Caracteristique("CONJOINT PENSIONNE", val);
	}
	
	private Caracteristique getConjointBeneficiaireAF(String offset) {
		String val = null;
		if(offset.contains("DEM:") && offset.contains("- ENF:") && offset.length() > 8 )
			val = "0".equals(offset.substring(offset.indexOf("- ENF:") -1 , offset.indexOf("- ENF:"))) ? "NON" : "OUI";
		if(!offset.contains("DEM:"))
			val = "0".equals(offset.substring(48, 49)) ? "NON" : "OUI";
		return new Caracteristique("CONJOINT BENEFICIAIRE AF", val);
	}
    
	private Caracteristique getEnfantSalarie(String offset) {
		String val = null;
		if(offset.contains("DEM:")  && offset.contains("- ENF:") && offset.length() > 6 )
			val = "0".equals(offset.substring(offset.indexOf("- ENF:") +6 , offset.indexOf("- ENF:") +7)) ? "NON" : "OUI";
		if(!offset.contains("DEM:"))
			val = "0".equals(offset.substring(106, 107)) ? "NON" : "OUI";
		return new Caracteristique("ENFANT SALARIE", val);
	}
	
	private Caracteristique getEnfantPensionne(String offset) {
		String val = null;
		if(offset.contains("DEM:") && offset.contains("- ENF:") && offset.length() > 7 )
			val = "0".equals(offset.substring(offset.indexOf("- ENF:") +7 , offset.indexOf("- ENF:") +8)) ? "NON" : "OUI";
		if(!offset.contains("DEM:"))
			val = "0".equals(offset.substring(107, 108)) ? "NON" : "OUI";
		return new Caracteristique("ENFANT PENSIONNE", val);
	}
	
	private Caracteristique getEnfantBeneficiaireAF(String offset) {
		String val = null;
		if(offset.contains("DEM:") && offset.contains("- ENF:") && offset.length() > 8 )
			val = "0".equals(offset.substring(offset.indexOf("- ENF:") +8 , offset.indexOf("- ENF:") +9)) ? "NON" : "OUI";
		if(!offset.contains("DEM:"))
			val = "0".equals(offset.substring(108, 109)) ? "NON" : "OUI";
		return new Caracteristique("ENFANT BENEFICIAIRE AF", val);
	}
	
	private Caracteristique getEnfantRang(String offset) {
		String val = null;
		if(offset.contains("DEM:")&& offset.contains("RANG:"))
			val = offset.substring(offset.indexOf("- HANDICAPE") - 2 , offset.indexOf("- HANDICAPE")).replace(":", "");
		if(!offset.contains("DEM:"))
			val = offset.substring(140, 142);
		return new Caracteristique("RANG", val);
	}
	private Caracteristique getEnfantAge(String offset) {
		String val = null;
		if(offset.contains("DEM:")&& offset.contains("AGE:"))
			val = offset.substring(offset.indexOf("- SCOLARISE") - 2 , offset.indexOf("- SCOLARISE")).replace(":", "");
		if(!offset.contains("DEM:"))
			val = offset.substring(142, 144);
		return new Caracteristique("AGE", val);
	}
	private Caracteristique getEnfantScolarise(String offset) {
		String val = null;
		if(offset.contains("DEM:")&& offset.contains("- SCOLARISE:"))
			val =  "0".equals(offset.substring(offset.indexOf("-ORPHELIN_PERE") - 1 , offset.indexOf("-ORPHELIN_PERE"))) ? "NON" : "OUI";
		if(!offset.contains("DEM:"))
			val = "0".equals(offset.substring(145, 146)) ? "NON" : "OUI";
		return new Caracteristique("SCOLARISE", val);
	}
	private Caracteristique getEnfantOrphelinPere(String offset) {
		String val = null;
		if(offset.contains("DEM:")&& offset.contains("-ORPHELIN_PERE:"))
			val =  "0".equals(offset.substring(offset.indexOf("- BOURSE") - 1 , offset.indexOf("- BOURSE"))) ? "NON" : "OUI";
		if(!offset.contains("DEM:"))
			val = "0".equals(offset.substring(144, 145)) ? "NON" : "OUI";
		return new Caracteristique("ORPHELIN PERE", val);
	}
	private Caracteristique getEnfantHandicape(String offset) {
		String val = null;
		if(offset.contains("DEM:")&& offset.contains("- AGE"))
			val =  "0".equals(offset.substring(offset.indexOf("- AGE") - 1 , offset.indexOf("- AGE"))) ? "NON" : "OUI";
		if(!offset.contains("DEM:"))
			val = "0".equals(offset.substring(146, 147)) ? "NON" : "OUI";
		return new Caracteristique("HANDICAPE", val);
	}
	private Caracteristique getEnfantBourse(String offset) {
		String val = null;
		if(offset.contains("DEM:") && offset.contains("-MONT.AV"))
			val =  offset.substring( offset.indexOf("BOURSE:") + 7 , offset.indexOf("-MONT.AV"));
		if(!offset.contains("DEM:"))
			val = offset.substring(147, 157).replace(" ", "");
		return new Caracteristique("BOURSE", val);
	}
    /*
     *  
     */
	//100000-----------------------------GM215657--1000-------------------------------                9,372363610000------------------------------0103000         0-------------------------
	//DEM:01000 - CONJ CIN:GN165472 1000- ENF:1000- RANG:1- HANDICAPE:0- AGE:18- SCOLARISE:1-ORPHELIN_PERE:0- BOURSE:0-MONT.AV.BOURSE:0- SCORE:9,17827
	//NVL (DC_ENF.RESIDE_MAROC, '1') || NVL (DC_ENF.SALARIE, '0') || NVL (DC_ENF.PENSIONNE, '0')|| NVL (DC_ENF.BENEFICIE_AF, '0')
	//NVL (DC_CONJ.RESIDE_MAROC, '1') || NVL (DC_CONJ.SALARIE, '0') || NVL (DC_CONJ.PENSIONNE, '0')|| NVL (DC_CONJ.BENEFICIE_AF, '0') data_cap_conj,
	//NVL (DC_DEM.EST_CHEF_MENAGE, '1')  || NVL (DC_DEM.RESIDE_MAROC, '1')  || NVL (DC_DEM.SALARIE, '0') || NVL (DC_DEM.PENSIONNE, '0') || NVL (DC_DEM.BENEFICIE_AF, '0')

}

class Caracteristique {
    private String nom;
    private String valeur;

    // Getters et Setters

    public Caracteristique() {}

    public Caracteristique(String nom, String valeur) {
        this.nom = nom;
        this.valeur = valeur;
    }

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getValeur() {
		return valeur;
	}

	public void setValeur(String valeur) {
		this.valeur = valeur;
	}
	
	
	
    // Getters et Setters
    
}
