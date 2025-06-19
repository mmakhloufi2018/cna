package ma.cdgp.af.entity.massar;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.entity.LotSituationGen;
import org.apache.commons.collections.CollectionUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "LOT_SITUATION_MASSAR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LotSituationMassar  extends LotSituationGen implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "LOT_ID")
    private String lotId;

    @Column(name = "IS_NOTES")
    private Boolean isNotes;
    @Column(name = "PARETENAIRE")
    private String partenaire;

    @Column(name = "DATE_CREATION")
    private Date dateCreation;

    @Column(name = "DATE_REPONSE")
    private Date dateReponse;


    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "lot", cascade = CascadeType.ALL)
    private Set<DetailsLotSituationMassar> codeEleves;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "lot", cascade = CascadeType.ALL)
    private Set<DemandeSituationMassar> demandes;

    @Column(name = "ETAT_LOT")
    private String etatLot;

    @Column(name = "DATE_LOT")
    private String dateLot;

    @Column(name = "CODE_RETOUR")
    private String codeRetour;

    @Column(name = "MSG_RETOUR")
    private String messageRetour;

    @Column(name = "LOCKED_BATCH")
    private Boolean locked;




    public void setters() {
        if (CollectionUtils.isNotEmpty(getCodeEleves())) {
            getCodeEleves().forEach(r -> {
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
        return getIdLot();
    }



    @Override
    public String getType() {
        return getPartenaire();
    }
}
