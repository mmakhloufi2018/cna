package ma.cdgep.paiement.dto;

import ma.cdgep.paiement.entity.ImpayeEntity;

public class ImpayeDto {
	
	private String referencePaiement;
	private String idcs;
	private Double Montant;
	private String echeance;
	
	
	public String getReferencePaiement() {
		return referencePaiement;
	}
	public void setReferencePaiement(String referencePaiement) {
		this.referencePaiement = referencePaiement;
	}
	public String getIdcs() {
		return idcs;
	}
	public void setIdcs(String idcs) {
		this.idcs = idcs;
	}
	public Double getMontant() {
		return Montant;
	}
	public void setMontant(Double montant) {
		Montant = montant;
	}
	public String getEcheance() {
		return echeance;
	}
	public void setEcheance(String echeance) {
		this.echeance = echeance;
	}
	
	public static ImpayeEntity to(ImpayeDto in) {
		if (in == null)
			return null;
		ImpayeEntity out = new ImpayeEntity();
		
		out.setEcheance(in.getEcheance());
		out.setIdcs(in.getIdcs());
		out.setMontant(in.getMontant());
		out.setReferencePaiement(in.getReferencePaiement());
		return out;
	}
}
