package ma.cdgp.af.entity.tgr;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.dto.af.tgr.BenefNotificationTgrDto;
import ma.cdgp.af.dto.af.tgr.LotNotificationTgrDto;
import ma.cdgp.af.entity.LotSituationGen;
import org.apache.commons.collections.CollectionUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "LOT_NOTIF_TGR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LotNotifTgr extends LotSituationGen implements Serializable {

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
    private Set<DetailsNotifTgr> personnes = new HashSet<>();

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "lot", cascade = CascadeType.ALL)
    private Set<DemandeNotifTgr> demandes = new HashSet<>();

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "lot", cascade = CascadeType.ALL)
    private Set<NotificationPartenaire> partenaires;

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
        return getId();
    }



    @Override
    public String getType() {
        return getPartenaire();
    }



    public void from(LotNotificationTgrDto in) {
        System.out.println("inside converting");
        if (in == null)
            return;

        this.setDateLot(in.getDateLot());
        this.setIdLot(in.getIdLot());
        if (in.getListeBeneficiaires() != null) {
            for (BenefNotificationTgrDto listeBeneficiaire : in.getListeBeneficiaires()) {
                DemandeNotifTgr demande = new DemandeNotifTgr();
                demande.setStatut(Boolean.valueOf(listeBeneficiaire.getStatut()));
                demande.setCin(listeBeneficiaire.getCIN());
                this.demandes.add(demande);
            }
        }
        System.out.println("out of converting");
    }

    public void updateFromDto(LotNotificationTgrDto dto) {
        if (dto == null) {
            return;
        }

        this.setDateLot(dto.getDateLot());
        this.setIdLot(dto.getIdLot());


        if (dto.getListeBeneficiaires() != null) {

            this.demandes.clear();
            dto.getListeBeneficiaires().forEach(benefDto -> {
                DemandeNotifTgr demande = new DemandeNotifTgr();
                boolean statut = "1".equals(benefDto.getStatut());
                demande.setStatut(statut);
                demande.setCin(benefDto.getCIN());
                this.demandes.add(demande);
            });
        }
    }




}
