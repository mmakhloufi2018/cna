package ma.cdgp.af.service;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.sql.rowset.serial.SerialException;

import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import io.jsonwebtoken.lang.Assert;
import ma.cdgp.af.client.AffiliationClient;
import ma.cdgp.af.client.RcarClient;
import ma.cdgp.af.dto.af.rcar.DetailsLotSituationPrsRcarDto;
import ma.cdgp.af.dto.af.rcar.LotSituationPrsRcarDto;
import ma.cdgp.af.entity.RetourTracabilite;
import ma.cdgp.af.entity.TraceEnvoiRetour;
import ma.cdgp.af.entity.rcar.DetailsLotSituationRcar;
import ma.cdgp.af.entity.rcar.LotSituationRcar;
import ma.cdgp.af.oauth2.JwtUtils;
import ma.cdgp.af.repository.DetailsLotSituationRcarRepo;
import ma.cdgp.af.repository.LotSituationRcarRepo;
import ma.cdgp.af.repository.RetourTracabiliteRepository;
import ma.cdgp.af.repository.TraceEnvoiRetourRepository;

@Service("RCAR")
public class RequestRcarExecutor extends ExecutorSituationPersonne {

	@Autowired
	LotSituationRcarRepo lotSituationPersonneRepo;
	@Autowired
	DetailsLotSituationRcarRepo detailsLotSituationRepo;

	@Autowired
	RetourTracabiliteRepository retourHistoRepo;

	@Autowired
	ApplicationContext ctx;
	@Autowired
	TraceEnvoiRetourRepository traceEnvoiRetourRepository;

	@Autowired
	AffiliationClient service;

	@Value("${rsu.sec.clientId}")
	private String clientId;

	@Value("${cmr.fa.cle.pass}")
	private String clePasswordCmr;

	@Value("${cmr.fa.user.pass}")
	private String userAuthCmr;
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	RcarClient rcarClient;

	public void traiterDemandes(Long idRequeteRSU) throws Exception {
		Assert.notNull(idRequeteRSU);
		LotSituationRcar savedRR = lotSituationPersonneRepo.findById(idRequeteRSU).orElse(null);
		Assert.notNull(savedRR, "Lot not found");
		lotSituationPersonneRepo.updateLock(true, idRequeteRSU);
		try {
			Date dateCreationReq = Calendar.getInstance().getTime();
			LotSituationPrsRcarDto reqIn = LotSituationPrsRcarDto.fromEntity(savedRR);
			System.out.println("asking RCAR ....");
			Set<DetailsLotSituationPrsRcarDto> responseList = rcarClient.askRcar();
			Assert.notEmpty(responseList, "OUTPUT: Liste personnes vide");
			System.out.println("RCAR respond with .... "+responseList.size()+" item" );
			LotSituationPrsRcarDto repLot = new LotSituationPrsRcarDto(Long.valueOf(savedRR.getLotId()),
					responseList);
//			saveTraceEnvoiRetour(dateCreationReq, reqIn, repLot, idRequeteRSU);
			LotSituationRcar retour = LotSituationPrsRcarDto.fromRetourDto(repLot, savedRR);
			System.out.println("saving RCAR with .... "+responseList.size()+" item" );
		
			List<List<DetailsLotSituationRcar>> subLists = Lists.partition(new ArrayList<>(retour.getPersonnes()), 1000);
			int index = 1;
			for (List<DetailsLotSituationRcar> list : subLists) {
				System.err.println("saving lot index : " + index);
				list.forEach(t -> {
					System.err.println(t.getCin());
					t.setLot(savedRR);
				});
				detailsLotSituationRepo.saveAll(list);
				index++;
			}
			retour.setCodeRetour("200");
			retour.setDateReponse(new Date());
			retour.setEtatLot("OK");
			retour.setMessageRetour("OK");
			retour.setters();
			LotSituationRcar savedRetour = lotSituationPersonneRepo.save(retour);
			System.err.println("Retour OK ID : " + savedRetour.getId());
			addTracabiliteRetour(savedRetour, retour.getEtatLot(), retour.getMessageRetour());
			lotSituationPersonneRepo.updateLock(false, idRequeteRSU);
		} catch (Exception e) {
			e.printStackTrace();
			savedRR.setCodeRetour("500");
			savedRR.setEtatLot("KO");
			savedRR.setMessageRetour(e.getMessage());
			savedRR.setDateReponse(null);
			savedRR.setters();
			savedRR.setLocked(false);
			lotSituationPersonneRepo.save(savedRR);
			String msg = e.getMessage().length() > 1000 ? e.getMessage().substring(0, 1000) : e.getMessage();
			addTracabiliteRetour(savedRR, "KO", msg);
		}
	}

	public void addTracabiliteRetour(LotSituationRcar r, String status, String msg) {
		retourHistoRepo.save(new RetourTracabilite(null, r, status, msg, new Date()));

	}

	private void saveTraceEnvoiRetour(Date dateCreation, LotSituationPrsRcarDto req, LotSituationPrsRcarDto rep,
			Long idLot) {
		TraceEnvoiRetour traceEnvoi = new TraceEnvoiRetour(null, getclob(req.toString()), null, dateCreation, null);
		traceEnvoi.setIdRetour(idLot);
		traceEnvoi.setDateRsu(Calendar.getInstance().getTime());
		traceEnvoi.setRetourRsu(rep != null ? getclob(rep.toString()) : null);
		traceEnvoiRetourRepository.save(traceEnvoi);
	}

	private Clob getclob(String val) {
		try {
			return new javax.sql.rowset.serial.SerialClob(val.toCharArray());
		} catch (SerialException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void executeWork(Long idLot, String type) {
		 if(type == null || !type.equalsIgnoreCase("RCAR")) {
			 return;
		 }
		try {
			traiterDemandes(idLot);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
