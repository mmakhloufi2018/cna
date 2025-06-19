package ma.cdgp.af.client;


import ma.cdgp.af.dto.af.notifRsu.LotAcquittementRsuDto;
import ma.cdgp.af.dto.af.notifRsu.LotNotificationRsuDto;
import ma.cdgp.af.feign.FeignClientConfiguration;
import ma.cdgp.af.oauth2.JwtResponseRsuResponse;
import ma.cdgp.af.oauth2.LoginRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "rsu-api", url = "${rsu.api.url}", configuration = FeignClientConfiguration.class)
public interface RsuApiClient {


	@PostMapping(value = "/authentification", consumes = MediaType.APPLICATION_JSON_VALUE)
	JwtResponseRsuResponse authenticateToRsu(@RequestBody LoginRequest loginRequest, @RequestHeader("X-Client-ID") String clientId,
											 @RequestHeader("X-Request-ID") String requestID, @RequestHeader("X-Request-Application-Name") String appNam,
											 @RequestHeader("Authorization") String token);
	
	@PostMapping("/beneficiaires")
	public ResponseEntity<LotAcquittementRsuDto> sendLotBenefsRsu(@RequestBody LotNotificationRsuDto benefPayLoad,
																  @RequestHeader("X-Client-ID") String clientId,
																  @RequestHeader("X-Request-ID") String requestID, @RequestHeader("X-Request-Application-Name") String appNam,
																  @RequestHeader("X-Token") String token, @RequestHeader("Authorization") String tokenBerear);
	
}
