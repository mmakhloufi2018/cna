package ma.cdgp.af.entity.cmr;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.entity.tgr.LotNotifTgr;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "DETAILS_NOTIF_CMR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetailsNotifCmr {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_LOT", nullable = true)
    private LotNotifCmr lot;


    @Column(name = "DATE_LOT")
    private String dateLot;


    @Column(name = "NOMBRE_ENREGISTREMENT")
    private String nombreEnregistrement;

    @Column(name = "LOT_ID")
    private String idLot;

    @Column(name = "CIN_NON_ENREGISTRES")
    private String cinNonEnregistres;

    @Column(name = "CODE_RETOUR")
    private String codeRetour;
    @Column(name = "MESSGAE_RETOUR")
    private String messageRetour;
}


