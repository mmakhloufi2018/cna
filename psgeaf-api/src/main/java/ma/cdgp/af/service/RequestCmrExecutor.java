package ma.cdgp.af.service;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

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
import ma.cdgp.af.client.CmrClient;
import ma.cdgp.af.dto.af.cmr.LotSituationPrsCmrDto;
import ma.cdgp.af.entity.RetourTracabilite;
import ma.cdgp.af.entity.TraceEnvoiRetour;
import ma.cdgp.af.entity.cmr.LotSituationCmr;
import ma.cdgp.af.oauth2.JwtResponse;
import ma.cdgp.af.oauth2.JwtUtils;
import ma.cdgp.af.oauth2.LoginRequest;
import ma.cdgp.af.repository.LotSituationCmrRepo;
import ma.cdgp.af.repository.RetourTracabiliteRepository;
import ma.cdgp.af.repository.TraceEnvoiRetourRepository;

@Service("CMR")
public class RequestCmrExecutor extends ExecutorSituationPersonne {

	@Autowired private LotSituationCmrRepo lotSituationPersonneRepo;
	@Autowired private RetourTracabiliteRepository retourHistoRepo;
	@Autowired private TraceEnvoiRetourRepository traceEnvoiRetourRepository;
	@Autowired private CmrClient cmrClient;
	@Autowired private JwtUtils jwtUtils;

	@Value("${cmr.fa.cle.pass}")
	private String clePasswordCmr;

	@Value("${cmr.fa.user.pass}")
	private String userAuthCmr;

	public CompletableFuture<ResponseEntity<?>> traiterDemandes(Long idRequeteRSU) throws Exception {
		Assert.notNull(idRequeteRSU);
		LotSituationCmr savedRR = lotSituationPersonneRepo.findById(idRequeteRSU).orElse(null);
		Assert.notNull(savedRR, "Lot not found");

		savedRR.setLocked(true);
		lotSituationPersonneRepo.save(savedRR);

		try {
			// Authentication
			LoginRequest login = new LoginRequest();
			login.setUserName(userAuthCmr);
			login.setPassword(jwtUtils.getPassword(clePasswordCmr));

			JwtResponse auth = null;
			try {
				auth = cmrClient.authCmr(login);
			} catch (Exception e) {
				login.setPassword(jwtUtils.getPasswordPlusHour(clePasswordCmr));
				auth = cmrClient.authCmr(login);
			}

			Assert.notNull(auth, "Authentification CMR failed");

			Date requestDate = new Date();
			LotSituationPrsCmrDto requestDto = LotSituationPrsCmrDto.fromEntity(savedRR);
			Assert.notEmpty(requestDto.getPersonnes(), "INPUT: Liste personnes vide");

			LotSituationPrsCmrDto response = cmrClient.askCmr(requestDto, "Bearer " + auth.getToken().getAccessToken());

			saveTraceEnvoiRetour(requestDate, requestDto, response, idRequeteRSU);
			Assert.notNull(response, "Reponse CMR Vide");
			Assert.notEmpty(response.getPersonnes(), "OUTPUT: Liste personnes vide");

			LotSituationCmr retour = LotSituationPrsCmrDto.fromRetourDto(response, savedRR);
			if (CollectionUtils.isEmpty(retour.getPersonnes())) {
				retour.setCodeRetour("500");
				retour.setEtatLot("KO");
				retour.setMessageRetour("EMPTY_PERSONNES");
			} else if (retour.getPersonnes().size() != savedRR.getDemandes().size()) {
				retour.setCodeRetour("500");
				retour.setEtatLot("KO");
				retour.setMessageRetour("SOME_PERSONNES_KO");
			} else {
				retour.setCodeRetour("200");
				retour.setEtatLot("OK");
				retour.setMessageRetour("OK");
			}
			retour.setDateReponse(new Date());
			retour.setters();

			LotSituationCmr savedRetour = lotSituationPersonneRepo.save(retour);
			System.err.println("Retour " + retour.getEtatLot() + " ID : " + savedRetour.getId());

			addTracabiliteRetour(savedRetour, retour.getEtatLot(), retour.getMessageRetour());
			return CompletableFuture.completedFuture(ResponseEntity.ok().body(""));

		} catch (Exception e) {
			e.printStackTrace();
			savedRR.setCodeRetour("500");
			savedRR.setEtatLot("KO");
			savedRR.setMessageRetour(e.getMessage().length() > 1000 ? e.getMessage().substring(0, 1000) : e.getMessage());
			savedRR.setDateReponse(null);
			savedRR.setters();
			savedRR.setLocked(false);
			lotSituationPersonneRepo.save(savedRR);
			addTracabiliteRetour(savedRR, "KO", savedRR.getMessageRetour());
			return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(""));
		}
	}

	public void addTracabiliteRetour(LotSituationCmr r, String status, String msg) {
		retourHistoRepo.save(new RetourTracabilite(null, r, status, msg, new Date()));
	}

	private void saveTraceEnvoiRetour(Date dateCreation, LotSituationPrsCmrDto req, LotSituationPrsCmrDto rep, Long idLot) {
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
		if (type != null && type.equalsIgnoreCase("CMR")) {
			try {
				traiterDemandes(idLot);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
