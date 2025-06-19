package ma.cdgp.af.repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import ma.cdgp.af.ControleInfos;
import ma.cdgp.af.dto.af.PartenaireEnum;
import ma.cdgp.af.dto.af.notifRsu.NotificationRsuInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import ma.cdgp.af.SqlQueries;
import ma.cdgp.af.dto.af.CandidatInfos;
import ma.cdgp.af.dto.af.CriteresTransactionDto;
import ma.cdgp.af.dto.af.DecesInfos;
import ma.cdgp.af.utils.DataMapper;

@Repository
public class CandidatsRepository extends JdbcDaoSupport {


	@Autowired
	@Qualifier("primaryDataSource")
	DataSource dataSourcePrincipal;

	@Autowired
	@Qualifier("esgeafdataSource")
	DataSource dataSource;

	@Autowired
	@Qualifier("paiementdataSource")
	DataSource paiementdataSource;

	@Autowired
	private SqlQueries queries;

	Logger log = LoggerFactory.getLogger(CandidatsRepository.class);

	public CandidatsRepository(DataSource dataSource) {
		setDataSource(dataSource);
	}

	public List<CandidatInfos> getAllCandidats(CriteresTransactionDto crt) throws SQLException {
		List<CandidatInfos> res = new ArrayList<>();
		String sql = queries.getQueries().get("fetch-candidats");
		int startRow = (crt.getPageNumber() / crt.getPageSize()) * crt.getPageSize() + 1;
		int endRow = (crt.getPageNumber() / crt.getPageSize() + 1) * crt.getPageSize();
		System.err.println(startRow + ":" + endRow);
		QueryRunner queryRunner = new QueryRunner(this.dataSource);
		List<Map<String, Object>> rows = queryRunner.query(sql, new MapListHandler(), endRow, startRow);
		DataMapper<CandidatInfos> mapper = new DataMapper<>(CandidatInfos.class);
		if (CollectionUtils.isNotEmpty(rows)) {
			for (Map<String, Object> map : rows) {
				res.add(mapper.map(map));
			}
		}
		return res;
	}
	
	public Map<String, Object> flagCandidatCollected(List<Long> idsCandida) throws SQLException {
		Map<String, Object> res = new HashMap<String, Object>();
		try {
			String updateQuery = "UPDATE  ESGEAF.candidat c set c.etat = 'COLLECTED' WHERE id IN ";
			String inClausePlaceholders = idsCandida.stream().map(id -> "?").collect(Collectors.joining(","));
			updateQuery += "(" + inClausePlaceholders + ")";
			QueryRunner queryRunner = new QueryRunner(this.dataSource);
			queryRunner.execute(updateQuery, new ScalarHandler<Long>(), idsCandida.toArray());
			res.put("statut", "OK");
		} catch (Exception e) {
			e.printStackTrace();
			res.put("statut", "KO");
			res.put("error", e.getMessage());
		}
		return res;
	}


	public List<DecesInfos> fetchDecesMi(List<String> cines) throws SQLException {
		List<DecesInfos> res = new ArrayList<>();
		String sql = "select * from DECES_MI d where d.CNIE is not null and upper(d.CNIE) in ";
		String inClausePlaceholders = cines.stream().map(c -> "?").collect(Collectors.joining(","));
		sql += "(" + inClausePlaceholders + ")";
		QueryRunner queryRunner = new QueryRunner(this.paiementdataSource);
		List<String> params = cines.stream().filter(y -> y != null).map(t -> t.toUpperCase())
				.collect(Collectors.toList());
		List<Map<String, Object>> rows = queryRunner.query(sql, new MapListHandler(), params.toArray());
		DataMapper<DecesInfos> mapper = new DataMapper<>(CandidatInfos.class);
		if (CollectionUtils.isNotEmpty(rows)) {
			for (Map<String, Object> map : rows) {
				res.add(mapper.map(map));
			}
		}
		return res;
	}



	public HashMap<String, Integer> fetchResultsStats() throws SQLException {
		HashMap<String, Integer> mapResults = new HashMap<>();
		String sql = "select count(c.id) TOTAL, sum(to_number(c.RESULTAT)) OK, count(c.id)-sum(to_number(c.RESULTAT)) KO from candidat c where c.etat like 'CHECKED'"
				+ "";
		QueryRunner queryRunner = new QueryRunner(this.dataSource);
		List<Map<String, Object>> rows = queryRunner.query(sql, new MapListHandler());
		DataMapper<ControleInfos> mapper = new DataMapper<>(ControleInfos.class);
		if (CollectionUtils.isNotEmpty(rows)) {
			for (Map<String, Object> map : rows) {
				mapResults.put("TOTAL", Integer.valueOf(map.get("TOTAL").toString()));
				mapResults.put("OK", Integer.valueOf(map.get("OK").toString()));
				mapResults.put("KO", Integer.valueOf(map.get("KO").toString()));
				break;
			}
		}
		return mapResults;
	}

	public HashMap<String, Integer> fetchCandidatStats() throws SQLException {
		HashMap<String, Integer> mapResults = new HashMap<>();
		String sql = "select sum(case when c.etat is null then 1 else 0 end) received,\r\n"
				+ "       sum(case when c.etat = 'CHECKED' then 1 else 0 end) checked,\r\n"
				+ "       sum(case when c.etat = 'COLLECTED' then 1 else 0 end) collected,\r\n"
				+ "       count(c.id) total from CANDIDAT c"
				+ "";
		QueryRunner queryRunner = new QueryRunner(this.dataSource);
		List<Map<String, Object>> rows = queryRunner.query(sql, new MapListHandler());
		DataMapper<ControleInfos> mapper = new DataMapper<>(ControleInfos.class);
		if (CollectionUtils.isNotEmpty(rows)) {
			for (Map<String, Object> map : rows) {
				mapResults.put("RECEIVED", Integer.valueOf(map.get("RECEIVED").toString()));
				mapResults.put("CHECKED", Integer.valueOf(map.get("CHECKED").toString()));
				mapResults.put("COLLECTED", Integer.valueOf(map.get("COLLECTED").toString()));
				mapResults.put("TOTAL", Integer.valueOf(map.get("TOTAL").toString()));
				break;
			}
		}
		System.err.println(mapResults.toString());
		return mapResults;
	}

	public HashMap<String, Integer> fetchPArtenairesLotsData(String partenaire) throws SQLException {
		HashMap<String, Integer> mapResults = new HashMap<>();
		String sql = null;
		if(partenaire.equalsIgnoreCase(PartenaireEnum.ANCFCC.name())) {
			sql = "select count(distinct lot.id) nbrLot, count(distinct rep.ID) reponse, count(distinct adsa.ID) demandes from AL_LOT_SITUATION_ANCFCC lot inner join AL_DMD_SITUATION_ANCFCC ADSA on lot.ID = ADSA.ID_LOT\r\n"
					+ "inner join AL_DET_LOT_SIT_ANCFCC rep on lot.ID = rep.ID_LOT"
					+ "";
		}else if(partenaire.equalsIgnoreCase(PartenaireEnum.TGR.name())) {
			sql = "select count(distinct lot.id) nbrLot, count(distinct rep.ID) reponse, count(distinct adsa.ID) demandes from AL_LOT_SITUATION_TGR lot inner join AL_DMD_SITUATION_TGR ADSA on lot.ID = ADSA.ID_LOT\r\n"
					+ "inner join AL_DETAILS_LOT_SITUATION_TGR rep on lot.ID = rep.ID_LOT"
					+ "";
		}else if(partenaire.equalsIgnoreCase(PartenaireEnum.DGI.name())) {
			sql = "select count(distinct lot.id) nbrLot, count(distinct rep.ID) reponse, count(distinct adsa.ID) demandes from AL_LOT_SITUATION_DGI lot inner join AL_DMD_SITUATION_DGI ADSA on lot.ID = ADSA.ID_LOT\r\n"
					+ "inner join AL_DETAILS_LOT_SITUATION_DGI rep on lot.ID = rep.ID_LOT"
					+ "";
		}

		QueryRunner queryRunner = new QueryRunner(this.dataSourcePrincipal);
		List<Map<String, Object>> rows = queryRunner.query(sql, new MapListHandler());
		if (CollectionUtils.isNotEmpty(rows)) {
			for (Map<String, Object> map : rows) {
				mapResults.put("NBRLOT", Integer.valueOf(map.get("NBRLOT").toString()));
				mapResults.put("REPONSE", Integer.valueOf(map.get("REPONSE").toString()));
				mapResults.put("DEMANDES", Integer.valueOf(map.get("DEMANDES").toString()));
				break;
			}
		}
		System.err.println(mapResults.toString());
		return mapResults;
	}



	public Map<String, Object> flagImported(List<Long> codes) throws SQLException {
		Map<String, Object> res = new HashMap<String, Object>();
		try {
			String updateQuery = "UPDATE  NOTIFICATION_RSU c set c.etat = 'SENT' WHERE ID IN ";
			String inClausePlaceholders = codes.stream()
					.map(id -> "?")
					.collect(Collectors.joining(","));
			updateQuery += "(" + inClausePlaceholders + ")";
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

	public List<NotificationRsuInfo> getAllCodesMenage() throws SQLException {
		List<NotificationRsuInfo> res = new ArrayList<>();
		String sql = "SELECT * FROM (SELECT ROWNUM r, data.* from (select distinct c.CODE_MENAGE_RSU, c.REFERENCE, c.TYPE_PRESTATION, c.ID,to_char(DATE_EFFET,'DD/MM/YYYY') DATE_EFFET,to_char(DATE_FIN,'DD/MM/YYYY') DATE_FIN,c.IDCS, c.GENRE, c.MONTANT, c.MOTIF, c.ACTIF, c.ECHEANCE, c.ETAT, to_char(c.date_naissance,'DD/MM/YYYY') DATE_NAISSANCE_CHAR from NOTIFICATION_RSU c  where c.ETAT is null or c.ETAT not like 'SENT'   order by c.id asc ) data where ROWNUM <= ?  ) WHERE r >= ? "
				+ "";
		QueryRunner queryRunner = new QueryRunner(this.dataSource);
		List<Map<String, Object>> rows = queryRunner.query(sql, new MapListHandler(), 300,1);
		DataMapper<NotificationRsuInfo> mapper = new DataMapper<>(NotificationRsuInfo.class);
		if (CollectionUtils.isNotEmpty(rows)) {
			for (Map<String, Object> map : rows) {
				res.add(mapper.map(map));
			}
		}
		return res;
	}

}
