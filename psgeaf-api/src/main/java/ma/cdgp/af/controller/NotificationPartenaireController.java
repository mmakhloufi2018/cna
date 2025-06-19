package ma.cdgp.af.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import ma.cdgp.af.dto.af.cmr.LotSituationPrsCmrDto;
import ma.cdgp.af.dto.af.tgr.LotNotificationTgrDto;
import ma.cdgp.af.repository.ParametrageRepo;
import ma.cdgp.af.service.RequestBuilderExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
public class NotificationPartenaireController {


    @Autowired
    ParametrageRepo paramRepo;

    @Autowired
    RequestBuilderExecutor executor;








    @PostMapping("/start-tgr")
    @Operation(summary = "Collection des données pour vérification situation")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Succès", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = LotNotificationTgrDto.class)) }),
            @ApiResponse(responseCode = "400", description = "La syntaxe de la requête est erronée", content = @Content),
            @ApiResponse(responseCode = "404", description = "La syntaxe de la requête est erronée", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content) })
    public ResponseEntity<Boolean> startTgrCollection() throws Exception {
        executor.runNotifTgr();
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }
}
