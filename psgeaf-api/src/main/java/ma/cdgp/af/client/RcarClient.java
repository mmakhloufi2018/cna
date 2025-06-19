package ma.cdgp.af.client;

import java.util.List;
import java.util.Set;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import ma.cdgp.af.dto.af.rcar.DetailsLotSituationPrsRcarDto;
import ma.cdgp.af.feign.FeignClientConfiguration;

@FeignClient(name = "${rcar.fa.service.name}", url = "${rcar.fa.url}", configuration = FeignClientConfiguration.class)
public interface RcarClient {

	
	@GetMapping(value = "/echange/verification", consumes = MediaType.APPLICATION_JSON_VALUE)
	Set<DetailsLotSituationPrsRcarDto> askRcar();
	
	
}