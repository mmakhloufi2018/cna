package ma.cdgp.af.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import ma.cdgp.af.dto.af.sante.LotSituationPrsSanteDto;
import ma.cdgp.af.feign.FeignClientConfiguration;

@FeignClient(name = "${sante.fa.service.name}", url = "${sante.fa.url}", configuration = FeignClientConfiguration.class)
public interface SanteClient {

		
	@PostMapping(value = "/ValidatedFile/GetEntity", consumes = MediaType.APPLICATION_JSON_VALUE)
	LotSituationPrsSanteDto askSante(@RequestBody LotSituationPrsSanteDto lot);
	
	
}