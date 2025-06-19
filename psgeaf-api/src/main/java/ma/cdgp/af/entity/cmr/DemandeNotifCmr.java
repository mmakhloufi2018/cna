package ma.cdgp.af.entity.cmr;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.entity.tgr.LotNotifTgr;
import ma.cdgp.af.utils.Col;

import javax.persistence.*;

@Entity
@Table(name = "DMD_NOTIF_CMR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DemandeNotifCmr {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;


    @Column(name = "CIN")
    private String cin;

    @Column(name = "STATUT")
    private String statut;


    @Column(name = "IDCS")
    private String idcs;


    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_LOT", nullable = true)
    private LotNotifCmr lot;
}
