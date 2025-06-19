package ma.cdgp.af.batch;
//package ma.rcar.rsu.batch;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//import javax.sql.DataSource;
//
//import org.springframework.batch.item.ItemReader;
//import org.springframework.batch.item.data.RepositoryItemReader;
//import org.springframework.batch.item.database.JdbcCursorItemReader;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.BeanPropertyRowMapper;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.stereotype.Component;
//import ma.rcar.rsu.entity.Retour;
//
//@Component
//public class RetryReader extends RepositoryItemReader<Retour>   {
////	public RetryReader(@Autowired DataSource primaryDataSource) {
////		setDataSource(primaryDataSource);
////		setSql("SELECT r.* FROM RETOUR_HISTO a inner join RETOUR r on r.ID = a.ID_RETOUR where a.STATUS like 'KO'");
////		setFetchSize(100);
////		setRowMapper(new BeanPropertyRowMapper<>(Retour.class));
////	}
//
//}
