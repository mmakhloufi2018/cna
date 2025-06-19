package ma.cdgp.af.esgaf;

import ma.cdgp.af.SqlQueries;
import ma.cdgp.af.dto.af.cmr.BenefNotificationCmrDto;
import ma.cdgp.af.dto.af.notifRsu.NotificationRsuInfo;
import ma.cdgp.af.dto.af.rsu.DemandeRsuDto;
import ma.cdgp.af.dto.af.tgr.BenefNotificationTgrDto;
import ma.cdgp.af.entity.NotificationCollected;
import ma.cdgp.af.utils.DataMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

@Repository
public class EsgeafRepository extends JdbcDaoSupport {

    @Autowired
    @Qualifier("primaryDataSource")
    DataSource dataSource;

    @Autowired
    private SqlQueries queries;

    Logger log = LoggerFactory.getLogger(EsgeafRepository.class);

    public EsgeafRepository(DataSource dataSource) {
        setDataSource(dataSource);
    }


    public Set<BenefNotificationTgrDto> getAllCinAndStatut(int page, int size) throws SQLException {
        int offset = page * size;
        int endRow = offset + size;


        String sql = 
                "  SELECT n.CIN as cin,\n" +
                "         n.active as statut,\n" +
                "         ROW_NUMBER() OVER (ORDER BY n.CIN) as rn\n" +
                "  FROM PSGEAF.NOTIFICATION_COLLECTED n\n" +
                "  WHERE n.CIN IS NOT NULL and n.ACTIVE is not null\n" +
                ")\n" +
                "WHERE rn BETWEEN ? AND ?";

        QueryRunner queryRunner = new QueryRunner(this.dataSource);

        Object[] params = {offset+1, endRow};

        List<BenefNotificationTgrDto> list = new ArrayList<>();
      //  System.out.println("Executing SQL: " + sql + " with params=[endRow=" + endRow + ", startRow=" + offset + "]");

        try {
             list = queryRunner.query(sql, new BeanListHandler<>(BenefNotificationTgrDto.class), params);

		} catch (Exception e) {
			e.getStackTrace();
			e.getMessage();
			System.out.println("======exception lors d'execution de la requete cin");
		}

        return new HashSet<>(list);
    }

    public Set<NotificationCollected> getAllData(int page, int size) throws SQLException {
        int offset = page * size;
        int endRow = offset + size;


        String sql = "SELECT * FROM ( " +
                "  SELECT a.*, ROWNUM rnum FROM ( " +
                "    SELECT ID as id, CIN as cin, ACTIVE as active, IDCS as idcs, MOIS_ANNEE as moisAnnee , flag as flag" +
                "    SELECT ID as id, CIN as cin, ACTIVE as active, IDCS as idcs, MOIS_ANNEE as moisAnnee" +
                "    FROM PSGEAF.NOTIFICATION_COLLECTED " +
                "  	 WHERE FLAG is null "+
                "    ORDER BY ID " +
                "  ) a " +
                "  WHERE FLAG is null and ROWNUM <= ? " +
                ") WHERE rnum > ?";

        Object[] params = {endRow, offset};
        QueryRunner queryRunner = new QueryRunner(this.dataSource);
      //  System.out.println("Executing SQL: " + sql + " with params=[endRow=" + endRow + ", startRow=" + offset + "]");
        List<NotificationCollected> list =  new ArrayList<>();
        try {
            list = queryRunner.query(sql, new BeanListHandler<>(NotificationCollected.class), params);

	} catch (Exception e) {
		e.getStackTrace();
		e.getMessage();
		System.out.println("======exception lors d'execution de la requete NotificationCollected");

	}
        Set<NotificationCollected> notifications = new HashSet<>(list);
        return notifications;
    }



    public Set<BenefNotificationCmrDto> getAllCinAndStatutCmr() throws SQLException {
        String sql = "SELECT n.CIN as cin, n.IDCS as idcs, n.ACTIVE AS statut FROM ESGEAF.NOTIFICATION n";
        QueryRunner queryRunner = new QueryRunner(this.dataSource);
        List<BenefNotificationCmrDto> list = queryRunner.query(sql, new BeanListHandler<>(BenefNotificationCmrDto.class));
        Set<BenefNotificationCmrDto> notifications = new HashSet<>(list);
        return notifications;
    }



    public Set<DemandeRsuDto> getDemandesRsuEsgeaf(int offset, int pageSize, int instanceId) throws SQLException {
        String sql = "SELECT c.IDCS as idcs, " +
                "TO_CHAR(c.DATE_NAISSANCE, 'DD/MM/YYYY') as dateNaissance, " +
                "c.CHEF_MENAGE as chefMenage, " +
                "c.GENRE as genre " +
                "FROM COLLECTED_DEMANDES_RSU c " +
                "WHERE c.FLAG IS NULL OR c.FLAG = 0 " +
                "AND MOD(c.id, 4) = ? " +
                "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        QueryRunner queryRunner = new QueryRunner(this.dataSource);
        Object[] params = {instanceId, offset, pageSize};
        List<DemandeRsuDto> list = queryRunner.query(sql, new BeanListHandler<>(DemandeRsuDto.class), params);
        return new HashSet<>(list);
    }
    

	public Map<String, Object> flagNotifTGRCollected(List<Long> codes, int flag) throws SQLException {
		System.out.println("===============updating flag notification collected=======");
		Map<String, Object> res = new HashMap<String, Object>();
		try {
			String updateQuery = "UPDATE PSGEAF.NOTIFICATION_COLLECTED N set N.FLAG = "+flag+" WHERE ID IN ";

			
			String inClausePlaceholders = codes.stream()
	                .map(id -> "?")
	                .collect(Collectors.joining(","));
	        updateQuery += "(" + inClausePlaceholders + ")";
	        //System.out.println(updateQuery);
			QueryRunner queryRunner = new QueryRunner(this.dataSource);
			 queryRunner.execute(updateQuery, new ScalarHandler<Long>(), codes.toArray());
			res.put("statut", "OK");
		} catch (Exception e) {
			e.printStackTrace();
			res.put("statut", "KO");
			res.put("error", e.getMessage());
		}
		return res;
	}

}
