package ma.cdgp.af.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.dto.af.tgr.BenefNotificationTgrDto;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "NOTIFICATION_COLLECTED")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationCollected implements Serializable {
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "CIN")
    private String cin;
    @Column(name = "IDCS")
    private String idcs;
    @Column(name = "ACTIVE")
    private Boolean active;
    @Column(name = "MOIS_ANNEE")
    private String moisAnnee;

    @Column(name = "FLAG")
    private Boolean flag;



//    @Column(name = "FLAG")
//    private Integer FLAG;


    public Set<BenefNotificationTgrDto> to (List<NotificationCollected> in) {
        if (in == null)
            return null;
        BenefNotificationTgrDto out = new BenefNotificationTgrDto();
        Set<BenefNotificationTgrDto> data = new HashSet<>();
        in.forEach( c -> {
            out.setCIN(c.getCin());
            out.setStatut(String.valueOf(c.getActive()));
            data.add(out);
        });
        return data;
    }
}
