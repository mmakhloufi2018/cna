package ma.cdgp.af.entity.massar2025;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import ma.cdgp.af.entity.CandidatCheckEvent;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "DMD_SITUATION_MASSAR_2025")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DemandeSituationMassar2025 implements Serializable {

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
    private LotSituationMassar2025 lot;

    @Column(name = "ANNEE_SCOLAIRE")
    private String AnneeScolaire;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CANDIDAT_EVENT", nullable = true)
    private CandidatCheckEventMassar2025 candidatEvent;




}
