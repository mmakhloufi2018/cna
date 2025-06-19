package ma.cdgp.af.entity.tgr;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.dto.af.tgr.AcquittementBenefNotificationTgrDto;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "DETAILS_NOTIF_TGR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetailsNotifTgr {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_LOT", nullable = true)
    private LotNotifTgr lot;

    @Column(name = "DATE_LOT")
    private String dateLot;
    @Column(name = "LOT_ID")
    private Long idLot;
    @Column(name = "CODE_MOTIF")
    private String codeMotif;
    @Column(name = "LIBE_MOTIF")
    private String libeMotif;
    @Column(name = "NOMBRE_ENREGISTREMENT")
    private String nombreEnregistrement;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "detailsNotifTgr", cascade = CascadeType.ALL)
    private Set<RetourBeneficiaresAquittementTgr> beneficiareNonEnregistrer;



    public static DetailsNotifTgr toEntity(AcquittementBenefNotificationTgrDto in) {
        if (in ==null)
            return null;


        DetailsNotifTgr d = new DetailsNotifTgr();

        d.setLibeMotif(in.getLibeMotif());
        d.setDateLot(in.getDateLot());
        d.setIdLot(Long.valueOf(in.getIdLot()));
        d.setCodeMotif(in.getCodeMotif());
        d.setNombreEnregistrement(in.getNombreEnregistrement());
        d.setBeneficiareNonEnregistrer(in.getBeneficiareNonEnregistrer() != null ? in.getBeneficiareNonEnregistrer().stream().map(t -> {
            RetourBeneficiaresAquittementTgr r = new RetourBeneficiaresAquittementTgr();
            r.setCin(t.getCin());
            r.setLibeMotif(t.getLibeMotif());
            r.setCodeMotif(t.getCodeMotif());
            return r;
        }).collect(Collectors.toSet()) : null);
        return d;
    }





}
