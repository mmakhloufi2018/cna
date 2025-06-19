package ma.cdgep.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ma.cdgep.demande.dto.AutreMembreDto;
import ma.cdgep.demande.dto.ConjointDto;
import ma.cdgep.demande.dto.DemandeAjoutMembreDto;
import ma.cdgep.demande.dto.DemandeAnnuleeDto;
import ma.cdgep.demande.dto.DemandeDto;
import ma.cdgep.demande.dto.DemandeRecoursDto;
import ma.cdgep.demande.dto.DemandeRejeteeDto;
import ma.cdgep.demande.dto.DemandeurDto;
import ma.cdgep.demande.dto.EnfantDto;
import ma.cdgep.demande.dto.LotAjoutMembreDto;
import ma.cdgep.demande.dto.LotAnnulationRecoursDto;
import ma.cdgep.demande.dto.LotDemandeAnnuleDto;
import ma.cdgep.demande.dto.LotDemandeDto;
import ma.cdgep.demande.dto.LotRecoursDto;
import ma.cdgep.demande.dto.ReponseLotDemandeDto;
import ma.cdgep.demande.entity.ConjointEntity;
import ma.cdgep.demande.entity.DemandeAjoutMembreEntity;
import ma.cdgep.demande.entity.DemandeEntity;
import ma.cdgep.demande.entity.EnfantEntity;
import ma.cdgep.demande.entity.LotAjoutMembreEntity;
import ma.cdgep.demande.entity.LotAnnulationRecoursEntity;
import ma.cdgep.demande.entity.LotDemandeBruteEntity;
import ma.cdgep.demande.entity.LotDemandeEntity;
import ma.cdgep.demande.entity.LotRecoursEntity;
import ma.cdgep.demande.entity.RecoursEntity;
import ma.cdgep.dossier.entity.DossierAsdEntity;
import ma.cdgep.paiement.entity.EcheanceEntity;
import ma.cdgep.repository.DemandeAjoutMemebreRepository;
import ma.cdgep.repository.DemandeRepository;
import ma.cdgep.repository.DossierAsdRepository;
import ma.cdgep.repository.InfoControlAjoutMembre;
import ma.cdgep.repository.LotAnnulationRecoursRepository;
import ma.cdgep.repository.LotDemandeBruteRepository;
import ma.cdgep.repository.LotDemandeRepository;
import ma.cdgep.repository.LotRecoursRepository;
import ma.cdgep.repository.RecoursRepository;
import ma.cdgep.repository.RefIdcsDossier;
import ma.cdgep.repository.ReponseLotDemandeRepository;
import ma.cdgep.repository.lotAjoutMembreRepository;
import ma.cdgep.utils.Utils;

@Service
public class LotSerivce {

	@Autowired
	LotDemandeRepository lotRepository;
	
	@Autowired
	LotAnnulationRecoursRepository lotAnnulationRecoursRepository;

	@Autowired
	LotRecoursRepository lotRecoursRepository;

	@Autowired
	lotAjoutMembreRepository lotAjoutMembreRepository;

	@Autowired
	ReponseLotDemandeRepository reponseLotDemandeRepository;

	@Autowired
	DemandeRepository demandeRepository;

	@Autowired
	RecoursRepository recoursRepository;

	@Autowired
	DossierAsdRepository dossierAsdRepository;

	@Autowired
	LotDemandeBruteRepository lotDemandeBruteRepository;

	@Autowired
	DemandeAjoutMemebreRepository demandeAjoutMemebreRepository;
	
	
	
	public ReponseLotDemandeDto annulerRecoursLot(LotAnnulationRecoursDto lotDto, EcheanceEntity echeanceEncours) {
		if (lotDto == null)
			return null;

		saveJson(lotDto);

		ReponseLotDemandeDto reponseLot = controlerAnnulationRecoursLot(lotDto);

		Integer total = lotDto.getDemandes() == null ? 0 : lotDto.getDemandes().size();
		Integer nbrRejet = (reponseLot.getDemandesRejetees() == null ? 0 : reponseLot.getDemandesRejetees().size());
		if (ReponseLotDemandeDto.STATUT_REJETE.equals(reponseLot.getStatut())) {
			nbrRejet = total;
		}
		LotAnnulationRecoursEntity lot = LotAnnulationRecoursDto.to(lotDto);
		lot.setEcheance(echeanceEncours);
		lot.setTotalDossiers(total);
		lot.setStatut(reponseLot.getStatut());
		lot.setMotif(reponseLot.getMotif());
		lot.setTotalAcceptes(total - nbrRejet);
		lot.setTotalRejetes(nbrRejet);

//		lot.setDemandes(null);
		try {
			lot = lotAnnulationRecoursRepository.save(lot);
		} catch (Exception e) {
//			System.out.println(lot.getReferenceLot());
			e.printStackTrace();
			String motif = reponseLot.getMotif() == null ? "" : reponseLot.getMotif();
			reponseLot.setMotif(motif + "\nErreur Lors du sauvegarde du lot ref " + lotDto.getReferenceLot() + "\n"
					+ e.getMessage() + e.getStackTrace());
		}
		
		reponseLot.setTotalDossiers(total);
		reponseLot.setTotalAcceptes(total - nbrRejet);
		reponseLot.setTotalRejetes(nbrRejet);

		try {
			reponseLotDemandeRepository.save(ReponseLotDemandeDto.to(reponseLot));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return reponseLot;

	}

	public ReponseLotDemandeDto annulerLot(LotDemandeAnnuleDto lotDto, EcheanceEntity echeanceEncours) {
		if (lotDto == null)
			return null;

		saveJson(lotDto);

		Date d0 = new Date();
		ReponseLotDemandeDto reponseLot = controlerAnnulationLot(lotDto);
		Date d1 = new Date();
		System.out.println("Controle " + (d1.getTime() - d0.getTime()));

		Integer total = lotDto.getDemandes() == null ? 0 : lotDto.getDemandes().size();
		Integer nbrRejet = (reponseLot.getDemandesRejetees() == null ? 0 : reponseLot.getDemandesRejetees().size());
		if (ReponseLotDemandeDto.STATUT_REJETE.equals(reponseLot.getStatut())) {
			nbrRejet = total;
		}
		LotDemandeEntity lot = LotDemandeDto.to(lotDto);
		lot.setEcheance(echeanceEncours);
		List<DemandeEntity> demandes = lot.getDemandes();
		lot.setTotalDossiers(total);
		lot.setStatut(reponseLot.getStatut());
		lot.setMotif(reponseLot.getMotif());
		lot.setTotalAcceptes(total - nbrRejet);
		lot.setTotalRejetes(nbrRejet);

		lot.setDemandes(null);
		try {
			lot = lotRepository.save(lot);
		} catch (Exception e) {
//			System.out.println(lot.getReferenceLot());
			e.printStackTrace();
			String motif = reponseLot.getMotif() == null ? "" : reponseLot.getMotif();
			reponseLot.setMotif(motif + "\nErreur Lors du sauvegarde du lot ref " + lotDto.getReferenceLot() + "\n"
					+ e.getMessage() + e.getStackTrace());
		}
		List<DemandeRejeteeDto> demandesRejeteesLorsDuSauvegarde = new ArrayList<DemandeRejeteeDto>();
		for (DemandeEntity d : demandes) {
			try {
				if (ReponseLotDemandeDto.STATUT_REJETE.equals(reponseLot.getStatut())) {
					d.setStatut(DemandeDto.STATUT_REJETE);
					d.setMotifRejet(reponseLot.getMotif());
				} else {
					DemandeRejeteeDto demandeRejetee = DemandeRejeteeDto
							.getByReference(reponseLot.getDemandesRejetees(), d.getReferenceCnss());
					if (demandeRejetee == null)
						d.setStatut(DemandeDto.STATUT_RECEPTIONNE);
					else {
						d.setStatut(DemandeDto.STATUT_REJETE);
						if (demandeRejetee != null)
							d.setMotifRejet(demandeRejetee.getMotifDemande());
					}
				}
				d.setTypeDemande(DemandeDto.TYPE_ANNULATION);
//				d.setTypePrestation(lotDto.getPrestation());
				d.setLot(lot);
				demandeRepository.save(d);
			} catch (Exception e) {
				System.out.println("Probleme lors de la sauvegarde de la demande ref lot " + lot.getReferenceLot()
						+ " ref demande : " + d.getReferenceCnss());
				e.printStackTrace();
				demandesRejeteesLorsDuSauvegarde.add(getDemandeRejetee(d,
						"Probleme lors de la sauvegarde de la demande ref : " + d.getReferenceCnss()));
			}

		}

		reponseLot.setTotalDossiers(total);
		reponseLot.setTotalAcceptes(total - nbrRejet);
		reponseLot.setTotalRejetes(nbrRejet);

		try {
			reponseLotDemandeRepository.save(ReponseLotDemandeDto.to(reponseLot));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return reponseLot;

	}

	public ReponseLotDemandeDto recours(LotRecoursDto lotDto, EcheanceEntity echeanceEncours) {
		if (lotDto == null)
			return null;

		saveJson(lotDto);

		Date d0 = new Date();
		ReponseLotDemandeDto reponseLot = controlerLotRecours(lotDto);
		Date d1 = new Date();
		System.out.println("Controle " + (d1.getTime() - d0.getTime()));

		Integer total = lotDto.getDemandes() == null ? 0 : lotDto.getDemandes().size();
		Integer nbrRejet = (reponseLot.getDemandesRejetees() == null ? 0 : reponseLot.getDemandesRejetees().size());
		if (ReponseLotDemandeDto.STATUT_REJETE.equals(reponseLot.getStatut())) {
			nbrRejet = total;
		}

		LotRecoursEntity lot = LotRecoursDto.to(lotDto);
		lot.setEcheance(echeanceEncours);
		List<RecoursEntity> recours = lot.getRecours();
		lot.setTotalDossiers(total);
		lot.setStatut(reponseLot.getStatut());
		lot.setMotif(reponseLot.getMotif());
		lot.setTotalAcceptes(total - nbrRejet);
		lot.setTotalRejetes(nbrRejet);

		lot.setRecours(null);
		try {
			lot = lotRecoursRepository.save(lot);
		} catch (Exception e) {
//			System.out.println(lot.getReferenceLot());
			e.printStackTrace();
			String motif = reponseLot.getMotif() == null ? "" : reponseLot.getMotif();
			reponseLot.setMotif(motif + "\nErreur Lors du sauvegarde du lot ref " + lotDto.getReferenceLot() + "\n"
					+ e.getMessage() + e.getStackTrace());
		}
		List<DemandeRejeteeDto> demandesRejeteesLorsDuSauvegarde = new ArrayList<DemandeRejeteeDto>();
		for (RecoursEntity d : recours) {
			try {
				if (ReponseLotDemandeDto.STATUT_REJETE.equals(reponseLot.getStatut())) {
					d.setStatut(DemandeDto.STATUT_REJETE);
					d.setMotifRejet(reponseLot.getMotif());
				} else {
					DemandeRejeteeDto demandeRejetee = DemandeRejeteeDto
							.getByReference(reponseLot.getDemandesRejetees(), d.getReference());
					if (demandeRejetee == null)
						d.setStatut(DemandeDto.STATUT_RECEPTIONNE);
					else {
						d.setStatut(DemandeDto.STATUT_REJETE);
						if (demandeRejetee != null)
							d.setMotifRejet(demandeRejetee.getMotifDemande());
					}
				}
//				d.setTypePrestation(lotDto.getPrestation());
				d.setLot(lot);
				recoursRepository.save(d);
			} catch (Exception e) {
				System.out.println("Probleme lors de la sauvegarde de la demande ref lot " + lot.getReferenceLot()
						+ " ref demande : " + d.getReference());
				e.printStackTrace();
				demandesRejeteesLorsDuSauvegarde.add(
						getDemandeRejetee(d, "Probleme lors de la sauvegarde de la demande ref : " + d.getReference()));
			}

		}

		reponseLot.setTotalDossiers(total);
		reponseLot.setTotalAcceptes(total - nbrRejet);
		reponseLot.setTotalRejetes(nbrRejet);

		try {
			reponseLotDemandeRepository.save(ReponseLotDemandeDto.to(reponseLot));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return reponseLot;

	}

	public ReponseLotDemandeDto ajoutMembre(LotAjoutMembreDto lotDto, EcheanceEntity echeanceEncours) {
		if (lotDto == null)
			return null;

		saveJson(lotDto);

		Date d0 = new Date();
		ReponseLotDemandeDto reponseLot = controlerLotAjoutMembre(lotDto);
		Date d1 = new Date();
		System.out.println("Controle " + (d1.getTime() - d0.getTime()));

		Integer total = lotDto.getDemandes() == null ? 0 : lotDto.getDemandes().size();
		Integer nbrRejet = (reponseLot.getDemandesRejetees() == null ? 0 : reponseLot.getDemandesRejetees().size());
		if (ReponseLotDemandeDto.STATUT_REJETE.equals(reponseLot.getStatut())) {
			nbrRejet = total;
		}

		LotAjoutMembreEntity lot = LotAjoutMembreDto.to(lotDto);
		lot.setEcheance(echeanceEncours);
		List<DemandeAjoutMembreEntity> demandes = lot.getDemandes();
		lot.setTotalDossiers(total);
		lot.setStatut(reponseLot.getStatut());
		lot.setMotif(reponseLot.getMotif());
		lot.setTotalAcceptes(total - nbrRejet);
		lot.setTotalRejetes(nbrRejet);

		lot.setDemandes(null);
		try {
			lot = lotAjoutMembreRepository.save(lot);
		} catch (Exception e) {
//			System.out.println(lot.getReferenceLot());
			e.printStackTrace();
			String motif = reponseLot.getMotif() == null ? "" : reponseLot.getMotif();
			reponseLot.setMotif(motif + "\nErreur Lors du sauvegarde du lot ref " + lotDto.getReferenceLot() + "\n"
					+ e.getMessage() + e.getStackTrace());
		}
		List<DemandeRejeteeDto> demandesRejeteesLorsDuSauvegarde = new ArrayList<DemandeRejeteeDto>();
		for (DemandeAjoutMembreEntity d : demandes) {
			try {
				if (ReponseLotDemandeDto.STATUT_REJETE.equals(reponseLot.getStatut())) {
					d.setStatut(DemandeDto.STATUT_REJETE);
					d.setMotifRejet(reponseLot.getMotif());
				} else {
					DemandeRejeteeDto demandeRejetee = DemandeRejeteeDto
							.getByReference(reponseLot.getDemandesRejetees(), d.getReferenceDemande());
					if (demandeRejetee == null)
						d.setStatut(DemandeDto.STATUT_RECEPTIONNE);
					else {
						d.setStatut(DemandeDto.STATUT_REJETE);
						if (demandeRejetee != null)
							d.setMotifRejet(demandeRejetee.getMotifDemande());
					}
				}
//				d.setTypePrestation(lotDto.getPrestation());
				d.setLot(lot);
				demandeAjoutMemebreRepository.save(d);
			} catch (Exception e) {
				System.out.println("Probleme lors de la sauvegarde de la demande ref lot " + lot.getReferenceLot()
						+ " ref demande : " + d.getReferenceCnss());
				e.printStackTrace();
				demandesRejeteesLorsDuSauvegarde.add(getDemandeRejete(d,
						"Probleme lors de la sauvegarde de la demande ref : " + d.getReferenceCnss()));
			}

		}

		reponseLot.setTotalDossiers(total);
		reponseLot.setTotalAcceptes(total - nbrRejet);
		reponseLot.setTotalRejetes(nbrRejet);

		if (reponseLot.getTotalAcceptes() > 0 && reponseLot.getTotalAcceptes() == reponseLot.getTotalDossiers())
			reponseLot.setStatut("ACCEPTE");
		if (reponseLot.getTotalAcceptes() > 0 && reponseLot.getTotalAcceptes() < reponseLot.getTotalDossiers())
			reponseLot.setStatut("ACCEPTE PARTIELLEMENT");
		if (reponseLot.getTotalAcceptes() == 0)
			reponseLot.setStatut("REJETE");

		try {
			reponseLotDemandeRepository.save(ReponseLotDemandeDto.to(reponseLot));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return reponseLot;

	}

	private ReponseLotDemandeDto controlerAnnulationRecoursLot(LotAnnulationRecoursDto lotDto) {
		if (lotDto == null || lotDto.getDemandes() == null || lotDto.getDemandes().size() == 0)
			return null;
		ReponseLotDemandeDto responseLot = new ReponseLotDemandeDto(lotDto);
		return responseLot;

	}
	
	private ReponseLotDemandeDto controlerAnnulationLot(LotDemandeAnnuleDto lotDto) {
		if (lotDto == null || lotDto.getDemandes() == null || lotDto.getDemandes().size() == 0)
			return null;
		List<DemandeRejeteeDto> demandesRejetees = new ArrayList<DemandeRejeteeDto>();
		List<DemandeRejeteeDto> demandesRejeteestmp = new ArrayList<DemandeRejeteeDto>();
		ReponseLotDemandeDto responseLot = new ReponseLotDemandeDto(lotDto);
		String controleReferenceLot = controleLot(lotDto.getReferenceLot(), lotDto.getDateLot(), null, true);
		if (controleReferenceLot != null) {
			responseLot.setStatut(ReponseLotDemandeDto.STATUT_REJETE);
			responseLot.setMotif(controleReferenceLot);
		}

//		demandesRejeteestmp = controlerDateAnnulation(lotDto.getDemandes());
//		if (demandesRejeteestmp != null && demandesRejeteestmp.size() > 0)
//			demandesRejetees.addAll(demandesRejeteestmp);
		for (DemandeAnnuleeDto demande : lotDto.getDemandes()) {
			if (demande.getReferenceCnss() == null || demande.getReferenceCnss().isEmpty())
				demandesRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_REFERENCE_OBLIGATOIRE));
			if (demande.getIdcs() == null || demande.getIdcs().isEmpty())
				demandesRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_IDCS_DEMANDEUR_OBLIGATOIRE));
		}

		demandesRejeteestmp = controlerReferenceDemandeExite(lotDto.getDemandes());
		if (demandesRejeteestmp != null && demandesRejeteestmp.size() > 0)
			demandesRejetees.addAll(demandesRejeteestmp);

//		demandesRejeteestmp = controlerIdcsExite(lotDto.getDemandes());
//		if (demandesRejeteestmp != null && demandesRejeteestmp.size() > 0)
//			demandesRejetees.addAll(demandesRejeteestmp);

//		demandesRejeteestmp = controlerImmatriculationOuverte(lotDto.getDemandes());
//		if (demandesRejeteestmp != null && demandesRejeteestmp.size() > 0)
//			demandesRejetees.addAll(demandesRejeteestmp);
		demandesRejetees = regrouperDemandeRejetParRefCnss(demandesRejetees);
		responseLot.setDemandesRejetees(demandesRejetees);
		return responseLot;

	}

	private ReponseLotDemandeDto controlerLotRecours(LotRecoursDto lotDto) {
		if (lotDto == null || lotDto.getDemandes() == null || lotDto.getDemandes().size() == 0)
			return null;
		List<DemandeRejeteeDto> demandesRejetees = new ArrayList<DemandeRejeteeDto>();
		List<DemandeRejeteeDto> demandesRejeteestmp = new ArrayList<DemandeRejeteeDto>();
		ReponseLotDemandeDto responseLot = new ReponseLotDemandeDto(lotDto);
		String controleReferenceLot = controleLotRecous(lotDto.getReferenceLot(), lotDto.getDateLot(), null, true);
		if (controleReferenceLot != null) {
			responseLot.setStatut(ReponseLotDemandeDto.STATUT_REJETE);
			responseLot.setMotif(controleReferenceLot);
		}
/*
//		demandesRejeteestmp = controlerDateAnnulation(lotDto.getDemandes());
//		if (demandesRejeteestmp != null && demandesRejeteestmp.size() > 0)
//			demandesRejetees.addAll(demandesRejeteestmp);
		for (DemandeRecoursDto demande : lotDto.getDemandes()) {
			if (demande.getReferenceCnss() == null || demande.getReferenceCnss().isEmpty())
				demandesRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_REFERENCE_OBLIGATOIRE));
			if (demande.getIdcs() == null || demande.getIdcs().isEmpty())
				demandesRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_IDCS_DEMANDEUR_OBLIGATOIRE));
		}

		demandesRejeteestmp = controlerIdcsExite(lotDto.getDemandes());
		if (demandesRejeteestmp != null && demandesRejeteestmp.size() > 0)
			demandesRejetees.addAll(demandesRejeteestmp);

		demandesRejeteestmp = controlerDroitZeroExite(lotDto.getDemandes());
		if (demandesRejeteestmp != null && demandesRejeteestmp.size() > 0)
			demandesRejetees.addAll(demandesRejeteestmp);
*/
//		demandesRejeteestmp = controlerImmatriculationOuverte(lotDto.getDemandes());
//		if (demandesRejeteestmp != null && demandesRejeteestmp.size() > 0)
//			demandesRejetees.addAll(demandesRejeteestmp);
		demandesRejetees = regrouperDemandeRejetParRefCnss(demandesRejetees);
		responseLot.setDemandesRejetees(demandesRejetees);
		return responseLot;

	}

	private ReponseLotDemandeDto controlerLotAjoutMembre(LotAjoutMembreDto lotDto) {
		if (lotDto == null || lotDto.getDemandes() == null || lotDto.getDemandes().size() == 0)
			return null;
		List<DemandeRejeteeDto> demandesRejetees = new ArrayList<DemandeRejeteeDto>();
		List<DemandeRejeteeDto> demandesRejeteestmp = new ArrayList<DemandeRejeteeDto>();
		ReponseLotDemandeDto responseLot = new ReponseLotDemandeDto(lotDto);
		String controleReferenceLot = controleLotAjoutMembre(lotDto.getReferenceLot(), lotDto.getDateLot(), null, true);
		if (controleReferenceLot != null) {
			responseLot.setStatut(ReponseLotDemandeDto.STATUT_REJETE);
			responseLot.setMotif(controleReferenceLot);
		}

//		demandesRejeteestmp = controlerDateAnnulation(lotDto.getDemandes());
//		if (demandesRejeteestmp != null && demandesRejeteestmp.size() > 0)
//			demandesRejetees.addAll(demandesRejeteestmp);
		for (DemandeAjoutMembreDto demande : lotDto.getDemandes()) {
			if (demande.getReferenceCnss() == null || demande.getReferenceCnss().isEmpty())
				demandesRejetees.add(getDemandeRejeteeAjoutDemande(demande, DemandeRejeteeDto.MESSAGE_REFERENCE_OBLIGATOIRE));
			if (demande.getIdcsDemandeur() == null || demande.getIdcsDemandeur().isEmpty())
				demandesRejetees.add(getDemandeRejeteeAjoutDemande(demande, DemandeRejeteeDto.MESSAGE_IDCS_DEMANDEUR_OBLIGATOIRE));
		}

		demandesRejeteestmp = controlerAjoutMembre(lotDto.getDemandes());
		if (demandesRejeteestmp != null && demandesRejeteestmp.size() > 0)
			demandesRejetees.addAll(demandesRejeteestmp);

		demandesRejetees = regrouperDemandeRejetParRefCnss(demandesRejetees);
		responseLot.setDemandesRejetees(demandesRejetees);
		return responseLot;

	}

	public ReponseLotDemandeDto saveLot(LotDemandeDto lotDto, EcheanceEntity echeanceEncours) {
		if (lotDto == null)
			return null;

		saveJson(lotDto);

		Date d0 = new Date();
		ReponseLotDemandeDto reponseLot = controlerLot(lotDto);
		Date d1 = new Date();
		System.out.println("Controle " + (d1.getTime() - d0.getTime()));

		Integer total = lotDto.getDemandes() == null ? 0 : lotDto.getDemandes().size();
		Integer nbrRejet = (reponseLot.getDemandesRejetees() == null ? 0 : reponseLot.getDemandesRejetees().size());
		if (ReponseLotDemandeDto.STATUT_REJETE.equals(reponseLot.getStatut())) {
			nbrRejet = total;
		}

		LotDemandeEntity lot = LotDemandeDto.to(lotDto);
		lot.setEcheance(echeanceEncours);
		List<DemandeEntity> demandes = lot.getDemandes();

		lot.setTotalDossiers(total);
		lot.setStatut(reponseLot.getStatut());
		lot.setMotif(reponseLot.getMotif());
		lot.setTotalAcceptes(total - nbrRejet);
		lot.setTotalRejetes(nbrRejet);

		lot.setDemandes(null);
		try {
			lot = lotRepository.save(lot);
		} catch (Exception e) {
//			System.out.println(lot.getReferenceLot());
			e.printStackTrace();
			String motif = reponseLot.getMotif() == null ? "" : reponseLot.getMotif();
			reponseLot.setMotif(motif + "\nErreur Lors du sauvegarde du lot ref " + lotDto.getReferenceLot() + "\n"
					+ e.getMessage() + e.getStackTrace());
		}
		List<DemandeRejeteeDto> demandesRejeteesLorsDuSauvegarde = new ArrayList<DemandeRejeteeDto>();
		for (DemandeEntity d : demandes) {
			try {
				if (ReponseLotDemandeDto.STATUT_REJETE.equals(reponseLot.getStatut())) {
					d.setStatut(DemandeDto.STATUT_REJETE);
					d.setMotifRejet(reponseLot.getMotif());
				} else {
					DemandeRejeteeDto demandeRejetee = DemandeRejeteeDto
							.getByReference(reponseLot.getDemandesRejetees(), d.getReferenceCnss());
					if (demandeRejetee == null)
						d.setStatut(DemandeDto.STATUT_RECEPTIONNE);
					else {
						d.setStatut(DemandeDto.STATUT_REJETE);
						if (demandeRejetee != null)
							d.setMotifRejet(demandeRejetee.getMotifDemande());
					}
				}
				d.setTypeDemande(DemandeDto.TYPE_NOUVELLE);
				d.setTypePrestation(lotDto.getPrestation());
				d.setLot(lot);
				demandeRepository.save(d);
			} catch (Exception e) {
				System.out.println("Probleme lors de la sauvegarde de la demande ref lot " + lot.getReferenceLot()
						+ " ref demande : " + d.getReferenceCnss());
				e.printStackTrace();
				demandesRejeteesLorsDuSauvegarde.add(getDemandeRejetee(d,
						"Probleme lors de la sauvegarde de la demande ref : " + d.getReferenceCnss()));
			}

		}

		reponseLot.setTotalDossiers(total);
		reponseLot.setTotalAcceptes(total - nbrRejet);
		reponseLot.setTotalRejetes(nbrRejet);

		try {
			reponseLotDemandeRepository.save(ReponseLotDemandeDto.to(reponseLot));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return reponseLot;
	}

	private void saveJson(LotAnnulationRecoursDto lotDto) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String json = objectMapper.writeValueAsString(lotDto);
			lotDemandeBruteRepository.save(new LotDemandeBruteEntity(lotDto.getReferenceLot(),
					Utils.stringToDate(lotDto.getDateLot(), Utils.FOURMAT_DATE_STRING), json));
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private void saveJson(LotDemandeDto lotDto) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String json = objectMapper.writeValueAsString(lotDto);
			lotDemandeBruteRepository.save(new LotDemandeBruteEntity(lotDto.getReferenceLot(),
					Utils.stringToDate(lotDto.getDateLot(), Utils.FOURMAT_DATE_STRING), json));
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void saveJson(LotAjoutMembreDto lotDto) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String json = objectMapper.writeValueAsString(lotDto);
			lotDemandeBruteRepository.save(new LotDemandeBruteEntity(lotDto.getReferenceLot(),
					Utils.stringToDate(lotDto.getDateLot(), Utils.FOURMAT_DATE_STRING), json));
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void saveJson(LotRecoursDto lotDto) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String json = objectMapper.writeValueAsString(lotDto);
			lotDemandeBruteRepository.save(new LotDemandeBruteEntity(lotDto.getReferenceLot(),
					Utils.stringToDate(lotDto.getDateLot(), Utils.FOURMAT_DATE_STRING), json));
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void saveJson(LotDemandeAnnuleDto lotDto) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String json = objectMapper.writeValueAsString(lotDto);
			lotDemandeBruteRepository.save(new LotDemandeBruteEntity(lotDto.getReferenceLot(),
					Utils.stringToDate(lotDto.getDateLot(), Utils.FOURMAT_DATE_STRING), json));
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private ReponseLotDemandeDto controlerLot(LotDemandeDto lotDto) {
		if (lotDto == null || lotDto.getDemandes() == null || lotDto.getDemandes().size() == 0)
			return null;
		ReponseLotDemandeDto responseLot = new ReponseLotDemandeDto(lotDto);
		List<DemandeRejeteeDto> demandesRejetees = new ArrayList<DemandeRejeteeDto>();
		try {

			List<DemandeRejeteeDto> demandesRejeteestmp = new ArrayList<DemandeRejeteeDto>();
			String controleReferenceLot = controleLot(lotDto.getReferenceLot(), lotDto.getDateLot(),
					lotDto.getPrestation(), false);

			if (controleReferenceLot != null) {
				responseLot.setStatut(ReponseLotDemandeDto.STATUT_REJETE);
				responseLot.setMotif(controleReferenceLot);
			}

			demandesRejeteestmp = controlerChampsObligatoire(lotDto.getDemandes(), lotDto.getPrestation());
			if (demandesRejeteestmp != null && demandesRejeteestmp.size() > 0)
				demandesRejetees.addAll(demandesRejeteestmp);

			demandesRejeteestmp = controlerReferenceDemande(lotDto.getDemandes(), lotDto.getPrestation());
			if (demandesRejeteestmp != null && demandesRejeteestmp.size() > 0)
				demandesRejetees.addAll(demandesRejeteestmp);

			if (!DemandeDto.CODE_PRESTATION_PRIME_NAISSANCE.equals(lotDto.getPrestation())) {
				demandesRejeteestmp = controlerDossier(lotDto.getDemandes());
				if (demandesRejeteestmp != null && demandesRejeteestmp.size() > 0)
					demandesRejetees.addAll(demandesRejeteestmp);

				/*
				 * demandesRejeteestmp = controlerMembres(lotDto.getDemandes()); if
				 * (demandesRejeteestmp != null && demandesRejeteestmp.size() > 0)
				 * demandesRejetees.addAll(demandesRejeteestmp);
				 */
			}
			demandesRejetees = regrouperDemandeRejetParRefCnss(demandesRejetees);

		} catch (Exception e) {
			System.out.println(lotDto.getReferenceLot());
			e.printStackTrace();
			responseLot.setStatut(ReponseLotDemandeDto.STATUT_REJETE);
			responseLot.setMotif("Erreur lors du controle");
		}
		responseLot.setDemandesRejetees(demandesRejetees);
		return responseLot;

	}

	private List<DemandeRejeteeDto> regrouperDemandeRejetParRefCnss(List<DemandeRejeteeDto> demandesRejetees) {
		if (demandesRejetees == null || demandesRejetees.size() == 0)
			return demandesRejetees;
		Map<String, DemandeRejeteeDto> map = new HashMap<String, DemandeRejeteeDto>();
		for (DemandeRejeteeDto demande : demandesRejetees) {
			if (map.containsKey(demande.getReferenceCnss())) {
				DemandeRejeteeDto d = map.get(demande.getReferenceCnss());
				if (!d.getMotifDemande().contains(demande.getMotifDemande()))
					map.get(demande.getReferenceCnss())
							.setMotifDemande(d.getMotifDemande() + "; " + demande.getMotifDemande());
			} else {
				map.put(demande.getReferenceCnss(), demande);
			}
		}
		List<DemandeRejeteeDto> result = new ArrayList<DemandeRejeteeDto>();
		result.addAll(map.values());
		return result;
	}

	private List<DemandeRejeteeDto> controlerReferenceDemande(List<DemandeDto> demandes, String prestation) {
		List<String> listReference = new ArrayList<String>();
		List<DemandeRejeteeDto> demandeRejetees = new ArrayList<DemandeRejeteeDto>();
		if (demandes != null && demandes.size() > 0) {
			listReference = DemandeDto.getReferenceDemandes(demandes);
			List<String> demandesExistes = demandeRepository.getExistReferenceCnssAndPrestation(listReference,
					prestation);

			if (demandes != null && demandes.size() > 0) {
				for (String ref : demandesExistes) {
					demandeRejetees.add(getDemandeRejetee(DemandeDto.getByReference(demandes, ref),
							DemandeRejeteeDto.MESSAGE_REFERENCE_DEMANDE_EXISTE));
				}
			}

		}

		return demandeRejetees;
	}

	private List<DemandeRejeteeDto> controlerReferenceDemandeExite(List<DemandeAnnuleeDto> demandes) {
		if (demandes == null || demandes.size() == 0)
			return null;
		List<DemandeRejeteeDto> demandeRejetees = new ArrayList<DemandeRejeteeDto>();

		List<String> referenceDemandes = DemandeAnnuleeDto.getReferenceDemandes(demandes);
		List<String> referenceDemandesTrouvees = demandeRepository.getExistReferenceCnss(referenceDemandes);
		for (DemandeAnnuleeDto d : demandes) {
			if (!referenceDemandesTrouvees.contains(d.getReferenceCnss())) {
				demandeRejetees.add(getDemandeRejetee(DemandeAnnuleeDto.getByReference(demandes, d.getReferenceCnss()),
						DemandeRejeteeDto.MESSAGE_REFERENCE_NON_LIE_DOSSIER_OUVERT));

			}
		}
		return demandeRejetees;
	}

	private List<DemandeRejeteeDto> controlerIdcsExite(List<DemandeRecoursDto> demandes) {
		if (demandes == null || demandes.size() == 0)
			return null;
		List<DemandeRejeteeDto> demandeRejetees = new ArrayList<DemandeRejeteeDto>();
		List<String> idcs = DemandeRecoursDto.getIdcsDemandes(demandes);
		List<String> listIdcs = dossierAsdRepository.getExistIdcsDemandeur(idcs);
		for (DemandeRecoursDto d : demandes) {
			if (!listIdcs.contains(d.getIdcs())) {
				demandeRejetees.add(getDemandeRejetee(DemandeRecoursDto.getByIdcs(demandes, d.getIdcs()),
						DemandeRejeteeDto.MESSAGE_IDCS_CORRESPOND_PAS_A_CELUI_DEMANDEUR));
			}
		}
		return demandeRejetees;
	}

	private List<DemandeRejeteeDto> controlerIdcsDemandeursExite(List<DemandeAjoutMembreDto> demandes) {
		if (demandes == null || demandes.size() == 0)
			return null;
		List<DemandeRejeteeDto> demandeRejetees = new ArrayList<DemandeRejeteeDto>();
		List<String> idcs = DemandeRecoursDto.getIdcsDemandeurs(demandes);
		List<String> listIdcs = dossierAsdRepository.getExistIdcsDemandeur(idcs);
		for (DemandeAjoutMembreDto d : demandes) {
			if (!listIdcs.contains(d.getIdcsDemandeur())) {
				demandeRejetees
						.add(getDemandeRejetee(DemandeRecoursDto.getByIdcsDemandeur(demandes, d.getIdcsDemandeur()),
								DemandeRejeteeDto.MESSAGE_IDCS_CORRESPOND_PAS_A_CELUI_DEMANDEUR));
			}
		}
		return demandeRejetees;
	}

	private List<DemandeRejeteeDto> controlerDroitZeroExite(List<DemandeRecoursDto> demandes) {
		if (demandes == null || demandes.size() == 0)
			return null;
		List<DemandeRejeteeDto> demandeRejetees = new ArrayList<DemandeRejeteeDto>();
		List<String> references = DemandeRecoursDto.getReferenceDemandes(demandes);
		List<RefIdcsDossier> refIdcs = dossierAsdRepository.getDossiersDroitZero(references);
		for (DemandeRecoursDto d : demandes) {
			if (refIdcs == null || refIdcs.size() == 0)
				demandeRejetees.add(getDemandeRejetee(DemandeRecoursDto.getByIdcs(demandes, d.getIdcs()),
						DemandeRejeteeDto.MESSAGE_RD31));
			else {
				boolean exist = false;
				boolean demandeRejete = false;
				for (RefIdcsDossier refIdcsDossier : refIdcs) {
					if (d.getReferenceCnss().equals(refIdcsDossier.getReference())) {
						if (!d.getIdcs().equals(refIdcsDossier.getIdcs())) {
							demandeRejetees.add(getDemandeRejetee(DemandeRecoursDto.getByIdcs(demandes, d.getIdcs()),
									DemandeRejeteeDto.MESSAGE_RD32));
							demandeRejete = true;
							break;
						} else {
							exist = true;
						}
					}
				}
				if (!exist && !demandeRejete)
					demandeRejetees.add(getDemandeRejetee(DemandeRecoursDto.getByIdcs(demandes, d.getIdcs()),
							DemandeRejeteeDto.MESSAGE_RD31));
			}
		}
		return demandeRejetees;
	}

	private List<DemandeRejeteeDto> controlerAjoutMembre(List<DemandeAjoutMembreDto> demandes) { if (demandes == null || demandes.size() == 0)
			return null;
		List<DemandeRejeteeDto> demandeRejetees = new ArrayList<DemandeRejeteeDto>();

		List<String> references = DemandeAjoutMembreDto.getReferenceDemandes(demandes);
		List<InfoControlAjoutMembre> infoControle = dossierAsdRepository.getInfoControlAjoutMembre(references);

		for (DemandeAjoutMembreDto d : demandes) {
			if (infoControle == null || infoControle.size() == 0)
				demandeRejetees.add(getDemandeRejeteeAjoutDemande(DemandeAjoutMembreDto.getByReferenceDemande(demandes, d.getReferenceDemande()),
						DemandeRejeteeDto.MESSAGE_RD61));
			else {
				boolean exist = false;
				boolean demandeRejete = false;
				for (InfoControlAjoutMembre info : infoControle) {
					Set<String> listIdcs = new HashSet<String>();
					if (d.getReferenceCnss().equals(info.getReference())) {
						controlerEnfant(demandeRejetees, d, info, listIdcs);
						controlerConjoint(demandeRejetees, d, info, listIdcs);
						if (!d.getIdcsDemandeur().equals(info.getIdcs())) {
							demandeRejetees.add(
									getDemandeRejeteeAjoutDemande(DemandeAjoutMembreDto.getByIdcs(demandes, d.getIdcsDemandeur()),
											DemandeRejeteeDto.MESSAGE_RD60));
							demandeRejete = true;
							break;
						} else if (!DemandeAjoutMembreDto.existeMembre(d)) {
							demandeRejetees.add(
									getDemandeRejeteeAjoutDemande(DemandeAjoutMembreDto.getByReferenceDemande(demandes, d.getReferenceDemande()),
											DemandeRejeteeDto.MESSAGE_RD62));
							demandeRejete = true;
							break;
						} else if (DemandeDto.CODE_PRESTATION_AIDE_FAMILLE.equals(info.getPrestation())
								&& DemandeAjoutMembreDto.existeMembreEnfant(d)) {
							demandeRejetees.add(
									getDemandeRejeteeAjoutDemande(DemandeAjoutMembreDto.getByReferenceDemande(demandes, d.getReferenceDemande()),
											DemandeRejeteeDto.MESSAGE_RD63));
							demandeRejete = true;
							break;
						} else if ("F".equals(info.getGenre()) && info.getIdcsconjoint() != null
								&& !info.getIdcsconjoint().isEmpty() && d.getConjoints() != null && d.getConjoints().size() > 0) {
							demandeRejetees.add(
									getDemandeRejeteeAjoutDemande(DemandeAjoutMembreDto.getByReferenceDemande(demandes, d.getReferenceDemande()),
											DemandeRejeteeDto.MESSAGE_RD64));
							demandeRejete = true;
							break;
						} else {
							exist = true;
						}
					}
				}
				if (!exist && !demandeRejete)
					demandeRejetees
							.add(getDemandeRejeteeAjoutDemande(DemandeAjoutMembreDto.getByReferenceDemande(demandes, d.getReferenceDemande()),
									DemandeRejeteeDto.MESSAGE_RD60));
			}
		}
		return demandeRejetees;
	}

	private void controlerConjoint(List<DemandeRejeteeDto> demandeRejetees, DemandeAjoutMembreDto demande,
			InfoControlAjoutMembre info, Set<String> listIdcs) {
		if (demande != null && demande.getConjoints() != null && demande.getConjoints().size() > 0)
			for (ConjointDto conjont : demande.getConjoints()) {
				if ("M".equals(info.getEtatmatrimonial()) && conjont != null
						&& (conjont.getIdcs() == null || conjont.getIdcs().isEmpty()))
					demandeRejetees.add(getDemandeRejeteeAjoutDemande(demande, DemandeRejeteeDto.MESSAGE_IDCS_MEMBRE_OBLIGATOIRE));

				if (info.getIdcsconjoints() != null && conjont != null
						&& info.getIdcsconjoints().contains(";" + conjont.getIdcs() + ";"))
					demandeRejetees.add(getDemandeRejeteeAjoutDemande(demande, DemandeRejeteeDto.MESSAGE_RD66));

				if (conjont != null && (conjont.getCin() == null || conjont.getCin().isEmpty()))
					demandeRejetees.add(getDemandeRejeteeAjoutDemande(demande, DemandeRejeteeDto.MESSAGE_CIN_OBLIGATOIRE));

				if (conjont != null && (conjont.getDateNaissance() == null || conjont.getDateNaissance().isEmpty()))
					demandeRejetees.add(
							getDemandeRejeteeAjoutDemande(demande, DemandeRejeteeDto.MESSAGE_DATE_NAISSANCE_MEMBRE_OBLIGATOIRE));

				if (conjont != null && conjont.getDateNaissance() != null
						&& !Utils.isDate(conjont.getDateNaissance(), Utils.FOURMAT_DATE_STRING))
					demandeRejetees.add(getDemandeRejeteeAjoutDemande(demande, DemandeRejeteeDto.MESSAGE_DATE_FORMAT_ERRONEE));

				
				if ("M".equals(info.getEtatmatrimonial()) && conjont != null
						&& (conjont.getCodeMenageRsu() == null || conjont.getCodeMenageRsu().isEmpty()))
					demandeRejetees.add(
							getDemandeRejeteeAjoutDemande(demande, DemandeRejeteeDto.MESSAGE_CODE_MENAGE_RSU_N_EST_PAS_RENSEIGNE));
				else if (conjont != null && "M".equals(info.getEtatmatrimonial()) && info.getCodemenage() != null
						&& !info.getCodemenage().equals(conjont.getCodeMenageRsu())
						&& !info.getCodemenage().contains(";" + conjont.getCodeMenageRsu())
						&& !info.getCodemenage().contains(conjont.getCodeMenageRsu() + ";"))
					demandeRejetees.add(
							getDemandeRejeteeAjoutDemande(demande, DemandeRejeteeDto.MESSAGE_MEMBRES_N_ONT_PAS_MEME_CODE_MENAGE));
				if (conjont != null && conjont.getIdcs() != null && !listIdcs.add(conjont.getIdcs())) {
					demandeRejetees
							.add(getDemandeRejeteeAjoutDemande(demande, DemandeRejeteeDto.MESSAGE_IDCS_DECLARE_PLUSIEURS_FOIS));
				}

			}
	}

	private void controlerEnfant(List<DemandeRejeteeDto> demandeRejetees, DemandeAjoutMembreDto demande,
			InfoControlAjoutMembre info, Set<String> listIdcs) {
		if (demande.getEnfants() != null && demande.getEnfants().size() > 0)
			for (EnfantDto enfant : demande.getEnfants()) {
				if (enfant != null) {
					if (enfant.getIdcs() == null || enfant.getIdcs().isEmpty())
						demandeRejetees
								.add(getDemandeRejeteeAjoutDemande(demande, DemandeRejeteeDto.MESSAGE_IDCS_MEMBRE_OBLIGATOIRE));
					if (info.getIdcsenfants() != null && info.getIdcsenfants().contains(";" + enfant.getIdcs() + ";"))
						demandeRejetees
								.add(getDemandeRejeteeAjoutDemande(demande, DemandeRejeteeDto.MESSAGE_RD65));
					if (enfant.getDateNaissance() == null || enfant.getDateNaissance().isEmpty())
						demandeRejetees.add(getDemandeRejeteeAjoutDemande(demande,
								DemandeRejeteeDto.MESSAGE_DATE_NAISSANCE_MEMBRE_OBLIGATOIRE));
					if (enfant.getDateNaissance() != null
							&& !Utils.isDate(enfant.getDateNaissance(), Utils.FOURMAT_DATE_STRING))
						demandeRejetees.add(getDemandeRejeteeAjoutDemande(demande, DemandeRejeteeDto.MESSAGE_DATE_FORMAT_ERRONEE));

					
					if (enfant.getCodeMenageRsu() == null || enfant.getCodeMenageRsu().isEmpty())
						demandeRejetees.add(getDemandeRejeteeAjoutDemande(demande,
								DemandeRejeteeDto.MESSAGE_CODE_MENAGE_RSU_N_EST_PAS_RENSEIGNE));
					else if (info.getCodemenage() != null && !info.getCodemenage().equals(enfant.getCodeMenageRsu())
							&& !info.getCodemenage().contains(";" + enfant.getCodeMenageRsu())
							&& !info.getCodemenage().contains(enfant.getCodeMenageRsu() + ";"))
						demandeRejetees.add(getDemandeRejeteeAjoutDemande(demande,
								DemandeRejeteeDto.MESSAGE_MEMBRES_N_ONT_PAS_MEME_CODE_MENAGE));
					if (enfant.getLienParente() == null || enfant.getLienParente().isEmpty())
						demandeRejetees.add(
								getDemandeRejeteeAjoutDemande(demande, DemandeRejeteeDto.MESSAGE_LIEN_PARENTE_N_EST_PAS_RENSEIGNE));

					if (enfant.getEstHandicape() == null || enfant.getEstHandicape().isEmpty()
							|| !Arrays.asList("1", "0", "oui", "non", "OUI", "NON").contains(enfant.getEstHandicape()))
						demandeRejetees.add(
								getDemandeRejeteeAjoutDemande(demande, DemandeRejeteeDto.MESSAGE_DONNEES_HANDICAP_NON_CONFORME));

					if (enfant.getEstOrphelinPere() == null || enfant.getEstOrphelinPere().isEmpty() || !Arrays
							.asList("1", "0", "oui", "non", "OUI", "NON").contains(enfant.getEstOrphelinPere()))
						demandeRejetees.add(
								getDemandeRejeteeAjoutDemande(demande, DemandeRejeteeDto.MESSAGE_DONNEES_ORPHELIN_NON_CONFORME));

					if (enfant.getTypeIdentifiantScolarite() != null
							&& !Arrays
									.asList(EnfantDto.TYPE_SCOLARITE_MASSAR, EnfantDto.TYPE_SCOLARITE_CEF,
											EnfantDto.TYPE_SCOLARITE_AUTRE)
									.contains(enfant.getTypeIdentifiantScolarite()))
						demandeRejetees.add(getDemandeRejeteeAjoutDemande(demande,
								DemandeRejeteeDto.MESSAGE_IDENTIFIANT_SCOLARITE_NON_CONFORME));

					if (enfant.getTypeIdentifiantScolarite() == null
							&& Arrays.asList(EnfantDto.TYPE_SCOLARITE_MASSAR, EnfantDto.TYPE_SCOLARITE_CEF)
									.contains(enfant.getTypeIdentifiantScolarite()))
						demandeRejetees.add(getDemandeRejeteeAjoutDemande(demande,
								DemandeRejeteeDto.MESSAGE_IDENTIFIANT_SCOLARITE_NON_CONFORME));

					if (enfant.getIdentifiantScolarite() != null
							&& EnfantDto.TYPE_SCOLARITE_AUTRE.equals(enfant.getTypeIdentifiantScolarite()))
						demandeRejetees.add(getDemandeRejeteeAjoutDemande(demande,
								DemandeRejeteeDto.MESSAGE_IDENTIFIANT_SCOLARITE_NON_CONFORME));
					if ("F".equals(enfant.getLienParente()) && "F".equals(info.getGenre()) && info.getCin() != null
							&& !info.getCin().equals(enfant.getCinMere()) && !"C".equals(info.getEtatmatrimonial())
							&& !"A".equals(info.getEtatmatrimonial()))
						demandeRejetees.add(getDemandeRejeteeAjoutDemande(demande,
								DemandeRejeteeDto.MESSAGE_INCOHERENCE_ENTRE_GENRE_DEMANDEUR_CIN_MERE_PERE));

					if ("F".equals(enfant.getLienParente()) && "M".equals(info.getGenre()) && info.getCin() != null
							&& !info.getCin().equals(enfant.getCinPere()) && !"C".equals(info.getEtatmatrimonial())
							&& !"A".equals(info.getEtatmatrimonial()))
						demandeRejetees.add(getDemandeRejeteeAjoutDemande(demande,
								DemandeRejeteeDto.MESSAGE_INCOHERENCE_ENTRE_GENRE_DEMANDEUR_CIN_MERE_PERE));

					if (EnfantDto.TYPE_SCOLARITE_MASSAR.equals(enfant.getTypeIdentifiantScolarite())
							&& !Utils.isNumeroMassarValide(enfant.getIdentifiantScolarite()))
						demandeRejetees.add(getDemandeRejeteeAjoutDemande(demande,
								DemandeRejeteeDto.MESSAGE_IDENTIFIANT_SCOLARITE_NON_CONFORME));

					if (enfant.getCinMere() != null && enfant.getCinMere().equals(info.getCin())
							&& "H".equals(info.getEtatmatrimonial()))
						demandeRejetees.add(getDemandeRejeteeAjoutDemande(demande,
								DemandeRejeteeDto.MESSAGE_CIN_MERE_EGALE_CIN_DEMANDEUR_GENRE_DEMANDEUR_H));

					if (enfant.getCinPere() != null && enfant.getCinPere().equals(info.getCin())
							&& "F".equals(info.getEtatmatrimonial()))
						demandeRejetees.add(getDemandeRejeteeAjoutDemande(demande,
								DemandeRejeteeDto.MESSAGE_CIN_MERE_EGALE_CIN_DEMANDEUR_GENRE_DEMANDEUR_H));

					if (enfant.getIdcs() != null && !listIdcs.add(enfant.getIdcs())) {
						demandeRejetees
								.add(getDemandeRejeteeAjoutDemande(demande, DemandeRejeteeDto.MESSAGE_IDCS_DECLARE_PLUSIEURS_FOIS));
					}

//					if(DemandeDto.CODE_PRESTATION_AIDE_FAMILLE.equals(prestation) && 
//							Utils.isDate(enfant.getDateNaissance(), Utils.FOURMAT_DATE_STRING) &&
//							Utils.isDate(demande.getDateDemande(), Utils.FOURMAT_DATE_STRING) &&
//							Utils.isAgeMoin21(enfant.getDateNaissance() ,demande.getDateDemande() ))
//						demandeRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_MEMBRE_AGE_MOINS_21));
				}
			}
	}

	private List<DemandeRejeteeDto> controlerMembres(List<DemandeDto> demandes) {
		if (demandes == null || demandes.size() == 0)
			return null;
		List<String> listIdcs = DemandeDto.getListIdcs(demandes);
		List<String> listIdcsMoin1000 = new ArrayList<String>();
		List<String> listExistIdcs = new ArrayList<String>();
		for (int i = 1; i <= listIdcs.size(); i++) {
			listIdcsMoin1000.add(listIdcs.get(i - 1));
			if (listIdcsMoin1000.size() == 1000 || i == listIdcs.size()) {
				List<String> tmpList = dossierAsdRepository.getExistIdcs(listIdcsMoin1000);
				if (tmpList != null && tmpList.size() > 0)
					listExistIdcs.addAll(tmpList);
				listIdcsMoin1000 = new ArrayList<String>();
			}
		}
		List<DemandeRejeteeDto> demandeRejetees = new ArrayList<DemandeRejeteeDto>();
		if (listExistIdcs != null && listExistIdcs.size() > 0) {
			List<DemandeDto> l = DemandeDto.getByIdcs(demandes, listExistIdcs);
			if (l != null && l.size() > 0)
				for (DemandeDto demandeDto : l) {
					demandeRejetees.add(getDemandeRejetee(demandeDto, DemandeRejeteeDto.MESSAGE_IDCS_EXIST));
				}

		}
		return demandeRejetees;
	}

	private List<DemandeRejeteeDto> controlerDossier(List<DemandeDto> demandes) {
		// Pas de dossier ouvert pour la même immatriculation
		List<String> listImmatricule = new ArrayList<String>();
		List<DemandeRejeteeDto> demandeRejetees = new ArrayList<DemandeRejeteeDto>();
		if (demandes != null && demandes.size() > 0) {
			for (DemandeDto demande : demandes) {
				if (demande != null)
					listImmatricule.add(demande.getImmatriculation());
			}
			List<DossierAsdEntity> dossiers = dossierAsdRepository
					.findByImmatriculationInAndDateClotureIsNull(listImmatricule);

			if (dossiers != null && dossiers.size() > 0) {
				for (DossierAsdEntity dossier : dossiers) {
					demandeRejetees.add(
							getDemandeRejetee(DemandeDto.getByImmatriculation(demandes, dossier.getImmatriculation()),
									DemandeRejeteeDto.MESSAGE_DOSSIER_OUVERT));
				}

			}

		}

		return demandeRejetees;
	}

//	private List<DemandeRejeteeDto> controlerImmatriculationOuverte(List<DemandeAnnuleeDto> demandes) {
//		List<String> listImmatricule = new ArrayList<String>();
//		List<DemandeRejeteeDto> demandeRejetees = new ArrayList<DemandeRejeteeDto>();
//		if (demandes != null && demandes.size() > 0) {
//			for (DemandeAnnuleeDto demande : demandes) {
//				listImmatricule.add(demande.getImmatriculation());
//			}
//			List<DossierAsdEntity> dossiers = dossierAsdRepository
//					.findByImmatriculationInAndDateClotureIsNull(listImmatricule);
//			if (dossiers.size() == demandes.size())
//				return null;
////			if (dossiers != null && dossiers.size() > 0) {
//			List<String> listImmatriculationExiste = DossierAsdEntity.getImmatriculations(dossiers);
//			for (DemandeAnnuleeDto demande : demandes) {
//				if (!listImmatriculationExiste.contains(demande.getImmatriculation()))
//					demandeRejetees
//							.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_DEMANDE_DOIT_ETRE_OUVERTE));
//			}
//
////			}
//
//		}

//		return demandeRejetees;
//	}

	private String controleLot(String reference, String dateLot, String prestation, boolean isAnnulationOrRecours) {
		String motif = "";
		if (reference == null || reference.isEmpty())
			motif = ReponseLotDemandeDto.MESSAGE_REFERENCE_LOT_NON_RENSEIGNEE + "; ";
//		La référence doit être inexistante 
		List<LotDemandeEntity> listLot = lotRepository.findByReferenceLot(reference);
		if (listLot != null && listLot.size() > 0)
			motif += ReponseLotDemandeDto.MESSAGE_REFERENCE_LOT_EXISTE + "; ";

		if (dateLot == null || dateLot.isEmpty())
			motif += ReponseLotDemandeDto.MESSAGE_DATE_LOT_NON_RENSEIGNEE + "; ";
		if (!Utils.isDate(dateLot, Utils.FOURMAT_DATE_STRING))
			motif += ReponseLotDemandeDto.MESSAGE_FORMAT_DATE_NON_CONFORME + "; ";
		if (!isAnnulationOrRecours
				&& !Arrays.asList(DemandeDto.CODE_PRESTATION_AIDE_ENFANCE, DemandeDto.CODE_PRESTATION_PRIME_NAISSANCE,
						DemandeDto.CODE_PRESTATION_AIDE_FAMILLE).contains(prestation)) {
			motif += ReponseLotDemandeDto.MESSAGE_CODE_PRESTATION_ERRONE + "; ";
		}
		return "".equals(motif) ? null : motif;
	}

	private String controleLotRecous(String reference, String dateLot, String prestation,
			boolean isAnnulationOrRecours) {
		String motif = "";
		if (reference == null || reference.isEmpty())
			motif = ReponseLotDemandeDto.MESSAGE_REFERENCE_LOT_NON_RENSEIGNEE + "; ";
//		La référence doit être inexistante 
		List<LotRecoursEntity> listLot = lotRecoursRepository.findByReferenceLot(reference);
		if (listLot != null && listLot.size() > 0)
			motif += ReponseLotDemandeDto.MESSAGE_REFERENCE_LOT_EXISTE + "; ";

		if (dateLot == null || dateLot.isEmpty())
			motif += ReponseLotDemandeDto.MESSAGE_DATE_LOT_NON_RENSEIGNEE + "; ";
		if (!Utils.isDate(dateLot, Utils.FOURMAT_DATE_STRING))
			motif += ReponseLotDemandeDto.MESSAGE_FORMAT_DATE_NON_CONFORME + "; ";
		return "".equals(motif) ? null : motif;
	}

	private String controleLotAjoutMembre(String reference, String dateLot, String prestation,
			boolean isAnnulationOrRecours) {
		String motif = "";
		if (reference == null || reference.isEmpty())
			motif = ReponseLotDemandeDto.MESSAGE_REFERENCE_LOT_NON_RENSEIGNEE + "; ";
//		La référence doit être inexistante 
		List<LotAjoutMembreEntity> listLot = lotAjoutMembreRepository.findByReferenceLot(reference);
		if (listLot != null && listLot.size() > 0)
			motif += ReponseLotDemandeDto.MESSAGE_REFERENCE_LOT_EXISTE + "; ";

		if (dateLot == null || dateLot.isEmpty())
			motif += ReponseLotDemandeDto.MESSAGE_DATE_LOT_NON_RENSEIGNEE + "; ";
		if (!Utils.isDate(dateLot, Utils.FOURMAT_DATE_STRING))
			motif += ReponseLotDemandeDto.MESSAGE_FORMAT_DATE_NON_CONFORME + "; ";
		return "".equals(motif) ? null : motif;
	}

//	private List<DemandeRejeteeDto> controlerDateAnnulation(List<DemandeAnnuleeDto> demandes) {
//		if (demandes == null)
//			return null;
//		List<DemandeRejeteeDto> demandeRejetees = new ArrayList<DemandeRejeteeDto>();
//		for (DemandeAnnuleeDto demande : demandes) {
//			if (demande.getDateAnnulation() == null || demande.getDateAnnulation().isEmpty())
//				demandeRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_DATE_ANNULATION_OBLIGATOIRE));
//		}
//		return demandeRejetees;
//	}

	private List<DemandeRejeteeDto> controlerChampsObligatoire(List<DemandeDto> demandes, String prestation) {
		// o Champs obligatoires : (référence, date demande, idcs des membres, date
		// naissance, type prestation)

		if (demandes == null)
			return null;
		List<DemandeRejeteeDto> demandeRejetees = new ArrayList<DemandeRejeteeDto>();
		for (DemandeDto demande : demandes) {
			if (demande != null) {
				List<DemandeRejeteeDto> rejet = null;
				try {
					rejet = controlerDemande(prestation, demande);
				} catch (Exception e) {
					e.printStackTrace();
					demandeRejetees.add(getDemandeRejetee(demande, e.getMessage()));
				}

				if (rejet != null && rejet.size() > 0)
					demandeRejetees.addAll(rejet);
			}
		}

		return demandeRejetees;
	}

	private List<DemandeRejeteeDto> controlerDemande(String prestation, DemandeDto demande) {
		List<DemandeRejeteeDto> demandeRejetees = new ArrayList<DemandeRejeteeDto>();
		Set<String> listIdcs = new HashSet<String>();
		boolean testIdcs = false;
		if (demande.getReferenceCnss() == null || demande.getReferenceCnss().isEmpty())
			demandeRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_REFERENCE_OBLIGATOIRE));
		if (demande.getDateDemande() == null || demande.getDateDemande().isEmpty())
			demandeRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_DATE_DEMANNDE_OBLIGATOIRE));
		if (demande.getDateDemande() != null && !Utils.isDate(demande.getDateDemande(), Utils.FOURMAT_DATE_STRING))
			demandeRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_DATE_FORMAT_ERRONEE));

		if (prestation == null || prestation.isEmpty())
			demandeRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_PRESTATION_OBLIGATOIRE));
		if (demande.getDemandeur() == null || demande.getDemandeur().getIdcs() == null
				|| demande.getDemandeur().getIdcs().isEmpty())
			demandeRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_IDCS_DEMANDEUR_OBLIGATOIRE));
		if (demande.getDemandeur() != null
				&& (demande.getDemandeur().getScoreRsu() == null || demande.getDemandeur().getScoreRsu().isEmpty()
						|| !Utils.isDouble(demande.getDemandeur().getScoreRsu())))
			demandeRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_SCORE_RSU_NON_RENSEIGNE));
		String codeMenage = "";
		if (demande.getDemandeur() != null && (demande.getDemandeur().getCodeMenageRsu() == null
				|| demande.getDemandeur().getCodeMenageRsu().isEmpty()))
			demandeRejetees
					.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_CODE_MENAGE_RSU_N_EST_PAS_RENSEIGNE));
		else
			codeMenage = demande.getDemandeur().getCodeMenageRsu();
		codeMenage = codeMenage.replace(" ", "");

		if (demande.getDemandeur() != null && !DemandeurDto.MASCULIN.equals(demande.getDemandeur().getGenre())
				&& !DemandeurDto.FEMININ.equals(demande.getDemandeur().getGenre()))
			demandeRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_GENRE_ERRONE));

		if (demande.getDemandeur() == null || demande.getDemandeur().getIdcs() == null
				|| demande.getDemandeur().getIdcs().isEmpty())
			demandeRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_IDCS_EXIST));

		if (demande.getDemandeur() != null
				&& (demande.getDemandeur().getCin() == null || demande.getDemandeur().getCin().isEmpty()))
			demandeRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_CIN_OBLIGATOIRE));

		if (demande.getDemandeur() != null && (demande.getDemandeur().getEtatMatrimonial() == null
				|| demande.getDemandeur().getEtatMatrimonial().isEmpty()))
			demandeRejetees.add(getDemandeRejetee(demande,
					DemandeRejeteeDto.MESSAGE_L_ETAT_MATRIMONIAL_DU_DEMANDEUR_N_EST_PAS_RENSEIGNE));

		if (demande.getDemandeur() != null && "M".equals(demande.getDemandeur().getEtatMatrimonial())
				&& (demande.getDemandeur().getConjoints() == null || demande.getDemandeur().getConjoints().size() == 0))
			demandeRejetees.add(getDemandeRejetee(demande,
					DemandeRejeteeDto.MESSAGE_L_ETAT_MATRIMONIAL_DU_DEMANDEUR_M_AVEC_0_CONJOINT));

		if (demande.getDemandeur() != null && DemandeDto.CODE_PRESTATION_AIDE_FAMILLE.equals(prestation)
				&& !Arrays.asList("1", "OUI", "oui").contains(demande.getDemandeur().getEstChefMenageRSU()))
			demandeRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_DEMANDEUR_N_EST_PAS_CHEF_MENAGE));
		if (demande.getDemandeur().getIdcs() != null && !listIdcs.add(demande.getDemandeur().getIdcs())) {
			testIdcs = true;
			demandeRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_IDCS_DECLARE_PLUSIEURS_FOIS));
		}

		if (demande.getDemandeur().getConjoints() != null && demande.getDemandeur().getConjoints().size() > 0)
			for (ConjointDto conjont : demande.getDemandeur().getConjoints()) {
				if ("M".equals(demande.getDemandeur().getEtatMatrimonial()) && conjont != null
						&& (conjont.getIdcs() == null || conjont.getIdcs().isEmpty()))
					demandeRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_IDCS_MEMBRE_OBLIGATOIRE));

				if (conjont != null && (conjont.getCin() == null || conjont.getCin().isEmpty()))
					demandeRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_CIN_OBLIGATOIRE));

				if (conjont != null && (conjont.getDateNaissance() == null || conjont.getDateNaissance().isEmpty()))
					demandeRejetees.add(
							getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_DATE_NAISSANCE_MEMBRE_OBLIGATOIRE));

				if (conjont != null && conjont.getDateNaissance() != null
						&& !Utils.isDate(conjont.getDateNaissance(), Utils.FOURMAT_DATE_STRING))
					demandeRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_DATE_FORMAT_ERRONEE));

				if (conjont != null && (conjont.getScoreRsu() == null || conjont.getScoreRsu().isEmpty()
						|| !Utils.isDouble(conjont.getScoreRsu())))
					demandeRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_SCORE_RSU_NON_RENSEIGNE));
				if ("M".equals(demande.getDemandeur().getEtatMatrimonial()) && conjont != null
						&& (conjont.getCodeMenageRsu() == null || conjont.getCodeMenageRsu().isEmpty()))
					demandeRejetees.add(
							getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_CODE_MENAGE_RSU_N_EST_PAS_RENSEIGNE));
				else if (conjont != null && "M".equals(demande.getDemandeur().getEtatMatrimonial())
						&& !codeMenage.equals(conjont.getCodeMenageRsu())
						&& !codeMenage.contains(";" + conjont.getCodeMenageRsu())
						&& !codeMenage.contains(conjont.getCodeMenageRsu() + ";"))
					demandeRejetees.add(
							getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_MEMBRES_N_ONT_PAS_MEME_CODE_MENAGE));
				if (conjont != null && !testIdcs && conjont.getIdcs() != null && !listIdcs.add(conjont.getIdcs())) {
					testIdcs = true;
					demandeRejetees
							.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_IDCS_DECLARE_PLUSIEURS_FOIS));
				}

			}
		if (demande.getDemandeur() != null && demande.getDemandeur().getEnfants() != null
				&& demande.getDemandeur().getEnfants().size() > 0)
			for (EnfantDto enfant : demande.getDemandeur().getEnfants()) {
				if (enfant != null) {
					if (enfant.getIdcs() == null || enfant.getIdcs().isEmpty())
						demandeRejetees
								.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_IDCS_MEMBRE_OBLIGATOIRE));
					if (enfant.getDateNaissance() == null || enfant.getDateNaissance().isEmpty())
						demandeRejetees.add(getDemandeRejetee(demande,
								DemandeRejeteeDto.MESSAGE_DATE_NAISSANCE_MEMBRE_OBLIGATOIRE));
					if (enfant.getDateNaissance() != null
							&& !Utils.isDate(enfant.getDateNaissance(), Utils.FOURMAT_DATE_STRING))
						demandeRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_DATE_FORMAT_ERRONEE));

					if (enfant.getScoreRsu() == null || enfant.getScoreRsu().isEmpty()
							|| !Utils.isDouble(enfant.getScoreRsu()))
						demandeRejetees
								.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_SCORE_RSU_NON_RENSEIGNE));
					if (enfant.getCodeMenageRsu() == null || enfant.getCodeMenageRsu().isEmpty())
						demandeRejetees.add(getDemandeRejetee(demande,
								DemandeRejeteeDto.MESSAGE_CODE_MENAGE_RSU_N_EST_PAS_RENSEIGNE));
					else if (!codeMenage.equals(enfant.getCodeMenageRsu())
							&& !codeMenage.contains(";" + enfant.getCodeMenageRsu())
							&& !codeMenage.contains(enfant.getCodeMenageRsu() + ";"))
						demandeRejetees.add(getDemandeRejetee(demande,
								DemandeRejeteeDto.MESSAGE_MEMBRES_N_ONT_PAS_MEME_CODE_MENAGE));
					if (enfant.getLienParente() == null || enfant.getLienParente().isEmpty())
						demandeRejetees.add(
								getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_LIEN_PARENTE_N_EST_PAS_RENSEIGNE));

					if (enfant.getEstHandicape() == null || enfant.getEstHandicape().isEmpty()
							|| !Arrays.asList("1", "0", "oui", "non", "OUI", "NON").contains(enfant.getEstHandicape()))
						demandeRejetees.add(
								getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_DONNEES_HANDICAP_NON_CONFORME));

					if (enfant.getEstOrphelinPere() == null || enfant.getEstOrphelinPere().isEmpty() || !Arrays
							.asList("1", "0", "oui", "non", "OUI", "NON").contains(enfant.getEstOrphelinPere()))
						demandeRejetees.add(
								getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_DONNEES_ORPHELIN_NON_CONFORME));

					if (enfant.getTypeIdentifiantScolarite() != null
							&& !Arrays
									.asList(EnfantDto.TYPE_SCOLARITE_MASSAR, EnfantDto.TYPE_SCOLARITE_CEF,
											EnfantDto.TYPE_SCOLARITE_AUTRE)
									.contains(enfant.getTypeIdentifiantScolarite()))
						demandeRejetees.add(getDemandeRejetee(demande,
								DemandeRejeteeDto.MESSAGE_IDENTIFIANT_SCOLARITE_NON_CONFORME));

					if (enfant.getTypeIdentifiantScolarite() == null
							&& Arrays.asList(EnfantDto.TYPE_SCOLARITE_MASSAR, EnfantDto.TYPE_SCOLARITE_CEF)
									.contains(enfant.getTypeIdentifiantScolarite()))
						demandeRejetees.add(getDemandeRejetee(demande,
								DemandeRejeteeDto.MESSAGE_IDENTIFIANT_SCOLARITE_NON_CONFORME));

					if (enfant.getIdentifiantScolarite() != null
							&& EnfantDto.TYPE_SCOLARITE_AUTRE.equals(enfant.getTypeIdentifiantScolarite()))
						demandeRejetees.add(getDemandeRejetee(demande,
								DemandeRejeteeDto.MESSAGE_IDENTIFIANT_SCOLARITE_NON_CONFORME));
					if ("F".equals(enfant.getLienParente()) && "F".equals(demande.getDemandeur().getGenre())
							&& demande.getDemandeur().getCin() != null
							&& !demande.getDemandeur().getCin().equals(enfant.getCinMere())
							&& !"C".equals(demande.getDemandeur().getEtatMatrimonial())
							&& !"A".equals(demande.getDemandeur().getEtatMatrimonial()))
						demandeRejetees.add(getDemandeRejetee(demande,
								DemandeRejeteeDto.MESSAGE_INCOHERENCE_ENTRE_GENRE_DEMANDEUR_CIN_MERE_PERE));

					if ("F".equals(enfant.getLienParente()) && "M".equals(demande.getDemandeur().getGenre())
							&& demande.getDemandeur().getCin() != null
							&& !demande.getDemandeur().getCin().equals(enfant.getCinPere())
							&& !"C".equals(demande.getDemandeur().getEtatMatrimonial())
							&& !"A".equals(demande.getDemandeur().getEtatMatrimonial()))
						demandeRejetees.add(getDemandeRejetee(demande,
								DemandeRejeteeDto.MESSAGE_INCOHERENCE_ENTRE_GENRE_DEMANDEUR_CIN_MERE_PERE));

					if (EnfantDto.TYPE_SCOLARITE_MASSAR.equals(enfant.getTypeIdentifiantScolarite())
							&& !Utils.isNumeroMassarValide(enfant.getIdentifiantScolarite()))
						demandeRejetees.add(getDemandeRejetee(demande,
								DemandeRejeteeDto.MESSAGE_IDENTIFIANT_SCOLARITE_NON_CONFORME));

					if (DemandeDto.CODE_PRESTATION_PRIME_NAISSANCE.equals(prestation)
							&& Utils.isDate(enfant.getDateNaissance(), Utils.FOURMAT_DATE_STRING)
							&& !Utils.isAgeMoin6Mois(enfant.getDateNaissance(), demande.getDateDemande())) {
						demandeRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_AGE_PLUS_6_MOIS));
					}
					if (DemandeDto.CODE_PRESTATION_PRIME_NAISSANCE.equals(prestation)
							&& Utils.isDate(enfant.getDateNaissance(), Utils.FOURMAT_DATE_STRING)
							&& !Utils.isDateAfter011223(enfant.getDateNaissance())) {
						demandeRejetees.add(
								getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_DATE_NAISSANCE_BEFORE_011223));
					}

					if (enfant.getCinMere() != null && enfant.getCinMere().equals(demande.getDemandeur().getCin())
							&& "H".equals(demande.getDemandeur().getEtatMatrimonial()))
						demandeRejetees.add(getDemandeRejetee(demande,
								DemandeRejeteeDto.MESSAGE_CIN_MERE_EGALE_CIN_DEMANDEUR_GENRE_DEMANDEUR_H));

					if (enfant.getCinPere() != null && enfant.getCinPere().equals(demande.getDemandeur().getCin())
							&& "F".equals(demande.getDemandeur().getEtatMatrimonial()))
						demandeRejetees.add(getDemandeRejetee(demande,
								DemandeRejeteeDto.MESSAGE_CIN_MERE_EGALE_CIN_DEMANDEUR_GENRE_DEMANDEUR_H));

					if (!testIdcs && enfant.getIdcs() != null && !listIdcs.add(enfant.getIdcs())) {
						testIdcs = true;
						demandeRejetees
								.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_IDCS_DECLARE_PLUSIEURS_FOIS));
					}

//					if(DemandeDto.CODE_PRESTATION_AIDE_FAMILLE.equals(prestation) && 
//							Utils.isDate(enfant.getDateNaissance(), Utils.FOURMAT_DATE_STRING) &&
//							Utils.isDate(demande.getDateDemande(), Utils.FOURMAT_DATE_STRING) &&
//							Utils.isAgeMoin21(enfant.getDateNaissance() ,demande.getDateDemande() ))
//						demandeRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_MEMBRE_AGE_MOINS_21));
				}
			}

		if (demande.getDemandeur() != null
				&& (DemandeDto.CODE_PRESTATION_AIDE_ENFANCE.equals(prestation)
						|| DemandeDto.CODE_PRESTATION_PRIME_NAISSANCE.equals(prestation))
				&& (demande.getDemandeur().getEnfants() == null || demande.getDemandeur().getEnfants().size() == 0)) {
			demandeRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_DECLARATION_ENFNANT_OBLIGATOIRE));
		}
		
		if (demande.getDemandeur() != null
				&& (DemandeDto.CODE_PRESTATION_AIDE_FAMILLE.equals(prestation))
				&& (demande.getDemandeur().getEnfants() != null && demande.getDemandeur().getEnfants().size() > 0) 
				&& existeEnfantAgeInf21(demande.getDemandeur().getEnfants(),demande.getDateDemande() )) {
			demandeRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_DEMANDE_FOR_AVEC_ENFANT));
		}
		
		
		if (demande.getDemandeur().getAutreMembres() != null && demande.getDemandeur().getAutreMembres().size() > 0)
			for (AutreMembreDto autreMembre : demande.getDemandeur().getAutreMembres()) {
				if (autreMembre != null) {
					if (autreMembre.getIdcs() == null || autreMembre.getIdcs().isEmpty())
						demandeRejetees
								.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_IDCS_MEMBRE_OBLIGATOIRE));
					if (autreMembre.getDateNaissance() == null || autreMembre.getDateNaissance().isEmpty())
						demandeRejetees.add(getDemandeRejetee(demande,
								DemandeRejeteeDto.MESSAGE_DATE_NAISSANCE_MEMBRE_OBLIGATOIRE));
					if (autreMembre.getDateNaissance() != null
							&& !Utils.isDate(autreMembre.getDateNaissance(), Utils.FOURMAT_DATE_STRING))
						demandeRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_DATE_FORMAT_ERRONEE));
					if (autreMembre.getScoreRsu() == null || autreMembre.getScoreRsu().isEmpty()
							|| !Utils.isDouble(autreMembre.getScoreRsu()))
						demandeRejetees
								.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_SCORE_RSU_NON_RENSEIGNE));
					if (autreMembre.getCodeMenageRsu() == null || autreMembre.getCodeMenageRsu().isEmpty())
						demandeRejetees.add(getDemandeRejetee(demande,
								DemandeRejeteeDto.MESSAGE_CODE_MENAGE_RSU_N_EST_PAS_RENSEIGNE));
					else if (!codeMenage.equals(autreMembre.getCodeMenageRsu())
							&& !codeMenage.contains(";" + autreMembre.getCodeMenageRsu())
							&& !codeMenage.contains(autreMembre.getCodeMenageRsu() + ";"))
						demandeRejetees.add(getDemandeRejetee(demande,
								DemandeRejeteeDto.MESSAGE_MEMBRES_N_ONT_PAS_MEME_CODE_MENAGE));
				}
			}
		return demandeRejetees;
	}

	private boolean existeEnfantAgeInf21(List<EnfantDto> enfants, String dateEffet) {
		if(enfants != null && enfants.size() > 0)
			for (EnfantDto enfantDto : enfants) {
				if(getAge(enfantDto.getDateNaissance(), dateEffet) < 21)
					return true;
			}
		return false;
	}

	private int getAge(String dateNaissance, String dateEffet) {
		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Utils.FOURMAT_DATE_STRING);
	        LocalDate birthDate = LocalDate.parse(dateNaissance, formatter);
	        LocalDate effet = LocalDate.parse(dateEffet, formatter);
	        return Period.between(birthDate, effet).getYears();
	}

	public List<DemandeRejeteeDto> controlerDemande(String prestation, DemandeEntity demande, List<EnfantEntity> enfs,
			List<ConjointEntity> conjs) {
		List<DemandeRejeteeDto> demandeRejetees = new ArrayList<DemandeRejeteeDto>();

		if (prestation == null || prestation.isEmpty())
			demandeRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_PRESTATION_OBLIGATOIRE));
		if (demande.getDemandeur() == null || demande.getDemandeur().getIdcs() == null
				|| demande.getDemandeur().getIdcs().isEmpty())
			demandeRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_IDCS_DEMANDEUR_OBLIGATOIRE));
		String codeMenage = "";
		if (demande.getDemandeur() != null && (demande.getDemandeur().getCodeMenageRsu() == null
				|| demande.getDemandeur().getCodeMenageRsu().isEmpty()))
			demandeRejetees
					.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_CODE_MENAGE_RSU_N_EST_PAS_RENSEIGNE));
		else
			codeMenage = demande.getDemandeur().getCodeMenageRsu();
		codeMenage = codeMenage.replace(" ", "");

		if (demande.getDemandeur() != null && !DemandeurDto.MASCULIN.equals(demande.getDemandeur().getGenre())
				&& !DemandeurDto.FEMININ.equals(demande.getDemandeur().getGenre()))
			demandeRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_GENRE_ERRONE));

		if (demande.getDemandeur() == null || demande.getDemandeur().getIdcs() == null
				|| demande.getDemandeur().getIdcs().isEmpty())
			demandeRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_IDCS_EXIST));

		if (demande.getDemandeur() != null
				&& (demande.getDemandeur().getCin() == null || demande.getDemandeur().getCin().isEmpty()))
			demandeRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_CIN_OBLIGATOIRE));

		if (demande.getDemandeur() != null && (demande.getDemandeur().getEtatMatrimonial() == null
				|| demande.getDemandeur().getEtatMatrimonial().isEmpty()))
			demandeRejetees.add(getDemandeRejetee(demande,
					DemandeRejeteeDto.MESSAGE_L_ETAT_MATRIMONIAL_DU_DEMANDEUR_N_EST_PAS_RENSEIGNE));

		if (demande.getDemandeur() != null && "M".equals(demande.getDemandeur().getEtatMatrimonial())
				&& (conjs == null || conjs.size() == 0))
			demandeRejetees.add(getDemandeRejetee(demande,
					DemandeRejeteeDto.MESSAGE_L_ETAT_MATRIMONIAL_DU_DEMANDEUR_M_AVEC_0_CONJOINT));

		if (demande.getDemandeur() != null && DemandeDto.CODE_PRESTATION_AIDE_FAMILLE.equals(prestation)
				&& (demande.getDemandeur().getEstChefMenageRSU() == null
						|| demande.getDemandeur().getEstChefMenageRSU() == false))
			demandeRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_DEMANDEUR_N_EST_PAS_CHEF_MENAGE));

		if (conjs != null && conjs.size() > 0)
			for (ConjointEntity conjont : conjs) {
				if ("M".equals(demande.getDemandeur().getEtatMatrimonial()) && conjont != null
						&& (conjont.getIdcs() == null || conjont.getIdcs().isEmpty()))
					demandeRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_IDCS_MEMBRE_OBLIGATOIRE));

				if (conjont != null && (conjont.getCin() == null || conjont.getCin().isEmpty()))
					demandeRejetees.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_CIN_OBLIGATOIRE));

				if ("M".equals(demande.getDemandeur().getEtatMatrimonial()) && conjont != null
						&& (conjont.getCodeMenageRsu() == null || conjont.getCodeMenageRsu().isEmpty()))
					demandeRejetees.add(
							getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_CODE_MENAGE_RSU_N_EST_PAS_RENSEIGNE));
				else if (conjont != null && "M".equals(demande.getDemandeur().getEtatMatrimonial())
						&& !codeMenage.equals(conjont.getCodeMenageRsu())
						&& !codeMenage.contains(";" + conjont.getCodeMenageRsu())
						&& !codeMenage.contains(conjont.getCodeMenageRsu() + ";"))
					demandeRejetees.add(
							getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_MEMBRES_N_ONT_PAS_MEME_CODE_MENAGE));

			}
		if (enfs != null && enfs.size() > 0)
			for (EnfantEntity enfant : enfs) {
				if (enfant != null) {
					if (enfant.getIdcs() == null || enfant.getIdcs().isEmpty())
						demandeRejetees
								.add(getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_IDCS_MEMBRE_OBLIGATOIRE));

					if (enfant.getCodeMenageRsu() == null || enfant.getCodeMenageRsu().isEmpty())
						demandeRejetees.add(getDemandeRejetee(demande,
								DemandeRejeteeDto.MESSAGE_CODE_MENAGE_RSU_N_EST_PAS_RENSEIGNE));
					else if (!codeMenage.equals(enfant.getCodeMenageRsu())
							&& !codeMenage.contains(";" + enfant.getCodeMenageRsu())
							&& !codeMenage.contains(enfant.getCodeMenageRsu() + ";"))
						demandeRejetees.add(getDemandeRejetee(demande,
								DemandeRejeteeDto.MESSAGE_MEMBRES_N_ONT_PAS_MEME_CODE_MENAGE));
					if (enfant.getLienParente() == null || enfant.getLienParente().isEmpty())
						demandeRejetees.add(
								getDemandeRejetee(demande, DemandeRejeteeDto.MESSAGE_LIEN_PARENTE_N_EST_PAS_RENSEIGNE));

					if (enfant.getTypeIdentifiantScolarite() != null
							&& !Arrays
									.asList(EnfantDto.TYPE_SCOLARITE_MASSAR, EnfantDto.TYPE_SCOLARITE_CEF,
											EnfantDto.TYPE_SCOLARITE_AUTRE)
									.contains(enfant.getTypeIdentifiantScolarite()))
						demandeRejetees.add(getDemandeRejetee(demande,
								DemandeRejeteeDto.MESSAGE_IDENTIFIANT_SCOLARITE_NON_CONFORME));

					if (enfant.getTypeIdentifiantScolarite() == null
							&& Arrays.asList(EnfantDto.TYPE_SCOLARITE_MASSAR, EnfantDto.TYPE_SCOLARITE_CEF)
									.contains(enfant.getTypeIdentifiantScolarite()))
						demandeRejetees.add(getDemandeRejetee(demande,
								DemandeRejeteeDto.MESSAGE_IDENTIFIANT_SCOLARITE_NON_CONFORME));

					if (enfant.getIdentifiantScolarite() != null
							&& EnfantDto.TYPE_SCOLARITE_AUTRE.equals(enfant.getTypeIdentifiantScolarite()))
						demandeRejetees.add(getDemandeRejetee(demande,
								DemandeRejeteeDto.MESSAGE_IDENTIFIANT_SCOLARITE_NON_CONFORME));
					if ("F".equals(enfant.getLienParente()) && "F".equals(demande.getDemandeur().getGenre())
							&& demande.getDemandeur().getCin() != null
							&& !demande.getDemandeur().getCin().equals(enfant.getCinMere()))
						demandeRejetees.add(getDemandeRejetee(demande,
								DemandeRejeteeDto.MESSAGE_INCOHERENCE_ENTRE_GENRE_DEMANDEUR_CIN_MERE_PERE));

					if ("F".equals(enfant.getLienParente()) && "M".equals(demande.getDemandeur().getGenre())
							&& demande.getDemandeur().getCin() != null
							&& !demande.getDemandeur().getCin().equals(enfant.getCinPere()))
						demandeRejetees.add(getDemandeRejetee(demande,
								DemandeRejeteeDto.MESSAGE_INCOHERENCE_ENTRE_GENRE_DEMANDEUR_CIN_MERE_PERE));

					if (EnfantDto.TYPE_SCOLARITE_MASSAR.equals(enfant.getTypeIdentifiantScolarite())
							&& !Utils.isNumeroMassarValide(enfant.getIdentifiantScolarite()))
						demandeRejetees.add(getDemandeRejetee(demande,
								DemandeRejeteeDto.MESSAGE_IDENTIFIANT_SCOLARITE_NON_CONFORME));
				}
			}

		return demandeRejetees;
	}

	public DemandeRejeteeDto getDemandeRejetee(DemandeDto demande, String messageReferenceObligatoire) {
		if (demande == null)
			return null;
		DemandeRejeteeDto demandeRejetee = new DemandeRejeteeDto();
		demandeRejetee.setMotifDemande(messageReferenceObligatoire);
		demandeRejetee.setReferenceCnss(demande.getReferenceCnss());
		demandeRejetee.setDateDemande(demande.getDateDemande());
		demandeRejetee.setImmatriculation(demande.getImmatriculation());
		demandeRejetee.setStatutDemande(DemandeDto.STATUT_REJETE);
		return demandeRejetee;
	}

	public DemandeRejeteeDto getDemandeRejete(DemandeAjoutMembreEntity demande, String messageReferenceObligatoire) {
		if (demande == null)
			return null;
		DemandeRejeteeDto demandeRejetee = new DemandeRejeteeDto();
		demandeRejetee.setMotifDemande(messageReferenceObligatoire);
		demandeRejetee.setReferenceCnss(demande.getReferenceCnss());
//		demandeRejetee.setDateDemande(demande.getDateDemande());
//		demandeRejetee.setImmatriculation(demande.getImmatriculation());
		demandeRejetee.setStatutDemande(DemandeDto.STATUT_REJETE);
		return demandeRejetee;
	}

	public DemandeRejeteeDto getDemandeRejetee(DemandeAjoutMembreDto demande, String messageReferenceObligatoire) {
		if (demande == null)
			return null;
		DemandeRejeteeDto demandeRejetee = new DemandeRejeteeDto();
		demandeRejetee.setMotifDemande(messageReferenceObligatoire);
		demandeRejetee.setReferenceCnss(demande.getReferenceCnss());
//		demandeRejetee.setDateDemande(demande.getDateDemande());
//		demandeRejetee.setImmatriculation(demande.getImmatriculation());
		demandeRejetee.setStatutDemande(DemandeDto.STATUT_REJETE);
		return demandeRejetee;
	}
	
	public DemandeRejeteeDto getDemandeRejeteeAjoutDemande(DemandeAjoutMembreDto demande, String messageReferenceObligatoire) {
		if (demande == null)
			return null;
		DemandeRejeteeDto demandeRejetee = new DemandeRejeteeDto();
		demandeRejetee.setMotifDemande(messageReferenceObligatoire);
		demandeRejetee.setReferenceCnss(demande.getReferenceDemande());
//		demandeRejetee.setDateDemande(demande.getDateDemande());
//		demandeRejetee.setImmatriculation(demande.getImmatriculation());
		demandeRejetee.setStatutDemande(DemandeDto.STATUT_REJETE);
		return demandeRejetee;
	}

	public DemandeRejeteeDto getDemandeRejetee(DemandeRecoursDto demande, String messageReferenceObligatoire) {
		if (demande == null)
			return null;
		DemandeRejeteeDto demandeRejetee = new DemandeRejeteeDto();
		demandeRejetee.setMotifDemande(messageReferenceObligatoire);
		demandeRejetee.setReferenceCnss(demande.getReferenceCnss());
		demandeRejetee.setStatutDemande(DemandeDto.STATUT_REJETE);
		return demandeRejetee;
	}

	public DemandeRejeteeDto getDemandeRejetee(DemandeEntity demande, String messageReferenceObligatoire) {
		if (demande == null)
			return null;
		DemandeRejeteeDto demandeRejetee = new DemandeRejeteeDto();
		demandeRejetee.setMotifDemande(messageReferenceObligatoire);
		demandeRejetee.setReferenceCnss(demande.getReferenceCnss());
		demandeRejetee.setDateDemande(Utils.dateToString(demande.getDateDemande(), Utils.FOURMAT_DATE_STRING));
		demandeRejetee.setImmatriculation(demande.getImmatriculation());
		demandeRejetee.setStatutDemande(DemandeDto.STATUT_REJETE);
		return demandeRejetee;
	}

	public DemandeRejeteeDto getDemandeRejetee(RecoursEntity demande, String messageReferenceObligatoire) {
		if (demande == null)
			return null;
		DemandeRejeteeDto demandeRejetee = new DemandeRejeteeDto();
		demandeRejetee.setMotifDemande(messageReferenceObligatoire);
		demandeRejetee.setReferenceCnss(demande.getReference());
		demandeRejetee.setDateDemande(Utils.dateToString(demande.getDateDemande(), Utils.FOURMAT_DATE_STRING));
		demandeRejetee.setStatutDemande(DemandeDto.STATUT_REJETE);
		return demandeRejetee;
	}

	private DemandeRejeteeDto getDemandeRejetee(DemandeAnnuleeDto demande, String messageReferenceObligatoire) {
		DemandeRejeteeDto demandeRejetee = new DemandeRejeteeDto();
		demandeRejetee.setMotifDemande(messageReferenceObligatoire);
		demandeRejetee.setReferenceCnss(demande.getReferenceCnss());
//		demandeRejetee.setImmatriculation(demande.getImmatriculation());
		demandeRejetee.setStatutDemande(DemandeDto.STATUT_REJETE);
		return demandeRejetee;
	}

}
