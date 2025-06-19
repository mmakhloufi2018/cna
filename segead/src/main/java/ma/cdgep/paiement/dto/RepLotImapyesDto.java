package ma.cdgep.paiement.dto;

import java.util.Set;

public class RepLotImapyesDto {

	private Integer numeroLot;

	private Integer statutLot;

	private String motif;

	private Set<RepImpayeDto> impayesRejetes;

	public Integer getNumeroLot() {
		return numeroLot;
	}

	public void setNumeroLot(Integer numeroLot) {
		this.numeroLot = numeroLot;
	}

	public String getMotif() {
		return motif;
	}

	public void setMotif(String motif) {
		this.motif = motif;
	}

	public Set<RepImpayeDto> getImpayesRejetes() {
		return impayesRejetes;
	}

	public void setImpayesRejetes(Set<RepImpayeDto> impayesRejetes) {
		this.impayesRejetes = impayesRejetes;
	}

	public Integer getStatutLot() {
		return statutLot;
	}

	public void setStatutLot(Integer statutLot) {
		this.statutLot = statutLot;
	}

}
