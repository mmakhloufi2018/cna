package ma.cdgp.af.esgaf;


import ma.cdgp.af.dto.af.rsu.DemandeRsuDto;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class RsuDemandesEsgeafRepository extends JdbcDaoSupport {


    @Autowired
    @Qualifier("esgeafdataSource")
    DataSource dataSource;



    public RsuDemandesEsgeafRepository(DataSource dataSource) {
        setDataSource(dataSource);
    }

    public Set<DemandeRsuDto> getDemandesRsuEsgeaf(int offset, int limit) throws SQLException {
        String sql = "SELECT 'DEMANDEUR' AS TYPE,\n" +
                "       dem.IDCS as idcs,\n" +
                "       dem.EST_CHEF_MENAGE_RSU as chefMenage,\n" +
                "       dem.GENRE as genre,\n" +
                "       dem.DATE_NAISSANCE as dateNaissance,\n" +
                "       DECODE(d.TYPE_PRESTATION, '1', 'FORFAITAIRE', '2', 'ENFANCE', '') AS TYPE_PRESTATION\n" +
                "FROM m_demandeur dem\n" +
                "JOIN dossier_asd d ON dem.DOSSIER_ASD_ID = d.ID\n" +
                "WHERE d.DATE_CLOTURE IS NULL\n" +
                "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        QueryRunner queryRunner = new QueryRunner(this.dataSource);
        Object[] params = { offset, limit };
        List<DemandeRsuDto> list = queryRunner.query(sql, new BeanListHandler<>(DemandeRsuDto.class), params);
        return new HashSet<>(list);
    }

}
