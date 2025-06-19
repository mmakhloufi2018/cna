package ma.cdgp.af.repository;


import ma.cdgp.af.entity.tgr.NotificationPartenaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationPartenaireRepository extends JpaRepository<NotificationPartenaire, Long> {

}
