package ma.cdgp.af.ControllerThymeleaf;


import ma.cdgp.af.Schedulers.*;
import ma.cdgp.af.dto.af.BatchData;
import ma.cdgp.af.dto.af.PartenaireEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeBatchsControllerThym {

    @Autowired
    private CollectSchedulerService collectSchedulerService;

    @Autowired
    private CnssSchedulerService cnssSchedulerService;

    @Autowired
    private CmrSchedulerService cmrSchedulerService;


    @Autowired
    private TgrSchedulerService tgrSchedulerService;

    @Autowired
    private MiSchedulerService miSchedulerService;

    @Autowired
    private MassarSchedulerService massarSchedulerService;

    @Autowired
    private SanteSchedulerService santeSchedulerService;

    @Autowired
    private DemandesRsuSchedulerService demandesRsuSchedulerService;


    @Autowired
    NotificationTgrSchedulerService notificationTgrSchedulerService;

    @Autowired
    private Massar2025SchedulerService massar2025SchedulerService;

    @Autowired
    private RsuSchedulerService rsuService;

    @GetMapping("/batchs")
    String batchs (Model model) {
        List<BatchData> datas = new ArrayList<>();

        // Collect Candidat
        datas.add(new BatchData(99, "Collecter Candidat", PartenaireEnum.COLLECT.name(),
                collectSchedulerService.getDateLancement(),
                collectSchedulerService.isRunningCollect(),
                collectSchedulerService.getRateCheck(),
                collectSchedulerService.getMotif()));

        // CNSS check
        datas.add(new BatchData(1, "Collecter CNSS", PartenaireEnum.CNSS.name(),
                cnssSchedulerService.getDateLancementCheck(),
                cnssSchedulerService.isServiceRunningCheck(),
                cnssSchedulerService.getRateCheck(),
                cnssSchedulerService.getMotif()));

        // CNSS retry check
        datas.add(new BatchData(2, "Réessayer CNSS", PartenaireEnum.CNSS.name(),
                cnssSchedulerService.getDateLancementRetry(),
                cnssSchedulerService.isServiceRunningRetry(),
                cnssSchedulerService.getRateRetry(),
                cnssSchedulerService.getMotif()));


        // CMR check
        datas.add(new BatchData(3, "Collecter CMR", PartenaireEnum.CMR.name(),
                cmrSchedulerService.getDateLancementCheck(),
                cmrSchedulerService.isServiceRunningCheck(),
                cmrSchedulerService.getRateCheck(),
                cmrSchedulerService.getMotif()));

        // CMR retry check
        datas.add(new BatchData(4, "Réessayer CMR", PartenaireEnum.CMR.name(),
                cmrSchedulerService.getDateLancementRetry(),
                cmrSchedulerService.isServiceRunningRetry(),
                cmrSchedulerService.getRateRetry(),
                cmrSchedulerService.getMotif()));


        // TGR check
        datas.add(new BatchData(5, "Collecter TGR", PartenaireEnum.TGR.name(),
                tgrSchedulerService.getDateLancementCheck(),
                tgrSchedulerService.isServiceRunningCheck(),
                tgrSchedulerService.getRateCheck(),
                tgrSchedulerService.getMotif()));

        // TGR retry check
        datas.add(new BatchData(6, "Réessayer TGR", PartenaireEnum.TGR.name(),
                tgrSchedulerService.getDateLancementRetry(),
                tgrSchedulerService.isServiceRunningRetry(),
                tgrSchedulerService.getRateRetry(),
                tgrSchedulerService.getMotif()));







        // MASSAR check
        datas.add(new BatchData(7, "Collecter MASSAR", PartenaireEnum.MASSAR.name(),
                massarSchedulerService.getDateLancementCheck(),
                massarSchedulerService.isServiceRunningCheck(),
                massarSchedulerService.getRateCheck(),
                massarSchedulerService.getMotif()));

        // MASSAR retry check
        datas.add(new BatchData(8, "Réessayer MASSAR", PartenaireEnum.MASSAR.name(),
                massarSchedulerService.getDateLancementRetry(),
                massarSchedulerService.isServiceRunningRetry(),
                massarSchedulerService.getRateRetry(),
                massarSchedulerService.getMotif()));




        // Demandes RSU
        datas.add(new BatchData(9, "Collecter Les Demandes RSU", PartenaireEnum.RSU.name(),
                demandesRsuSchedulerService.getDateLancementCheck(),
                demandesRsuSchedulerService.isServiceRunningCheck(),
                demandesRsuSchedulerService.getRateCheck(),
                demandesRsuSchedulerService.getMotif()));



        // Notifications RSU
        datas.add(new BatchData(10, "RSU", PartenaireEnum.NOTIFICATION_RSU.name(), rsuService.getDateLancementCheck(),
                rsuService.isServiceRunningCheck(), rsuService.getRateCheck(), ""));



        datas.add(new BatchData(11, "Collecter SANTE", PartenaireEnum.SANTE.name(),
                santeSchedulerService.getDateLancementCheck(),
                santeSchedulerService.isServiceRunningCheck(),
                santeSchedulerService.getRateCheck(),
                santeSchedulerService.getMotif()));

        // SANTE retry check
        datas.add(new BatchData(12, "Réessayer SANTE", PartenaireEnum.SANTE.name(),
                santeSchedulerService.getDateLancementRetry(),
                santeSchedulerService.isServiceRunningRetry(),
                santeSchedulerService.getRateRetry(),
                santeSchedulerService.getMotif()));




        // MASSAR 2025
//        datas.add(new BatchData(13, "Collecter MASSAR - 2025", PartenaireEnum.MASSAR.name(),
//                massar2025SchedulerService.getDateLancementCheck(),
//                massar2025SchedulerService.isServiceRunningCheck(),
//                massar2025SchedulerService.getRateCheck(),
//                massar2025SchedulerService.getMotif()));
//
//        datas.add(new BatchData(14, "Réessayer MASSAR - 2025", PartenaireEnum.MASSAR.name(),
//                massar2025SchedulerService.getDateLancementRetry(),
//                massar2025SchedulerService.isServiceRunningRetry(),
//                massar2025SchedulerService.getRateRetry(),
//                massar2025SchedulerService.getMotif()));



        // SANTE check


        model.addAttribute("datas", datas);
        return "batchs";
    }


    @PostMapping("/startService/{id}")
    public ResponseEntity<String> startService(@PathVariable Integer id) {

        System.out.println("📩 Received request to start service with ID = " + id);

        switch(id) {
            case 1:
                cnssSchedulerService.startCnssCheck();
                break;
            case 2:
                cnssSchedulerService.startCnssCheckRetry();
                break;
            case 3:
                cmrSchedulerService.startCmrCheck();
                break;
            case 4:
                cmrSchedulerService.startCmrCheckRetry();
                break;
            case 5:
                tgrSchedulerService.startTgrCheck();
                break;
            case 6:
                tgrSchedulerService.startTgrCheckRetry();
                break;
            case 7:
                massarSchedulerService.startMassarCheck();
                break;
            case 8:
                massarSchedulerService.startMassarCheckRetry();
                break;
            case 9:
                demandesRsuSchedulerService.startDemadesRsuCheck();
                break;
            case 10:
                rsuService.startSent();
                break;
            case 11:
                santeSchedulerService.startSanteCheck();
                break;
            case 12:
                santeSchedulerService.startSanteCheckRetry();
                break;
            case 99:
                collectSchedulerService.startCollect();
                break;
            default:
                break;
        }
        return ResponseEntity.ok("Success");
    }







    @PostMapping("/stopService/{id}")
    public ResponseEntity<String> stopService(@PathVariable Integer id) {
        System.err.println("stopService");

        switch(id) {
            case 1:
                cnssSchedulerService.stopServiceCheck();
                break;
            case 2:
                cnssSchedulerService.stopServiceRetry();
                break;
            case 3:
                cmrSchedulerService.stopServiceCheck();
                break;
            case 4:
                cmrSchedulerService.stopServiceRetry();
                break;
            case 5:
                tgrSchedulerService.stopServiceCheck();
                break;
            case 6:
                tgrSchedulerService.stopServiceRetry();
                break;
            case 7:
                massarSchedulerService.stopServiceCheck();
                break;
            case 8:
                massarSchedulerService.stopServiceRetry();
                break;
            case 9:
                demandesRsuSchedulerService.stopServiceCheck();
                break;
            case 10:
                rsuService.stopServiceCheck();
                break;
            case 11:
                santeSchedulerService.stopServiceCheck();
                break;
            case 12:
                santeSchedulerService.stopServiceRetry();
                break;
            case 99:
                collectSchedulerService.stopServiceCollect();
                break;
            default:
                break;
        }
        return ResponseEntity.ok("Success");
    }
}
