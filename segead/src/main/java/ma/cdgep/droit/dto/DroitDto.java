package ma.cdgep.droit.dto;

import java.util.List;

public class DroitDto {

	private String reference;
	private String prestation;
	private String typeDemande;
	private String eligible;
	private Double montant;
	private String dateEffet;
	private String idcs;
	private List<ConstatDto> constats;
	private List<_ConjointDto> conjoints;
	private List<_EnfantDto> enfants;
	
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getPrestation() {
		return prestation;
	}
	public void setPrestation(String prestation) {
		this.prestation = prestation;
	}
	public String getTypeDemande() {
		return typeDemande;
	}
	public void setTypeDemande(String typeDemande) {
		this.typeDemande = typeDemande;
	}
	public String getEligible() {
		return eligible;
	}
	public void setEligible(String eligible) {
		this.eligible = eligible;
	}
	public Double getMontant() {
		return montant;
	}
	public void setMontant(Double montant) {
		this.montant = montant;
	}	
	public String getDateEffet() {
		return dateEffet;
	}
	public void setDateEffet(String dateEffet) {
		this.dateEffet = dateEffet;
	}
	public String getIdcs() {
		return idcs;
	}
	public void setIdcs(String idcs) {
		this.idcs = idcs;
	}
	public List<ConstatDto> getConstat() {
		return constats;
	}
	public void setConstat(List<ConstatDto> constats) {
		this.constats = constats;
	}
	public List<_ConjointDto> getConjoints() {
		return conjoints;
	}
	public void setConjoints(List<_ConjointDto> conjoints) {
		this.conjoints = conjoints;
	}
	public List<_EnfantDto> getEnfants() {
		return enfants;
	}
	public void setEnfants(List<_EnfantDto> enfants) {
		this.enfants = enfants;
	}
	
	

}
