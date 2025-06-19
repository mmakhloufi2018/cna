package ma.cdgep.demande.dto;

import java.util.List;
import java.util.stream.Collectors;

import ma.cdgep.demande.entity.LotAjoutMembreEntity;
import ma.cdgep.utils.Utils;

public class LotAjoutMembreDto {

	private String referenceLot;
	private String dateLot;
	private List<DemandeAjoutMembreDto> demandes;

	public String getReferenceLot() {
		return referenceLot;
	}

	public void setReferenceLot(String referenceLot) {
		this.referenceLot = referenceLot;
	}

	public String getDateLot() {
		return dateLot;
	}

	public void setDateLot(String dateLot) {
		this.dateLot = dateLot;
	}

	public List<DemandeAjoutMembreDto> getDemandes() {
		return demandes;
	}

	public void setDemandes(List<DemandeAjoutMembreDto> demandes) {
		this.demandes = demandes;
	}

	public static LotAjoutMembreEntity to(LotAjoutMembreDto lotDto) {
		if (lotDto == null)
			return null;
		LotAjoutMembreEntity entity = new LotAjoutMembreEntity();

		entity.setDateLot(Utils.stringToDate(lotDto.getDateLot(), Utils.FOURMAT_DATE_STRING));
		entity.setReferenceLot(lotDto.getReferenceLot());

		if (lotDto.getDemandes() != null) {

			entity.setDemandes(lotDto.getDemandes().stream().map(DemandeAjoutMembreDto::to).collect(Collectors.toList()));

			entity.getDemandes().forEach(c -> c.setLot(entity));
		}
		return entity;
	}

	


}
