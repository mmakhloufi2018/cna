package ma.cdgep.demande.dto;

import java.util.List;
import java.util.stream.Collectors;

import ma.cdgep.demande.entity.LotRecoursEntity;
import ma.cdgep.utils.Utils;

public class LotRecoursDto {

	private String referenceLot;
	private String dateLot;
	private List<DemandeRecoursDto> demandes;

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

	public List<DemandeRecoursDto> getDemandes() {
		return demandes;
	}

	public void setDemandes(List<DemandeRecoursDto> demandes) {
		this.demandes = demandes;
	}

	public static LotRecoursEntity to(LotRecoursDto lotDto) {
		if (lotDto == null)
			return null;
		LotRecoursEntity entity = new LotRecoursEntity();

		entity.setDateLot(Utils.stringToDate(lotDto.getDateLot(), Utils.FOURMAT_DATE_STRING));
		entity.setReferenceLot(lotDto.getReferenceLot());

		if (lotDto.getDemandes() != null) {

			entity.setRecours(lotDto.getDemandes().stream().map(DemandeRecoursDto::to).collect(Collectors.toList()));

			entity.getRecours().forEach(c -> c.setLot(entity));
		}
		return entity;
	}

	


}
