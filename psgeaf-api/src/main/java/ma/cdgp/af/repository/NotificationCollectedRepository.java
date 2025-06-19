package ma.cdgp.af.repository;


import ma.cdgp.af.entity.NotificationCollected;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;
import java.util.List;
import java.util.Set;

@Repository
public interface NotificationCollectedRepository extends JpaRepository<NotificationCollected, Long> {


    @Query("select l.active, l.cin from NotificationCollected l")
    public List<NotificationCollected> findCinAndStatut();


    @Modifying
    @Transactional
    @Query("update NotificationCollected l set l.flag = true where l.cin in :cinList")
    int updateFlagToTrueForCins(@Param("cinList") Set<String> cinList);


}
