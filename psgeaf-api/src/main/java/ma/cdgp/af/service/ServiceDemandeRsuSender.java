package ma.cdgp.af.service;


import io.jsonwebtoken.lang.Assert;
import ma.cdgp.af.client.DemandeRsuClient;
import ma.cdgp.af.dto.af.rsu.AquittementDemandeRsuDto;
import ma.cdgp.af.dto.af.rsu.DemandeRsuDto;
import ma.cdgp.af.dto.af.rsu.LotDemandeRsuDto;
import ma.cdgp.af.dto.af.tgr.AcquittementBenefNotificationTgrDto;
import ma.cdgp.af.dto.af.tgr.LotNotificationTgrDto;
import ma.cdgp.af.entity.RetourTracabiliteNotification;
import ma.cdgp.af.entity.TraceEnvoiRetourNotification;
import ma.cdgp.af.entity.massar.LotSituationMassar;
import ma.cdgp.af.entity.rsu.*;
import ma.cdgp.af.entity.tgr.LotNotifTgr;
import ma.cdgp.af.entity.tgr.NotificationPartenaire;
import ma.cdgp.af.oauth2.*;
import ma.cdgp.af.repository.*;
import ma.cdgp.af.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;

import javax.net.ssl.*;
import javax.sql.rowset.serial.SerialException;
import javax.transaction.Transactional;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ServiceDemandeRsuSender {


    @Autowired
    JwtUtilsRsu jwtUtils;

    @Autowired
    DemandeRsuRepository demandeRsuRepository;

    @Autowired
    DetailsDemandeRsuRepository detailsDemandeRsuRepository;

    @Autowired
    LotDemandeRsuRepository lotDemandeRsuRepository;



    @Autowired
    DemandeRsuClient demandeRsuClient;

    @Autowired
    TraceEnvoiRetourDemandeRsuRepository traceEnvoiRetourDemandeRsuRepository;

    @Autowired
    RetourTracabiliteDemandesRsuRepository retourTracabiliteDemandesRsuRepository;

    @Autowired
    CollectedDemandesRsuRepository collectedDemandesRsuRepository;



    @Value("${rsu.demandes.X-Client-ID}")
    private String xClientId;

    @Value("${rsu.demandes.X-Client-Secret}")
    private String xClientSecret;


    @Value("${rsu.demandes.X-Application-Name}")
    private String xRequestApplicationName;

    @Value("${rsu_login_user}")
    private String rsuUsername;

    @Value("${rsu_login_cle_password}")
    private String rsuPasswordRaw;

    @Value("${rsu_clientId_authenticate}")
    private String rsuClientId;



    @Value("${rsu.demandes.X-Client-Secret}")
    private String clientSecret;

    @Value("${rsu.sec.grantType}")
    private String grantType;

    @Value("${rsu.sec.acessTokenUri}")
    private String acessTokenUri;

    @Value("${rsu.demandes.scope}")
    private String scope;



   @Transactional
    public void sendDemandesRsu(Long IdRsu, LotDemandeRsuDto dto, String type) throws Exception {

//        System.err.println("\033[31mprocessing " + dto.getDemandes().size() + " \033[0m demandes");
        Assert.notNull(IdRsu,"Lot vide");
        Assert.notNull(dto,"Lot vide");
       LotDemandeRsu lot = lotDemandeRsuRepository.findById(IdRsu).orElse(null);
       Date dateCreationReq = Calendar.getInstance().getTime();
        LoginRequestRsu logingToRsu = new LoginRequestRsu();
        String password = jwtUtils.getPassword(rsuPasswordRaw);
        logingToRsu.setPassword(password);
        logingToRsu.setUserName(rsuUsername);
       String tokenAuth2 = jwtUtils.tokenRsuAcess();
       JwtResponseRsu response = demandeRsuClient.authenticateToRsu(logingToRsu, rsuClientId,
                "PS", "Bearer " + tokenAuth2);
        if (response != null && response.getToken() != null && response.getToken().getAccessToken() != null) {
            Set<DemandeRsu> demandeRsuSet = dto.getDemandes().stream().map(d -> {
                DemandeRsu demandeRsu = DemandeRsu.from(d);
                demandeRsu.setLot(lot);
                return demandeRsu;
            }).collect(Collectors.toSet());
            lot.setDemandes(demandeRsuSet);
            LotDemandeRsu saved = lotDemandeRsuRepository.save(lot);
            saved.setIdTransaction(dto.getIdTransaction());
            saved.setDateCreation(new Date());
            String xToken = response.getToken().getAccessToken();
            ResponseEntity<AquittementDemandeRsuDto> acq = demandeRsuClient.askRsu(dto,xClientId,xRequestApplicationName,xToken,"Bearer "+tokenAuth2);
            saveTraceEnvoiRetour(dateCreationReq, dto, acq.getBody(), saved.getIdLot(), "RSU");
            if (acq.getBody() != null) {
                saved.setPartenaire("RSU");
                saved.setDateReponse(new Date());
                saved.setEtatLot("REP_OK");
                saved.setCodeRetour("200");
                saved.setMessageRetour("OK");
                Set<DetailsDemandeRsu> detailsDemandeRsuSet = new HashSet<>();
                acq.getBody().getDemandes().stream().map(d -> {
                    DetailsDemandeRsu detailsDemandeRsu;
                    detailsDemandeRsu = DetailsDemandeRsu.from(d);
                    detailsDemandeRsu.setLot(saved);
                    detailsDemandeRsuSet.add(detailsDemandeRsu);
                    return detailsDemandeRsu;
                }).collect(Collectors.toSet());
                saved.setPersonnes(detailsDemandeRsuSet);
                lotDemandeRsuRepository.save(saved);
                addTracabiliteRetour(saved,"OK","SENT");
            }else {
                System.err.println("NO RESPONSE");
                addTracabiliteRetour(saved,"KO","FAILED");
            }
        }else System.err.println("AUTH KO");

    }


    public void addTracabiliteRetour(LotDemandeRsu r, String status, String msg) {
        retourTracabiliteDemandesRsuRepository.save(new RetourTracabiliteDemandeRsu(null, r, status, msg, new Date()));

    }

    private void saveTraceEnvoiRetour(Date dateCreation, LotDemandeRsuDto req, AquittementDemandeRsuDto rep, Long idLot, String partenaire) {
        TraceEnvoiRetourDemandeRsu traceEnvoi = new TraceEnvoiRetourDemandeRsu(null, getclob(req.toString()), null,
                dateCreation, null, partenaire);
        traceEnvoi.setIdRetour(idLot);
        traceEnvoi.setDatePartenaire(Calendar.getInstance().getTime());
        traceEnvoi.setRetourPartenaire(rep != null ? getclob(rep.toString()) : null);
        traceEnvoiRetourDemandeRsuRepository.save(traceEnvoi);
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




    public String tokenAcess() throws Exception {
        final ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();
        resourceDetails.setClientId(xClientSecret);
        resourceDetails.setClientSecret(clientSecret);
        resourceDetails.setGrantType(grantType);
        resourceDetails.setAccessTokenUri(acessTokenUri);
        resourceDetails.setScope(Arrays.asList(scope));
//		resourceDetails.setAuthenticationScheme(AuthenticationScheme.form);
        disableSslVerification();
        final OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(resourceDetails);
        final OAuth2AccessToken accessToken = oAuth2RestTemplate.getAccessToken();
        final String accessTokenAsString = accessToken.getValue();
        return accessTokenAsString;
    }

    public static void disableSslVerification() {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            } };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }



}
