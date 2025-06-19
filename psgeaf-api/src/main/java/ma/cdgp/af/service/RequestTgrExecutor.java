package ma.cdgp.af.service;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

import javax.sql.rowset.serial.SerialException;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.lang.Assert;
import ma.cdgp.af.client.AffiliationClient;
import ma.cdgp.af.client.TGrClient;
import ma.cdgp.af.dto.af.tgr.LotSituationPrsTgrDto;
import ma.cdgp.af.entity.RetourTracabilite;
import ma.cdgp.af.entity.TraceEnvoiRetour;
import ma.cdgp.af.entity.tgr.LotSituationTgr;
import ma.cdgp.af.oauth2.JwtResponse;
import ma.cdgp.af.oauth2.JwtUtils;
import ma.cdgp.af.oauth2.LoginRequestTgr;
import ma.cdgp.af.repository.LotSituationTgrRepo;
import ma.cdgp.af.repository.RetourTracabiliteRepository;
import ma.cdgp.af.repository.TraceEnvoiRetourRepository;

@Service("TGR")
public class RequestTgrExecutor extends ExecutorSituationPersonne {

	@Autowired private LotSituationTgrRepo lotSituationPersonneRepo;
	@Autowired private RetourTracabiliteRepository retourHistoRepo;
	@Autowired private TraceEnvoiRetourRepository traceEnvoiRetourRepository;
	@Autowired private ApplicationContext ctx;
	@Autowired private AffiliationClient service;
	@Autowired private JwtUtils jwtUtils;
	@Autowired private TGrClient tgrClient;

	@Value("${tgr.fa.cle.pass}")
	private String clePass;

	@Value("${tgr.fa.user.pass}")
	private String userAuth;

	public CompletableFuture<ResponseEntity<?>> traiterDemandes(Long idRequeteRSU) throws Exception {
		Assert.notNull(idRequeteRSU, "[TGR] idRequeteRSU is null.");
		LotSituationTgr savedRR = lotSituationPersonneRepo.findById(idRequeteRSU).orElse(null);
		Assert.notNull(savedRR, "[TGR] Lot not found: " + idRequeteRSU);

		savedRR.setLocked(true);
		lotSituationPersonneRepo.save(savedRR);

		try {
			// Authentification
			LoginRequestTgr login = new LoginRequestTgr();
			login.setPassword(clePass);
			login.setUserName(userAuth);
			JwtResponse token = tgrClient.authTgr(login);

			Assert.notNull(token, "[TGR] Authentication failed.");
			System.out.println("[TGR][AUTH] Token: " + token.getToken().getAccessToken());

			// Prepare & send request
			Date requestDate = Calendar.getInstance().getTime();
			LotSituationPrsTgrDto reqIn = LotSituationPrsTgrDto.fromEntity(savedRR);
			Assert.notEmpty(reqIn.getPersonnes(), "[TGR] No persons in input.");

			LotSituationPrsTgrDto rep = tgrClient.askTgr(reqIn, "Bearer " + token.getToken().getAccessToken());

			saveTraceEnvoiRetour(requestDate, reqIn, rep, idRequeteRSU);

			Assert.notNull(rep, "[TGR] Response is null.");
			Assert.notEmpty(rep.getPersonnes(), rep.getLibeMotif());

			// Convert to entity
			LotSituationTgr retour = LotSituationPrsTgrDto.fromRetourDto(rep, savedRR);
			if (CollectionUtils.isEmpty(retour.getPersonnes()) || !"0000".equalsIgnoreCase(rep.getCodeMotif())) {
				retour.setCodeRetour(rep.getCodeMotif());
				retour.setEtatLot("KO");
				retour.setMessageRetour(rep.getLibeMotif());
			} else {
				retour.setCodeRetour("200");
				retour.setEtatLot("OK");
				retour.setMessageRetour("OK");
			}

			retour.setDateReponse(new Date());
			retour.setters();
			retour.setLocked(retour.getEtatLot().equals("OK")); // only lock on success

			LotSituationTgr savedRetour = lotSituationPersonneRepo.save(retour);
			System.out.println("[TGR][RETOUR] Saved ID: " + savedRetour.getId());

			addTracabiliteRetour(savedRetour, retour.getEtatLot(), retour.getMessageRetour());

			return CompletableFuture.completedFuture(ResponseEntity.ok().body(""));
		} catch (Exception e) {
			e.printStackTrace();
			savedRR.setCodeRetour("500");
			savedRR.setEtatLot("KO");
			savedRR.setMessageRetour(e.getMessage());
			savedRR.setDateReponse(null);
			savedRR.setLocked(false);
			savedRR.setters();
			lotSituationPersonneRepo.save(savedRR);

			String msg = e.getMessage().length() > 1000 ? e.getMessage().substring(0, 1000) : e.getMessage();
			addTracabiliteRetour(savedRR, "KO", msg);

			return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(""));
		}
	}

	public void addTracabiliteRetour(LotSituationTgr r, String status, String msg) {
		retourHistoRepo.save(new RetourTracabilite(null, r, status, msg, new Date()));
	}

	private void saveTraceEnvoiRetour(Date dateCreation, LotSituationPrsTgrDto req, LotSituationPrsTgrDto rep, Long idLot) {
		TraceEnvoiRetour trace = new TraceEnvoiRetour(null, getClob(req.toString()), null, dateCreation, null);
		trace.setIdRetour(idLot);
		trace.setDateRsu(new Date());
		trace.setRetourRsu(rep != null ? getClob(rep.toString()) : null);
		traceEnvoiRetourRepository.save(trace);
	}

	private Clob getClob(String val) {
		try {
			return new javax.sql.rowset.serial.SerialClob(val.toCharArray());
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public void executeWork(Long idLot, String type) {
		if (type == null || !type.equalsIgnoreCase("TGR")) return;
		try {
			traiterDemandes(idLot);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
