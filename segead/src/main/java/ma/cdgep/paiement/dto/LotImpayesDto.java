package ma.cdgep.paiement.dto;

import java.util.Set;
import java.util.stream.Collectors;

import ma.cdgep.paiement.entity.LotImpayesEntity;
import ma.cdgep.utils.Utils;

public class LotImpayesDto {

	private Integer numeroLot;

	private String dateLot;

	private Set<ImpayeDto> impayes;

	public Integer getNumeroLot() {
		return numeroLot;
	}

	public void setNumeroLot(Integer numeroLot) {
		this.numeroLot = numeroLot;
	}

	public Set<ImpayeDto> getImpayes() {
		return impayes;
	}

	public void setImpayes(Set<ImpayeDto> impayes) {
		this.impayes = impayes;
	}

	public String getDateLot() {
		return dateLot;
	}

	public void setDateLot(String dateLot) {
		this.dateLot = dateLot;
	}

	public static LotImpayesEntity to(LotImpayesDto in) {
		if (in == null)
			return null;
		LotImpayesEntity out = new LotImpayesEntity();

		out.setNumeroLot(in.getNumeroLot());
		out.setDateLot(Utils.stringToDate(in.getDateLot(), Utils.FOURMAT_DATE_STRING));

		if (in.getImpayes() != null) {
			out.setImpayes(in.getImpayes().stream().map(ImpayeDto::to).collect(Collectors.toSet()));

			out.getImpayes().forEach(i -> i.setLotImpayes(out));
		}

		return out;
	}

}
