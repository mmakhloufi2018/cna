package ma.cdgp.af.client;


import ma.cdgp.af.dto.af.cmr.AcquittementBenefNotificationCmrDto;
import ma.cdgp.af.dto.af.cmr.LotNotificationCmrDto;
import ma.cdgp.af.feign.FeignClientConfiguration;
import ma.cdgp.af.oauth2.JwtResponse;
import ma.cdgp.af.oauth2.LoginRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "notification-cmr-client", url = "https://pmg.cmr.gov.ma:8082/api/af/",configuration = FeignClientConfiguration.class)
public interface NotificationCmrClient {

    @PostMapping(value = "/Session/Create", produces = { MediaType.APPLICATION_JSON_VALUE })
    JwtResponse authCmr(@RequestBody LoginRequest loginRequest);

    @PostMapping("/v1/poc/demandes/beneficiaire")
    ResponseEntity<AcquittementBenefNotificationCmrDto> sendLotBenefPartenaire(@RequestBody LotNotificationCmrDto benefs, @RequestHeader("Authorization") String token);
}
