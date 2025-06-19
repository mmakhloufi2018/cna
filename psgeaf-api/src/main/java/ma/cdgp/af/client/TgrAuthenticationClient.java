//package ma.cdgp.af.client;
//
//
//import ma.cdgp.af.feign.FeignClientConfiguration;
//import ma.cdgp.af.oauth2.JwtResponse;
//import ma.cdgp.af.oauth2.LoginRequestTgr;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//@FeignClient(name = "authenticationClient", url = "https://testadsltgr.tgr.gov.ma/testcnratgradslrest/api", configuration = FeignClientConfiguration.class)
//public interface TgrAuthenticationClient {
//
//    @PostMapping(value = "/eadsl/authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
//    JwtResponse authTgr(@RequestBody LoginRequestTgr loginRequest);
//}
