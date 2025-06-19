package ma.cdgp.af.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import ma.cdgp.af.client.MassarClient;
import ma.cdgp.af.dto.af.massar.LotSituationPrsMassarDto;
import ma.cdgp.af.dto.af.sante.LotSituationPrsSanteDto;
import ma.cdgp.af.entity.massar.LotSituationMassar;
import ma.cdgp.af.repository.LotSituationMassarRepo;
import ma.cdgp.af.service.RequestMassarExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;

@RestController
@RequestMapping("/massar")
public class RequestMassarController {

    @Autowired
    LotSituationMassarRepo lotSituationPersonneRepo;

    @Autowired
    RequestMassarExecutor requestMassarExecutor;

    @Autowired
    MassarClient massarClient;



    @PostMapping("/situation-personnes")
    @Operation(summary = "Traitement demandes MASSAR")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Succès", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = LotSituationPrsMassarDto.class)) }),
            @ApiResponse(responseCode = "400", description = "La syntaxe de la requête est erronée", content = @Content),
            @ApiResponse(responseCode = "404", description = "La syntaxe de la requête est erronée", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content) })
    public ResponseEntity<LotSituationPrsMassarDto> getSituationPersonnesMassar(@RequestBody LotSituationPrsMassarDto lotSituation) throws Exception {
        LotSituationMassar requestEntity = new LotSituationMassar();
        requestEntity.setPartenaire("MASSAR");
        requestEntity.setDateCreation(new Date());
        requestEntity.setEtatLot("CREATED");
        requestEntity.setLotId(String.valueOf(Calendar.getInstance().getTimeInMillis()));
        LotSituationMassar savedRR = lotSituationPersonneRepo.save(requestEntity);
        new Thread(() -> {
            try {
                requestMassarExecutor.traiterDemandes(savedRR.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        return ResponseEntity.ok().body(lotSituation);
    }

}
