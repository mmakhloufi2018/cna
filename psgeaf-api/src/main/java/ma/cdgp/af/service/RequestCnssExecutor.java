package ma.cdgp.af.service;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.sql.rowset.serial.SerialException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.lang.Assert;
import ma.cdgp.af.client.AffiliationClient;
import ma.cdgp.af.client.CnssClient;
import ma.cdgp.af.dto.af.cnss.AcquittementSituationCnssDto;
import ma.cdgp.af.dto.af.cnss.LotReponseSituationCnssDto;
import ma.cdgp.af.dto.af.cnss.LotSituationPrsCnssDto;
import ma.cdgp.af.entity.RetourTracabilite;
import ma.cdgp.af.entity.TraceEnvoiRetour;
import ma.cdgp.af.entity.cnss.LotSituationCnss;
import ma.cdgp.af.oauth2.JwtUtils;
import ma.cdgp.af.repository.LotSituationCnssRepo;
import ma.cdgp.af.repository.RetourTracabiliteRepository;
import ma.cdgp.af.repository.TraceEnvoiRetourRepository;

@Service("CNSS")
public class RequestCnssExecutor extends ExecutorSituationPersonne {

	@Autowired private LotSituationCnssRepo lotSituationPersonneRepo;
	@Autowired private RetourTracabiliteRepository retourHistoRepo;
	@Autowired private TraceEnvoiRetourRepository traceEnvoiRetourRepository;
	@Autowired private CnssClient cnssClient;
	@Autowired private ObjectMapper objectMapper;

	public CompletableFuture<ResponseEntity<?>> traiterDemandes(Long idRequeteRSU) throws Exception {
		Assert.notNull(idRequeteRSU);
		LotSituationCnss savedRR = lotSituationPersonneRepo.findById(idRequeteRSU).orElse(null);
		Assert.notNull(savedRR, "Lot cnss not found");

		try {
			Date dateCreationReq = Calendar.getInstance().getTime();
			LotSituationPrsCnssDto reqIn = LotSituationPrsCnssDto.fromEntity(savedRR);
			Assert.notEmpty(reqIn.getPersonnes(), "INPUT: Liste personnes vide");
			AcquittementSituationCnssDto response = cnssClient.askCnss(reqIn, null);
			saveTraceEnvoiRetour(dateCreationReq, reqIn, response, idRequeteRSU);
			Assert.notNull(response, "Reponse CNSS Vide");

			savedRR.setCodeRetour(response.getStatus());
			savedRR.setDateReponse(new Date());
			savedRR.setEtatLot(response.getStatus().equalsIgnoreCase("R") ? "KO_SEND" : "WAITING_RESPONSE");
			savedRR.setMessageRetour(response.getMessage());
			savedRR.setters();

			LotSituationCnss savedRetour = lotSituationPersonneRepo.save(savedRR);
			System.err.println("Retour " + response.getStatus() + " ID : " + savedRetour.getId());

			addTracabiliteRetour(savedRetour, savedRR.getEtatLot(), savedRR.getMessageRetour());
			return CompletableFuture.completedFuture(ResponseEntity.ok().body(""));

		} catch (Exception e) {
			e.printStackTrace();
			handleLotError(savedRR, "KO_SEND", e.getMessage());
			return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(""));
		}
	}

	public CompletableFuture<ResponseEntity<?>> traiterReponsesSituationCnss(LotSituationPrsCnssDto repLotCnss) throws Exception {
		Assert.notNull(repLotCnss);
		Assert.notEmpty(repLotCnss.getPersonnes(), "REP CNSS: Liste personnes vide");
		LotSituationCnss foundLot = lotSituationPersonneRepo.findById(repLotCnss.getReferenceLot()).orElse(null);
		Assert.notNull(foundLot, "Lot cnss not found " + repLotCnss.getReferenceLot());

		try {
			LotSituationCnss retour = LotSituationPrsCnssDto.fromRetourDto(repLotCnss, foundLot);

			if (CollectionUtils.isEmpty(retour.getPersonnes())) {
				retour.setEtatLot("KO");
				retour.setMessageRetour("lot reçu avec une liste de personnes vide");
			} else if (retour.getPersonnes().size() != foundLot.getDemandes().size()) {
				retour.setEtatLot("KO");
				retour.setMessageRetour("lot reçu avec une liste de personnes incomplète");
			} else {
				retour.setEtatLot("OK");
				retour.setMessageRetour("lot reçu avec succès");
			}

			retour.setCodeRetour(retour.getEtatLot().equals("OK") ? "200" : "500");
			retour.setDateReponse(new Date());
			retour.setters();
			retour.setLocked(true);

			LotSituationCnss savedRetour = lotSituationPersonneRepo.save(retour);
			System.err.println("Retour " + retour.getEtatLot() + " ID : " + savedRetour.getId());

			addTracabiliteRetour(savedRetour, retour.getEtatLot(), retour.getMessageRetour());
			waitForCnssRersponse(repLotCnss.getReferenceLot());

			AcquittementSituationCnssDto acq = new AcquittementSituationCnssDto(retour.getDateLot(), retour.getLotId(),
					retour.getCodeRetour(), retour.getMessageRetour(), "S");
			return CompletableFuture.completedFuture(ResponseEntity.ok().body(acq));

		} catch (Exception e) {
			e.printStackTrace();
			handleLotError(foundLot, "KO", e.getMessage());
			AcquittementSituationCnssDto acq = new AcquittementSituationCnssDto(foundLot.getDateLot(), foundLot.getLotId(),
					foundLot.getCodeRetour(), "Erreur interne", "R");
			return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(acq));
		}
	}

	private void handleLotError(LotSituationCnss lot, String etat, String msg) {
		lot.setCodeRetour("500");
		lot.setEtatLot(etat);
		lot.setMessageRetour(msg.length() > 1000 ? msg.substring(0, 1000) : msg);
		lot.setDateReponse(null);
		lot.setters();
		lot.setLocked(false);
		lotSituationPersonneRepo.save(lot);
		addTracabiliteRetour(lot, etat, msg);
	}

	public void addTracabiliteRetour(LotSituationCnss r, String status, String msg) {
		retourHistoRepo.save(new RetourTracabilite(null, r, status, msg, new Date()));
	}

	private void saveTraceEnvoiRetour(Date dateCreation, LotSituationPrsCnssDto req, AcquittementSituationCnssDto rep, Long idLot) {
		TraceEnvoiRetour trace = new TraceEnvoiRetour(null, getclob(req.toString()), null, dateCreation, null);
		trace.setIdRetour(idLot);
		trace.setDateRsu(new Date());
		trace.setRetourRsu(rep != null ? getclob(rep.toString()) : null);
		traceEnvoiRetourRepository.save(trace);
	}

	private void saveTraceEnvoiRetour(Date dateCreation, Long req, LotReponseSituationCnssDto rep, Long idLot) {
		TraceEnvoiRetour trace = new TraceEnvoiRetour(null, getclob(req.toString()), null, dateCreation, null);
		trace.setIdRetour(idLot);
		trace.setDateRsu(new Date());
		trace.setRetourRsu(rep != null ? getclob(rep.toString()) : null);
		traceEnvoiRetourRepository.save(trace);
	}

	private Clob getclob(String val) {
		try {
			return new javax.sql.rowset.serial.SerialClob(val.toCharArray());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}


	private void waitForCnssRersponse(Long refLotCnss) {
		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.schedule(() -> {
			try {
				askCnssForResponse(refLotCnss);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, 15, TimeUnit.MINUTES);
	}

	public void lancerRecupReponses(Long refLotCnss) {
		new Thread(() -> {
			try {
				askCnssForResponse(refLotCnss);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}


	public CompletableFuture<ResponseEntity<?>> askCnssForResponse(Long refLotCnss) throws Exception {
		LotSituationCnss foundLot = lotSituationPersonneRepo.findById(refLotCnss).orElse(null);
		Assert.notNull(foundLot, "Lot cnss not found " + refLotCnss);

		try {
			LotReponseSituationCnssDto rep = cnssClient.askCnssForResponse(String.valueOf(refLotCnss), "");
			LotSituationCnss retour;

			if (rep != null && rep.isAcquittement()) {
				retour = foundLot;
				retour.setEtatLot("KO_REP");
				retour.setMessageRetour(rep.getMessage());
			} else {
				retour = LotSituationPrsCnssDto.fromResponseDto(rep, foundLot);
				if (CollectionUtils.isEmpty(retour.getPersonnes())) {
					retour.setEtatLot("KO_REP");
					retour.setMessageRetour("lot reçu avec une liste de personnes vide");
				} else {
					retour.setEtatLot("OK_REP");
					retour.setMessageRetour("lot reçu avec succès");
				}
			}

			retour.setCodeRetour(retour.getEtatLot().startsWith("OK") ? "200" : "500");
			retour.setDateReponse(new Date());
			retour.setLocked(false);
			retour.setters();

			LotSituationCnss savedRetour = lotSituationPersonneRepo.save(retour);
			addTracabiliteRetour(savedRetour, retour.getEtatLot(), retour.getMessageRetour());
			saveTraceEnvoiRetour(new Date(), refLotCnss, rep, refLotCnss);

			AcquittementSituationCnssDto acq = new AcquittementSituationCnssDto(retour.getDateLot(), retour.getLotId(),
					retour.getCodeRetour(), retour.getMessageRetour(), "S");
			return CompletableFuture.completedFuture(ResponseEntity.ok().body(acq));

		} catch (Exception e) {
			e.printStackTrace();
			handleLotError(foundLot, "KO_REP", e.getMessage());
			AcquittementSituationCnssDto acq = new AcquittementSituationCnssDto(foundLot.getDateLot(),
					foundLot.getLotId(), foundLot.getCodeRetour(), "Erreur interne", "R");
			return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(acq));
		}
	}

	@Override
	public void executeWork(Long idLot, String type) {
		if (type == null || !type.equalsIgnoreCase("CNSS")) return;

		LotSituationCnss savedRR = lotSituationPersonneRepo.findById(idLot).orElse(null);
		Assert.notNull(savedRR, "Lot not found");
		try {
			traiterDemandes(savedRR.getIdLot());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
