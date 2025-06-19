package ma.cdgp.af.client;


import ma.cdgp.af.dto.af.massar.LotSituationPrsMassarDto;
import ma.cdgp.af.dto.af.rsu.AquittementDemandeRsuDto;
import ma.cdgp.af.dto.af.rsu.LotDemandeRsuDto;
import ma.cdgp.af.feign.FeignClientConfiguration;
import ma.cdgp.af.oauth2.JwtResponse;
import ma.cdgp.af.oauth2.JwtResponseRsu;
import ma.cdgp.af.oauth2.LoginRequest;
import ma.cdgp.af.oauth2.LoginRequestRsu;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
@FeignClient(name = "demande-rsu-client", url = "https://10.2.10.23/rsu-social-programs-services/v1/",configuration = FeignClientConfiguration.class)
public interface DemandeRsuClient {




    @PostMapping(value = "/authentification", consumes = MediaType.APPLICATION_JSON_VALUE)
    JwtResponseRsu authenticateToRsu(@RequestBody LoginRequestRsu loginRequest, @RequestHeader("X-Client-ID") String clientId
            , @RequestHeader("X-Request-Application-Name") String appNam,
                                     @RequestHeader("Authorization") String token);


    @PostMapping(value = "/demandes", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AquittementDemandeRsuDto> askRsu(@RequestBody LotDemandeRsuDto lot,
                                                   @RequestHeader("X-Client-ID") String clientId,
                                                   @RequestHeader("X-Request-Application-Name") String appNam,
                                                   @RequestHeader("X-Token") String xToken,
                                                   @RequestHeader("Authorization") String token );


}
