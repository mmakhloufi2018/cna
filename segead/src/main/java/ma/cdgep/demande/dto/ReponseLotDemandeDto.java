package ma.cdgep.demande.dto;

import java.util.List;

import ma.cdgep.demande.entity.ReponseLotDemandeEntity;

public class ReponseLotDemandeDto {

	private String referenceLot;
	private String dateLot;
	private String prestation;
	private String motif;
	private String statut;
	private Integer totalAcceptes;
	private Integer totalDossiers;
	private Integer totalRejetes;
	private List<DemandeRejeteeDto> demandesRejetees;
	public static String STATUT_REJETE = "REJETE";
	public static String MESSAGE_REFERENCE_LOT_EXISTE = "RL01";//"La référence du lot est dupliquée";
	public static String MESSAGE_REFERENCE_LOT_NON_RENSEIGNEE = "RL02";//"Référence du lot non renseignée";
	public static String MESSAGE_DATE_LOT_NON_RENSEIGNEE = "RL03";//"Date lot non renseignée";
	public static String MESSAGE_FORMAT_DATE_NON_CONFORME = "RL04";//"Date lot non renseignée";
	public static String MESSAGE_CODE_PRESTATION_ERRONE = "RL05";//"Date lot non renseignée";

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

	public String getMotif() {
		return motif;
	}

	public void setMotif(String motif) {
		this.motif = motif;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public Integer getTotalAcceptes() {
		return totalAcceptes;
	}

	public void setTotalAcceptes(Integer totalAcceptes) {
		this.totalAcceptes = totalAcceptes;
	}

	public Integer getTotalDossiers() {
		return totalDossiers;
	}

	public void setTotalDossiers(Integer totalDossiers) {
		this.totalDossiers = totalDossiers;
	}

	public Integer getTotalRejetes() {
		return totalRejetes;
	}

	public void setTotalRejetes(Integer totalRejetes) {
		this.totalRejetes = totalRejetes;
	}

	public List<DemandeRejeteeDto> getDemandesRejetees() {
		return demandesRejetees;
	}

	public void setDemandesRejetees(List<DemandeRejeteeDto> demandesRejetees) {
		this.demandesRejetees = demandesRejetees;
	}

	public ReponseLotDemandeDto(LotDemandeDto lot) {
		this.dateLot = lot.getDateLot();
		this.prestation = lot.getPrestation();
		this.referenceLot = lot.getReferenceLot();
	}
	public ReponseLotDemandeDto(LotAnnulationRecoursDto lot) {
		this.dateLot = lot.getDateLot();
		this.referenceLot = lot.getReferenceLot();
	}
	
	public ReponseLotDemandeDto(LotDemandeAnnuleDto lot) {
		this.dateLot = lot.getDateLot();
		this.referenceLot = lot.getReferenceLot();
	}
	
	public ReponseLotDemandeDto(LotRecoursDto lot) {
		this.dateLot = lot.getDateLot();
		this.referenceLot = lot.getReferenceLot();
	}
	
	public ReponseLotDemandeDto(LotAjoutMembreDto lot) {
		this.dateLot = lot.getDateLot();
		this.referenceLot = lot.getReferenceLot();
	}
	
	
	public ReponseLotDemandeDto(String statut, String motif) {
		super();
		this.motif = motif;
		this.statut = statut;
	}
	
	public ReponseLotDemandeDto() {
		super();
	}

	public static ReponseLotDemandeEntity to(ReponseLotDemandeDto in) {
		if(in == null)
			return null;
		
		
		return new ReponseLotDemandeEntity(null, in.getReferenceLot() , in.getDateLot(), in.getPrestation(),in.getMotif(),in.getStatut(),
				in.getTotalAcceptes(), in.getTotalDossiers(),in.getTotalRejetes(),in.getDemandesRejeteesString());
	}

	private String getDemandesRejeteesString() {
		if(demandesRejetees == null || demandesRejetees.size() == 0)
			return null;
		String demandes = "";
		for (DemandeRejeteeDto demandeRejeteeDto : demandesRejetees) {
			demandes+=demandeRejeteeDto.toString();
		}
		if(demandes.length() > 2000)
			demandes = demandes.substring(0, 1999);
		return demandes;
	}

}
