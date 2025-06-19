package ma.cdgep.demande.dto;

import java.util.List;
import java.util.stream.Collectors;

import ma.cdgep.demande.entity.LotDemandeEntity;
import ma.cdgep.utils.Utils;

public class LotDemandeDto {

	private String referenceLot;
	private String dateLot;
	private String prestation;
	private List<DemandeDto> demandes;

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

	public String getPrestation() {
		return prestation;
	}

	public void setPrestation(String prestation) {
		this.prestation = prestation;
	}

	public List<DemandeDto> getDemandes() {
		return demandes;
	}

	public void setDemandes(List<DemandeDto> demandes) {
		this.demandes = demandes;
	}

	public static LotDemandeEntity to(LotDemandeDto dto) {
		if (dto == null)
			return null;
		LotDemandeEntity entity = new LotDemandeEntity();

		entity.setDateLot(Utils.stringToDate(dto.getDateLot(), Utils.FOURMAT_DATE_STRING));
		entity.setReferenceLot(dto.getReferenceLot());
		entity.setPrestation(dto.getPrestation());

		if (dto.getDemandes() != null && dto.getDemandes().size() > 0 ) {

			entity.setDemandes(dto.getDemandes().stream().map(DemandeDto::to).collect(Collectors.toList()));

			entity.getDemandes().forEach(c -> c.setLot(entity));
		}
		return entity;
	}

	public LotDemandeDto(String referenceLot, String dateLot, String prestation, List<DemandeDto> demandes) {
		super();
		this.referenceLot = referenceLot;
		this.dateLot = dateLot;
		this.prestation = prestation;
		this.demandes = demandes;
	}

	
	public static LotDemandeEntity to(LotDemandeAnnuleDto lotDto) {
		if (lotDto == null)
			return null;
		LotDemandeEntity entity = new LotDemandeEntity();

		entity.setDateLot(Utils.stringToDate(lotDto.getDateLot(), Utils.FOURMAT_DATE_STRING));
		entity.setReferenceLot(lotDto.getReferenceLot());

		if (lotDto.getDemandes() != null) {

			entity.setDemandes(lotDto.getDemandes().stream().map(DemandeAnnuleeDto::to).collect(Collectors.toList()));

			entity.getDemandes().forEach(c -> c.setLot(entity));
		}
		return entity;
	}
	

}
