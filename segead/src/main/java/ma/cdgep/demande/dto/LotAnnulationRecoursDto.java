package ma.cdgep.demande.dto;

import java.util.List;
import java.util.stream.Collectors;

import ma.cdgep.demande.entity.LotAjoutMembreEntity;
import ma.cdgep.demande.entity.LotAnnulationRecoursEntity;
import ma.cdgep.utils.Utils;

public class LotAnnulationRecoursDto {

	private String dateLot;

	private String referenceLot;

	private Integer totalDossiers;

	private Integer totalAcceptes;

	private Integer totalRejetes;

	private String statut;

	private String motif;

	private List<AnnulationRecoursDto> demandes;

	public String getDateLot() {
		return dateLot;
	}

	public void setDateLot(String dateLot) {
		this.dateLot = dateLot;
	}

	public String getReferenceLot() {
		return referenceLot;
	}

	public void setReferenceLot(String referenceLot) {
		this.referenceLot = referenceLot;
	}

	public void setTotalDossiers(Integer totalDossiers) {
		this.totalDossiers = totalDossiers;
	}

	public void setTotalAcceptes(Integer totalAcceptes) {
		this.totalAcceptes = totalAcceptes;
	}

	public void setTotalRejetes(Integer totalRejetes) {
		this.totalRejetes = totalRejetes;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public void setMotif(String motif) {
		this.motif = motif;
	}

	public List<AnnulationRecoursDto> getDemandes() {
		return demandes;
	}

	public void setDemandes(List<AnnulationRecoursDto> demandes) {
		this.demandes = demandes;
	}

	public LotAnnulationRecoursDto() {
		super();
	}

	public static LotAnnulationRecoursEntity to(LotAnnulationRecoursDto lotDto) {
		if (lotDto == null)
			return null;
		LotAnnulationRecoursEntity entity = new LotAnnulationRecoursEntity();

		entity.setDateLot(Utils.stringToDate(lotDto.getDateLot(), Utils.FOURMAT_DATE_STRING));
		entity.setReferenceLot(lotDto.getReferenceLot());

		if (lotDto.getDemandes() != null) {

			entity.setDemandes(
					lotDto.getDemandes().stream().map(AnnulationRecoursDto::to).collect(Collectors.toList()));

			entity.getDemandes().forEach(c -> {
				c.setStatut("RECEPTIONNE");
				c.setLot(entity);
			});
		}
		return entity;
	}

}
