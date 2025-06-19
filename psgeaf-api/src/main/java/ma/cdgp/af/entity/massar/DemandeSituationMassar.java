package ma.cdgp.af.entity.massar;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import ma.cdgp.af.entity.CandidatCheckEvent;
import ma.cdgp.af.entity.cnss.LotSituationCnss;
import ma.cdgp.af.utils.Col;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "DMD_SITUATION_MASSAR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DemandeSituationMassar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NUM_MASSAR")
    private String numMassar;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_LOT", nullable = true)
    private LotSituationMassar lot;

    @Column(name = "ANNEE_SCOLAIRE")
    private String AnneeScolaire;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CANDIDAT_EVENT", nullable = true)
    private CandidatCheckEvent candidatEvent;




}
