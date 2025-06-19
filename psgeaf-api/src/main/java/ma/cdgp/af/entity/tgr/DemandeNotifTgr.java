package ma.cdgp.af.entity.tgr;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "DMD_NOTIF_TGR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DemandeNotifTgr {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CIN")
    private String cin;


    @Column(name = "STATUT")
    private Boolean statut;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_LOT", nullable = true)
    private LotNotifTgr lot;
}
