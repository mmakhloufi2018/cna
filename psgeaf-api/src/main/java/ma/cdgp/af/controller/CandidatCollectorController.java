package ma.cdgp.af.controller;

import ma.cdgp.af.dto.af.massar.LotSituationPrsMassarDto;
import ma.cdgp.af.entity.ParametrageCollection;
import ma.cdgp.af.repository.ParametrageRepo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import ma.cdgp.af.dto.af.cmr.LotSituationPrsCmrDto;
import ma.cdgp.af.service.RequestBuilderExecutor;

@RestController
@RequestMapping("/candidat")
public class CandidatCollectorController {

	@Autowired
	RequestBuilderExecutor executor;
	
	@Autowired
	ParametrageRepo paramRepo;

	@GetMapping("/start-collect-candidats")
	@Operation(summary = "Recupération des candidats non encore traités (Comparaison par id candidat) & lancement vérification des logs (CMR TGR CNSS SANTE)")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Succès", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = LotSituationPrsCmrDto.class)) }),
			@ApiResponse(responseCode = "400", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "404", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content) })
	public ResponseEntity<Boolean> startCollect() throws Exception {
		executor.collectCandidats();
		return ResponseEntity.status(HttpStatus.OK).body(true);
	}
	
 
	@GetMapping("/start-kos-cmr")
	@Operation(summary = "Relancer les Lots CMR KO")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Succès", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = LotSituationPrsCmrDto.class)) }),
			@ApiResponse(responseCode = "400", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "404", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content) })
	public ResponseEntity<Boolean> startKOCmrCollect() throws Exception {
		executor.checkCmrIntegKO();
		return ResponseEntity.status(HttpStatus.OK).body(true);
	}
	
	@GetMapping("/start-kos-cnss")
	@Operation(summary = "Relancer les lots cnss KO (intégration & recupération réponse)")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Succès", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = LotSituationPrsCmrDto.class)) }),
			@ApiResponse(responseCode = "400", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "404", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content) })
	public ResponseEntity<Boolean> startKOCnssCollect() throws Exception {
		executor.checkCnssIntegKO();
		executor.checkCnssRepKO();
		return ResponseEntity.status(HttpStatus.OK).body(true);
	}
	
	@GetMapping("/start-kos-tgr")
	@Operation(summary = "Relancer les lots TGR KO ")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Succès", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = LotSituationPrsCmrDto.class)) }),
			@ApiResponse(responseCode = "400", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "404", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content) })
	public ResponseEntity<Boolean> startKOTgrCollect() throws Exception {
		executor.checkTgrIntegKO();
		return ResponseEntity.status(HttpStatus.OK).body(true);
	}
	
	
	@GetMapping("/start-kos-mi")
	@Operation(summary = "Relancer les lots TGR KO ")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Succès", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = LotSituationPrsCmrDto.class)) }),
			@ApiResponse(responseCode = "400", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "404", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content) })
	public ResponseEntity<Boolean> startKOMiCollect() throws Exception {
		executor.checkMiIntegKO();
		return ResponseEntity.status(HttpStatus.OK).body(true);
	}

	@GetMapping("/start-kos-massar")
	@Operation(summary = "Relancer les Lots CMR KO")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Succès", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = LotSituationPrsMassarDto.class)) }),
			@ApiResponse(responseCode = "400", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "404", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content) })
	public ResponseEntity<Boolean> startKOMassarCollect() throws Exception {
		executor.checkMassarIntegKO();
		return ResponseEntity.status(HttpStatus.OK).body(true);
	}
	@GetMapping("/start-collect-missed-cnss")
	@Operation(summary = "Collection des données pour vérification situation")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Succès", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = LotSituationPrsCmrDto.class)) }),
			@ApiResponse(responseCode = "400", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "404", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content) })
	public ResponseEntity<Boolean> startMissedCollect() throws Exception {
		executor.runCnssCheck();
		return ResponseEntity.status(HttpStatus.OK).body(true);
	}


	 
	@GetMapping("/start-collect-missed-cmr")
	@Operation(summary = "Collection des données pour vérification situation")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Succès", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = LotSituationPrsCmrDto.class)) }),
			@ApiResponse(responseCode = "400", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "404", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content) })
	public ResponseEntity<Boolean> startMissedCmrCollect() throws Exception {
		executor.runCmrCheck();
		return ResponseEntity.status(HttpStatus.OK).body(true);
	}
	
	@GetMapping("/start-collect-missed-mi")
	@Operation(summary = "Collection des données pour vérification situation")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Succès", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = LotSituationPrsCmrDto.class)) }),
			@ApiResponse(responseCode = "400", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "404", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content) })
	public ResponseEntity<Boolean> startMissedMiCollect() throws Exception {
		executor.runMiCheck();
		return ResponseEntity.status(HttpStatus.OK).body(true);
	}
	
	

	@GetMapping("/start-collect-missed-tgr")
	@Operation(summary = "Collection des données pour vérification situation")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Succès", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = LotSituationPrsCmrDto.class)) }),
			@ApiResponse(responseCode = "400", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "404", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content) })
	public ResponseEntity<Boolean> startTgrMissedCollect() throws Exception {
		executor.runTgrCheck();
		return ResponseEntity.status(HttpStatus.OK).body(true);
	}
	
	@GetMapping("/start-collect-missed-massar")
	@Operation(summary = "Collection des données pour vérification situation")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Succès", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = LotSituationPrsCmrDto.class)) }),
			@ApiResponse(responseCode = "400", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "404", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content) })
	public ResponseEntity<Boolean> startMassarMissedCollect() throws Exception {
		executor.runMassarCheck();
		return ResponseEntity.status(HttpStatus.OK).body(true);
	}
	
	@GetMapping("/start-collect-missed-sante")
	@Operation(summary = "Collection des données pour vérification situation")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Succès", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = LotSituationPrsCmrDto.class)) }),
			@ApiResponse(responseCode = "400", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "404", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content) })
	public ResponseEntity<Boolean> startSanteMissedCollect() throws Exception {
		executor.runSanteCheck();
		return ResponseEntity.status(HttpStatus.OK).body(true);
	}
	
	
	
	@GetMapping("/all-params")
	@Operation(summary = "Collection des données pour vérification situation")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Succès", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = LotSituationPrsCmrDto.class)) }),
			@ApiResponse(responseCode = "400", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "404", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content) })
	public ResponseEntity<List<ParametrageCollection>> params() throws Exception {
		return ResponseEntity.status(HttpStatus.OK).body(paramRepo.findAll());
	}
	
}
