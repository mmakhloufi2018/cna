package ma.cdgp.af.client;


import ma.cdgp.af.dto.af.tgr.AcquittementBenefNotificationTgrDto;
import ma.cdgp.af.dto.af.tgr.LotNotificationTgrDto;
import ma.cdgp.af.feign.FeignClientConfiguration;
import ma.cdgp.af.oauth2.JwtResponse;
import ma.cdgp.af.oauth2.LoginRequestTgr;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "notification-tgr-client",url="${tgr.fa.url}", configuration = FeignClientConfiguration.class)
public interface NotificationTgrClient {

    @PostMapping(value = "/authenticate", produces = { MediaType.APPLICATION_JSON_VALUE })
     JwtResponse authTgr(@RequestBody LoginRequestTgr loginRequest);

    @PostMapping("/beneficiaires")
     ResponseEntity<AcquittementBenefNotificationTgrDto> sendLotBenefPartenaire(@RequestBody LotNotificationTgrDto benefs, @RequestHeader("Authorization") String token);
}

//
//
//
////https://testadsltgr.tgr.gov.ma/testcnratgradslrest/api/eadsl/authenticate
////https://testeaftgr.tgr.gov.ma/testcnratgrafrest/api/eaf/beneficiaires
