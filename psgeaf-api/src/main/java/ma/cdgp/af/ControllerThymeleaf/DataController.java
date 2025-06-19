package ma.cdgp.af.ControllerThymeleaf;

import ma.cdgp.af.dto.af.BatchData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;


@RestController
public class DataController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/refreshData")
    public Map<String, Object> refreshData() {
        String query1 = "SELECT count(1) AS nbr_candidats, count(distinct upper(cin)) AS nbr_cin, count(distinct massar) AS nbr_massar FROM AF_CANDIDAT_COLLECTED c WHERE c.ARCHIVE = 0";
        Map<String, Object> counts = jdbcTemplate.queryForMap(query1);

        int nbrCandidats = ((BigDecimal) counts.get("nbr_candidats")).intValue();
        int nbrCin = ((BigDecimal) counts.get("nbr_cin")).intValue();
        int nbrMassar = ((BigDecimal) counts.get("nbr_massar")).intValue();

        List<BatchData> batchDataList = new ArrayList<>();
        batchDataList.add(executeQuery("CNSS Demandes", "select 'CNSS Demandes' as partenaire, count(distinct upper(cin)) as nbr, max(l.date_reponse) as max_date_reponse from dmd_SITUATION_CNSS d, LOT_SITUATION_CNSS l where l.id=d.id_lot and l.date_creation >= trunc(sysdate,'MM')", nbrCin, false));
        batchDataList.add(executeQuery("CNSS Réponses", "select 'CNSS Reponses' as partenaire, count(distinct upper(cin)) as nbr, max(l.date_reponse) as max_date_reponse from DETAILS_LOT_SITUATION_CNSS d, LOT_SITUATION_CNSS l where l.id=d.id_lot and l.date_reponse >= trunc(sysdate,'MM')", nbrCin, false));
        batchDataList.add(executeQuery("CMR Demandes", "select 'CMR Demandes' as partenaire, count(distinct upper(cin)) as nbr, max(l.date_reponse) as max_date_reponse from DMD_SITUATION_CMR d, LOT_SITUATION_CMR l where l.id=d.id_lot and l.date_creation >= trunc(sysdate,'MM')", nbrCin, false));
        batchDataList.add(executeQuery("CMR Réponses", "select 'CMR Reponses' as partenaire, count(distinct upper(cin)) as nbr, max(l.date_reponse) as max_date_reponse from DETAILS_LOT_SITUATION_CMR d, LOT_SITUATION_CMR l where l.id=d.id_lot and l.date_reponse >= trunc(sysdate,'MM')", nbrCin, false));
        batchDataList.add(executeQuery("TGR Demandes", "select 'TGR Demandes' as partenaire, count(distinct upper(cin)) as nbr, max(l.date_reponse) as max_date_reponse from DMD_SITUATION_TGR d, LOT_SITUATION_TGR l where l.id=d.id_lot and l.date_creation >= trunc(sysdate,'MM')", nbrCin, false));
        batchDataList.add(executeQuery("TGR Réponses", "select 'TGR Reponses' as partenaire, count(distinct upper(cin)) as nbr, max(l.date_reponse) as max_date_reponse from DETAILS_LOT_SITUATION_TGR d, LOT_SITUATION_TGR l where l.id=d.id_lot and l.date_reponse >= trunc(sysdate,'MM')", nbrCin, false));
        batchDataList.add(executeQuery("MASSAR Demandes", "select 'MASSAR Demandes' as partenaire, count(distinct num_massar) as nbr, max(l.date_reponse) as max_date_reponse from DMD_SITUATION_MASSAR d, LOT_SITUATION_MASSAR l where l.id=d.id_lot and l.date_creation >= trunc(sysdate,'MM')", nbrMassar, false));
        batchDataList.add(executeQuery("MASSAR Réponses", "select 'MASSAR Reponses' as partenaire, count(distinct code_eleve) as nbr, max(l.date_reponse) as max_date_reponse from DETAILS_LOT_SITUATION_MASSAR d, LOT_SITUATION_MASSAR l where l.id=d.id_lot and l.date_reponse >= trunc(sysdate,'MM')", nbrMassar, false));

        Map<String, Object> response = new HashMap<>();
        response.put("totalCandidates", nbrCandidats);
        response.put("totalCins", nbrCin);
        response.put("totalMassars", nbrMassar);
        response.put("batchData", batchDataList);

        return response;
    }

    @GetMapping("/refreshRecapData")
    public Map<String, List<?>> refreshRecapData() {
        Map<String, List<?>> response = new HashMap<>();
        response.put("CMR", executeRecapQuery("CMR", "select to_char(trunc(l.date_reponse,'MM'),'MM/YYYY') mois, count(distinct upper(cin)) nbr, to_char(max(l.date_reponse),'DD/MM/YYYY HH24:MI') derniere_reponse, round(sum(COUVERTURE_AF)/count(1)*100,3) non_eligibles from DETAILS_LOT_SITUATION_CMR d, LOT_SITUATION_CMR l where l.id=d.id_lot and l.date_reponse is not null group by trunc(l.date_reponse,'MM') order by trunc(l.date_reponse,'MM')", RecapDTO.class));
        response.put("TGR", executeRecapQuery("TGR", "select to_char(trunc(l.date_reponse,'MM'),'MM/YYYY') mois, count(distinct upper(cin)) nbr, to_char(max(l.date_reponse),'DD/MM/YYYY HH24:MI') derniere_reponse, round(sum(COUVERTURE_AF)/count(1)*100,3) non_eligibles from DETAILS_LOT_SITUATION_TGR d, LOT_SITUATION_TGR l where l.id=d.id_lot and l.date_reponse is not null group by trunc(l.date_reponse,'MM') order by trunc(l.date_reponse,'MM')", RecapDTO.class));
        response.put("MASSAR", executeRecapQuery("MASSAR", "select to_char(trunc(l.date_reponse,'MM'),'MM/YYYY') mois, count(distinct code_eleve) nbr, to_char(max(l.date_reponse),'DD/MM/YYYY HH24:MI') derniere_reponse, round(sum(mod(1,SITUATION_SCOLARISATION))/count(1)*100,3) non_eligibles from DETAILS_LOT_SITUATION_MASSAR d, LOT_SITUATION_MASSAR l where l.id=d.id_lot and l.date_reponse is not null group by trunc(l.date_reponse,'MM') order by trunc(l.date_reponse,'MM')", RecapDTO.class));
        response.put("RSU", executeRecapQuery("RSU", "select to_char(trunc(l.DATE_REPONSE,'MM'),'MM/YYYY') mois, d.MSG_RETOUR msg_retour, count(1) nbr from DETAILS_DEMANDE_RSU d, LOT_DEMANDE_RSU l where l.id = d.id_lot group by trunc(l.DATE_REPONSE,'MM'), d.MSG_RETOUR order by trunc(l.DATE_REPONSE,'MM') desc, d.MSG_RETOUR desc", RecapRSUDTO.class));

        return response;
    }

    private <T> List<T> executeRecapQuery(String partenaire, String query, Class<T> type) {
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(type));
    }

    private BatchData executeQuery(String partenaire, String query, int nbrCandidats, boolean isDemande) {
        Map<String, Object> resultMap = jdbcTemplate.queryForMap(query);

        int nbr = ((BigDecimal) resultMap.get("nbr")).intValue();
        Timestamp dateReponseTimestamp = (Timestamp) resultMap.get("max_date_reponse");

        String dateReponse = null;
        if (dateReponseTimestamp != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            dateReponse = sdf.format(dateReponseTimestamp);
        }

        double percentage = (nbr != 0 ? nbr : 0) * 100.0 / nbrCandidats;

        BatchData data = new BatchData();
        data.setPartenaire(partenaire);
        data.setNbr(nbr);
        data.setDateReponse(dateReponse);
        data.setReponsesPourcentage(percentage);

        // Logging to verify the data
        System.out.println("Partenaire: " + partenaire);
        System.out.println("Number Total: " + nbr);
        System.out.println("Date Reponse: " + dateReponse);
        System.out.println("Demandes Pourcentage: " + (isDemande ? percentage : "N/A"));
        System.out.println("Reponses Pourcentage: " + (!isDemande ? percentage : "N/A"));

        return data;
    }

    @GetMapping("/refreshDataForPartenaire")
    public BatchData refreshDataForPartenaire(@RequestParam String partenaire) {
        String query1 = "SELECT count(1) AS nbr_candidats, count(distinct upper(cin)) AS nbr_cin, count(distinct massar) AS nbr_massar FROM AF_CANDIDAT_COLLECTED c WHERE c.ARCHIVE IS NULL";
        int nbrCandidats = ((BigDecimal) jdbcTemplate.queryForObject(query1, BigDecimal.class)).intValue();

        String query = "";
        boolean isDemande = false;
        switch (partenaire) {
            case "CNSS Demandes":
                query = "select 'CNSS Demandes' as partenaire, count(distinct upper(cin)) as nbr, max(l.date_reponse) as max_date_reponse from dmd_SITUATION_CNSS d, LOT_SITUATION_CNSS l where l.id=d.id_lot and l.date_creation >= trunc(sysdate,'MM')";
                isDemande = true;
                break;
            case "CNSS Réponses":
                query = "select 'CNSS Reponses' as partenaire, count(distinct upper(cin)) as nbr, max(l.date_reponse) as max_date_reponse from DETAILS_LOT_SITUATION_CNSS d, LOT_SITUATION_CNSS l where l.id=d.id_lot and l.date_reponse >= trunc(sysdate,'MM')";
                break;
            case "CMR Demandes":
                query = "select 'CMR Demandes' as partenaire, count(distinct upper(cin)) as nbr, max(l.date_reponse) as max_date_reponse from DMD_SITUATION_CMR d, LOT_SITUATION_CMR l where l.id=d.id_lot and l.date_creation >= trunc(sysdate,'MM')";
                isDemande = true;
                break;
            case "CMR Réponses":
                query = "select 'CMR Reponses' as partenaire, count(distinct upper(cin)) as nbr, max(l.date_reponse) as max_date_reponse from DETAILS_LOT_SITUATION_CMR d, LOT_SITUATION_CMR l where l.id=d.id_lot and l.date_reponse >= trunc(sysdate,'MM')";
                break;
            case "TGR Demandes":
                query = "select 'TGR Demandes' as partenaire, count(distinct upper(cin)) as nbr, max(l.date_reponse) as max_date_reponse from DMD_SITUATION_TGR d, LOT_SITUATION_TGR l where l.id=d.id_lot and l.date_creation >= trunc(sysdate,'MM')";
                isDemande = true;
                break;
            case "TGR Réponses":
                query = "select 'TGR Reponses' as partenaire, count(distinct upper(cin)) as nbr, max(l.date_reponse) as max_date_reponse from DETAILS_LOT_SITUATION_TGR d, LOT_SITUATION_TGR l where l.id=d.id_lot and l.date_reponse >= trunc(sysdate,'MM')";
                break;
            case "MASSAR Demandes":
                query = "select 'MASSAR Demandes' as partenaire, count(distinct num_massar) as nbr, max(l.date_reponse) as max_date_reponse from DMD_SITUATION_MASSAR d, LOT_SITUATION_MASSAR l where l.id=d.id_lot and l.date_creation >= trunc(sysdate,'MM')";
                isDemande = true;
                break;
            case "MASSAR Réponses":
                query = "select 'MASSAR Reponses' as partenaire, count(distinct code_eleve) as nbr, max(l.date_reponse) as max_date_reponse from DETAILS_LOT_SITUATION_MASSAR d, LOT_SITUATION_MASSAR l where l.id=d.id_lot and l.date_reponse >= trunc(sysdate,'MM')";
                break;
            default:
                throw new IllegalArgumentException("Invalid partenaire: " + partenaire);
        }

        if (query.isEmpty()) {
            throw new IllegalArgumentException("Query is empty for partenaire: " + partenaire);
        }

        return executeQuery(partenaire, query, nbrCandidats, isDemande);
    }
}
