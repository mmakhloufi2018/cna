package ma.cdgep.droit.dto;

import java.util.List;

public class _EnfantDto {

	private String idcs;
	private String rang;
	private Double montant;
	private List<ConstatDto> constats;
	
	public String getIdcs() {
		return idcs;
	}
	public void setIdcs(String idcs) {
		this.idcs = idcs;
	}
	public String getRang() {
		return rang;
	}
	public void setRang(String rang) {
		this.rang = rang;
	}
	public Double getMontant() {
		return montant;
	}
	public void setMontant(Double montant) {
		this.montant = montant;
	}
	public List<ConstatDto> getConstats() {
		return constats;
	}
	public void setConstats(List<ConstatDto> constats) {
		this.constats = constats;
	}
	
	
}
