package ma.cdgp.af.entity.cmr;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.entity.LotSituationGen;
import ma.cdgp.af.entity.tgr.DemandeNotifTgr;
import ma.cdgp.af.entity.tgr.DetailsNotifTgr;
import org.apache.commons.collections.CollectionUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "LOT_NOTIF_CMR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LotNotifCmr extends LotSituationGen implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "PARETENAIRE")
    private String partenaire;

    @Column(name = "DATE_CREATION")
    private Date dateCreation;

    @Column(name = "DATE_REPONSE")
    private Date dateReponse;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "lot", cascade = CascadeType.ALL)
    private Set<DetailsNotifCmr> personnes;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "lot", cascade = CascadeType.ALL)
    private Set<DemandeNotifCmr> demandes;

    @Column(name = "ETAT_LOT")
    private String etatLot;

    @Column(name = "DATE_LOT")
    private String dateLot;

    @Column(name = "CODE_RETOUR")
    private String codeRetour;

    @Column(name = "MSG_RETOUR", length = 1000)
    private String messageRetour;

    @Column(name = "LOCKED_BATCH")
    private Boolean locked;

    @Column(name = "ID_LOT")
    private Long idLot;

    public void setters() {
        if (CollectionUtils.isNotEmpty(getPersonnes())) {
            getPersonnes().forEach(r -> {
                r.setLot(this);
            });
        }

        if (CollectionUtils.isNotEmpty(getDemandes())) {
            getDemandes().forEach(r -> {
                r.setLot(this);
            });
        }
    }

    @Override
    public Long getIdLot() {
        return null;
    }

    @Override
    public String getType() {
        return null;
    }
}
