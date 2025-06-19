package ma.cdgp.af.entity;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "LOT_NOTIFICATION_HISTO")
public class RetourTracabiliteNotification {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_LOT", nullable = true)
    private LotSituationGen retour;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "MESSAGE", length = 1000)
    private String message;

    @Column(name = "DATE_EVENT")
    private Date dateEvent;



    public RetourTracabiliteNotification(Long id2) {
        this.id = id2;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LotSituationGen getRetour() {
        return retour;
    }

    public void setRetour(LotSituationGen retour) {
        this.retour = retour;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDateEvent() {
        return dateEvent;
    }

    public void setDateEvent(Date dateEvent) {
        this.dateEvent = dateEvent;
    }

    public RetourTracabiliteNotification() {
        super();
    }

    public RetourTracabiliteNotification(Long id, LotSituationGen retour, String status, String message, Date dateEvent) {
        super();
        this.id = id;
        this.retour = retour;
        this.status = status;
        this.message = message;
        this.dateEvent = dateEvent;
    }
}
