//package ma.cdgp.af.client;
//
//
//import ma.cdgp.af.dto.af.tgr.LotAcquittementTgrDto;
//import ma.cdgp.af.dto.af.tgr.LotNotificationTgrDto;
//import ma.cdgp.af.feign.FeignClientConfiguration;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestHeader;
//
//@FeignClient(name = "beneficiariesClient", url = "https://testeaftgr.tgr.gov.ma/testcnratgrafrest/api", configuration = FeignClientConfiguration.class)
//public interface TgrBeneficiariesClient {
//
//    @PostMapping(value = "/eaf/beneficiaires")
//    ResponseEntity<LotAcquittementTgrDto> sendLotBenefPartenaire(@RequestBody LotNotificationTgrDto benefs, @RequestHeader("Authorization") String token);
//}
