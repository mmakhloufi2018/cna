package ma.cdgp.af.controller;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import ma.cdgp.af.client.RcarClient;
import ma.cdgp.af.dto.af.cmr.LotSituationPrsCmrDto;
import ma.cdgp.af.dto.af.rcar.LotSituationPrsRcarDto;
import ma.cdgp.af.entity.rcar.LotSituationRcar;
import ma.cdgp.af.repository.LotSituationRcarRepo;
import ma.cdgp.af.service.RequestRcarExecutor;

@RestController
@RequestMapping("/rcar")
public class RequestRcarController {

	@Autowired
	LotSituationRcarRepo lotSituationPersonneRepo;
 
	@Autowired
	RequestRcarExecutor requestRcarExec;

	@Autowired
	RcarClient rcarClient;

	@PostMapping("/situation-personnes")
	@Operation(summary = "Traitement demandes RCAR")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Succès", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = LotSituationPrsCmrDto.class)) }),
			@ApiResponse(responseCode = "400", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "404", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content) })
	public ResponseEntity<LotSituationPrsRcarDto> getSituationPersonnesCmr() throws Exception {
		LotSituationRcar requestEntity = new LotSituationRcar();
		requestEntity.setPartenaire("RCAR");
		requestEntity.setDateCreation(new Date());
		requestEntity.setEtatLot("CREATED");
		requestEntity.setLotId(String.valueOf(Calendar.getInstance().getTimeInMillis()));
		LotSituationRcar savedRR = lotSituationPersonneRepo.save(requestEntity);
		new Thread(() -> {
			try {
				requestRcarExec.traiterDemandes(savedRR.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
		return ResponseEntity.status(HttpStatus.OK).body(LotSituationPrsRcarDto.fromEntity(savedRR));
	}
}
