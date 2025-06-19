package ma.cdgp.af.entity.tgr;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "RETOUR_BENEF_AQUIT_TGR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RetourBeneficiaresAquittementTgr {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;
    @Column(name = "CIN")
    private String cin;
    @Column(name = "CODE_MOTIF")
    private String codeMotif;
    @Column(name = "LIBE_MOTIF")
    private String libeMotif;

    @ManyToOne
    @JoinColumn(name = "detailsNotifTgr")
    private DetailsNotifTgr detailsNotifTgr;

}

