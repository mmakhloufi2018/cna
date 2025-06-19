package ma.cdgp.af.service;

import ma.cdgp.af.client.RsuApiClient;

import ma.cdgp.af.dto.af.notifRsu.BenefNotificationRsu;
import ma.cdgp.af.dto.af.notifRsu.LotAcquittementRsuDto;
import ma.cdgp.af.dto.af.notifRsu.LotNotificationRsuDto;
import ma.cdgp.af.dto.af.notifRsu.NotificationRsuInfo;
import ma.cdgp.af.entity.notifRsu.DetailsNotificationRsu;
import ma.cdgp.af.entity.notifRsu.LotNotifRsu;
import ma.cdgp.af.esgaf.EsgeafRepository;
import ma.cdgp.af.oauth2.JwtResponseRsuResponse;
import ma.cdgp.af.oauth2.JwtUtils;
import ma.cdgp.af.oauth2.LoginRequest;
import ma.cdgp.af.repository.CandidatsRepository;
import ma.cdgp.af.repository.LotNotifRsuRepo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceRsuSender {
	@Autowired
	JwtUtils jwtUtils;
	@Autowired
	RsuApiClient rsuService;
	@Autowired
	LotNotifRsuRepo lotNotifRsuRepo;
	@Autowired
	CandidatsRepository repoEseag;

	@Value("${rsu_login_user}")
	private String rsuUsername;

	@Value("${rsu_login_cle_password}")
	private String rsuPasswordRaw;

	@Value("${rsu_clientId_authenticate}")
	private String rsuClientId;

	public void sendNotifToRsu(List<NotificationRsuInfo> lotToSend) {
		// get token
		LoginRequest logingToRsu = new LoginRequest();
		String password = jwtUtils.getPassword(rsuPasswordRaw);
		logingToRsu.setPassword(password);
		logingToRsu.setUserName(rsuUsername);
		System.out.println("Login - Username : " + logingToRsu.getUserName() + " Login - Password : " + logingToRsu.getPassword() );
		try {
			String uuid = jwtUtils.generateUniqueId();
			System.out.println("UUID : " + uuid);
			String tokenAuth2 = jwtUtils.tokenRsuAcess();
			System.err.println(tokenAuth2);
			System.err.println("connecting to rsu");
			JwtResponseRsuResponse jwtResponseRsu = rsuService.authenticateToRsu(logingToRsu, rsuClientId, uuid,
					"PS", "Bearer " + tokenAuth2);
			if (jwtResponseRsu != null && jwtResponseRsu.getToken() != null
					&& jwtResponseRsu.getToken().getAccessToken() != null) {
				System.err.println("connected");
				// connected
				LotNotificationRsuDto lot = new LotNotificationRsuDto();
				lot.setIdTransaction(Calendar.getInstance().getTimeInMillis());
				lot.setListeBeneficiaires(new HashSet<>());
				for (NotificationRsuInfo d : lotToSend) {
					BenefNotificationRsu benef = new BenefNotificationRsu();
					try {
						benef.setActif(BooleanUtils.toBooleanObject(d.getActif()));
					} catch (Exception e) {
						benef.setActif(d.getActif() != null && d.getActif().equalsIgnoreCase("true") ? true : false);
					}
					benef.setBeneficiaireId(Long.valueOf(d.getId()));
					benef.setDateDebut(d.getDateEffet());
					benef.setDateFin(d.getDateFin());
					benef.setDateNaissance(d.getDateNaissanceChar());
					benef.setGenre(d.getGenre() != null ? d.getGenre() : "M");
					benef.setIdcs(Long.valueOf(d.getIdcs()));
					benef.setMontant(Double.valueOf(d.getMontant()));
					benef.setMotif(d.getMotif());
					lot.getListeBeneficiaires().add(benef);
				}
				LotNotifRsu lotToSave = LotNotificationRsuDto.fromDto(lot);
				lotToSave.setters();
				LotNotifRsu saved = lotNotifRsuRepo.save(lotToSave);
				ResponseEntity<LotAcquittementRsuDto> acq = rsuService.sendLotBenefsRsu(lot,
						rsuClientId, uuid, "PS", jwtResponseRsu.getToken().getAccessToken(),
						"Bearer " + tokenAuth2);
				if (acq.getBody() != null && acq.getBody().getListeBeneficiaires() != null) {
					saved.setDateReponse(new Date());
					saved.setPersonnes(acq.getBody().getListeBeneficiaires() != null
							? acq.getBody().getListeBeneficiaires().stream().map(t -> {
								DetailsNotificationRsu d = new DetailsNotificationRsu();
								d.setBeneficiaireId(
										t.getBeneficiaireId() != null ? t.getBeneficiaireId().toString() : null);
								d.setCodeRetour(t.getCodeRetour());
								d.setMessageRetour(t.getMessageRetour());
								return d;
							}).collect(Collectors.toSet())
							: null);
					saved.setEtatLot("REP_OK");
					lotNotifRsuRepo.save(saved);
					repoEseag.flagImported(
							lotToSend.stream().map(t -> Long.valueOf(t.getId())).collect(Collectors.toList()));
				}
			} else {
				// not connected
				System.err.println("AUTH KO");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Runnable runTask() {
		return () -> {

			List<NotificationRsuInfo> data = null;
			try {
				data = repoEseag.getAllCodesMenage();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (CollectionUtils.isEmpty(data)) {
				System.err.println("Liste empty");
				return;
			}
			try {
				sendNotifToRsu(data);
			} catch (Exception e) {
				e.printStackTrace();
			}

		};
	}
}
