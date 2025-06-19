package ma.cdgp.af.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import ma.cdgp.af.dto.af.tgr.LotSituationPrsTgrDto;
import ma.cdgp.af.feign.FeignClientConfiguration;
import ma.cdgp.af.oauth2.JwtResponse;
import ma.cdgp.af.oauth2.LoginRequestTgr;

@FeignClient(name = "${tgr.fa.service.name}", url = "${tgr.fa.url}", configuration = FeignClientConfiguration.class)
public interface TGrClient {

	@PostMapping(value = "/authenticate", produces = { MediaType.APPLICATION_JSON_VALUE })
	public JwtResponse authTgr(@RequestBody LoginRequestTgr loginRequest);
	
	
	@PostMapping(value = "/situationPersonnes", consumes = MediaType.APPLICATION_JSON_VALUE)
	LotSituationPrsTgrDto askTgr(@RequestBody LotSituationPrsTgrDto lot,  @RequestHeader("Authorization") String token);
	
	
}