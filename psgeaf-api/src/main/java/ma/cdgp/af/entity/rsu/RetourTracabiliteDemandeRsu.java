package ma.cdgp.af.entity.rsu;


import lombok.Getter;
import lombok.Setter;
import ma.cdgp.af.entity.LotSituationGen;

import javax.persistence.*;
import java.util.Date;

@Getter @Setter
@Entity
@Table(name = "LOT_DEMANDES_RSU_HISTO")
public class RetourTracabiliteDemandeRsu {


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


    public RetourTracabiliteDemandeRsu(){super();};

    public RetourTracabiliteDemandeRsu(Long id, LotSituationGen retour, String status, String message, Date dateEvent) {
        super();
        this.id = id;
        this.retour = retour;
        this.status = status;
        this.message = message;
        this.dateEvent = dateEvent;
    }
}
