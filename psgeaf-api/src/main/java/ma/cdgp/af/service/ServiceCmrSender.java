package ma.cdgp.af.service;


import ma.cdgp.af.client.NotificationCmrClient;

import ma.cdgp.af.dto.af.cmr.AcquittementBenefNotificationCmrDto;
import ma.cdgp.af.dto.af.cmr.LotNotificationCmrDto;
import ma.cdgp.af.dto.af.tgr.AcquittementBenefNotificationTgrDto;
import ma.cdgp.af.entity.cmr.LotNotifCmr;
import ma.cdgp.af.entity.tgr.NotificationPartenaire;
import ma.cdgp.af.esgaf.EsgeafRepository;
import ma.cdgp.af.oauth2.JwtResponse;
import ma.cdgp.af.oauth2.JwtUtils;
import ma.cdgp.af.oauth2.LoginRequest;
import ma.cdgp.af.repository.LotNotifCmrRepository;
import ma.cdgp.af.repository.NotificationPartenaireRepository;
import ma.cdgp.af.repository.RetourTracabiliteNotificationRepository;
import ma.cdgp.af.repository.TraceEnvoiRetourNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class ServiceCmrSender {

    @Value("${cmr.fa.cle.pass.auth.notif}")
    private String clePass;

    @Value("${cmr.fa.user.pass}")
    private String userAuth;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    EsgeafRepository repoEseag;

    @Autowired
    LotNotifCmrRepository lotNotifCmrRepository;


    @Autowired
    NotificationCmrClient notificationCmrClient;

    @Autowired
    NotificationPartenaireRepository notificationPartenaireRepo;

    @Autowired
    TraceEnvoiRetourNotificationRepository traceEnvoiRetourNotificationRepository;

    @Autowired
    RetourTracabiliteNotificationRepository retourTracabiliteNotificationRepository;




//    public void sendNotifToPartenaireCmr(LotNotificationCmrDto lot) throws SQLException {
//        LoginRequest loginRequestCmr = new LoginRequest();
//        loginRequestCmr.setUserName(userAuth);
//        loginRequestCmr.setPassword(clePass);
//        JwtResponse jwtauthOk = notificationCmrClient.authCmr(loginRequestCmr);
//        if(jwtauthOk != null && jwtauthOk.getToken() != null && jwtauthOk.getToken().getAccessToken() != null) {
//            Date dateCreationReq = Calendar.getInstance().getTime();
//            LotNotifCmr lotNotifCmr = LotNotificationCmrDto.fromDto(lot);
//            lotNotifCmr.setters();
//            LotNotifCmr saved = lotNotifCmrRepository.save(lotNotifCmr);
//            ResponseEntity<AcquittementBenefNotificationCmrDto> acq = notificationCmrClient.sendLotBenefPartenaire(lot,"Bearer "+jwtauthOk.getToken().getAccessToken());
//            saveTraceEnvoiRetour(dateCreationReq, lot, acq.getBody()., saved.getIdLot(), "CMR");
//            if(acq.getBody() != null) {
//                saved.setPersonnes(acq.getBody().get() != null
//                        ? acq.getBody().getBeneficiareNonEnregistrer().stream().map(t -> {
//                    DetailsNotifTgr d = new DetailsNotifTgr();
//                    d.setCin(t.getCin() != null ? t.getCin() : null);
//                    d.setCodeRetour(t.getCodeMotif());
//                    d.setMessageRetour(t.getLibeMotif());
//                    return d;
//                }).collect(Collectors.toSet()) : null);
//                saved.setPartenaire("TGR");
//                saved.setDateReponse(new Date());
//                saved.setEtatLot("REP_OK");
//                NotificationPartenaire notificationPartenaire = new NotificationPartenaire();
//                notificationPartenaire.setPartenaire("TGR");
//                notificationPartenaire.setEtat("SENT");
//                notificationPartenaire.setMotif("OK");
//                lot.getListeBeneficiaires().forEach(l -> {
//                    notificationPartenaire.setCin(l.getCin());
//                    notificationPartenaire.setActive(String.valueOf(l.getStatut()));
//                });
//                lotNotifTgrRepo.save(saved);
//                notificationPartenaireRepo.save(notificationPartenaire);
//                addTracabiliteRetour(saved, acq.getBody().getLibeMotif(), acq.getBody().getCodeMotif());
//            }else {
//                System.err.printf("NO RESPONSE");
//                addTracabiliteRetour(saved, acq.getBody().getLibeMotif(), acq.getBody().getCodeMotif());
//            }
//        } else System.err.printf("AUTH KO");
//    }






}
