package ma.cdgp.af.entity.massar2025;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.entity.CandidatCollected;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "AF_CANDIDAT_EVENT_MASSAR_2025")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CandidatCheckEventMassar2025 {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "CANDIDAT_ID")
    private Long idCandidat;

    @Column(name = "PARTENAIRE")
    private String partenaire;

    @Column(name = "CIN")
    private String cin;

    @Column(name = "IDCS")
    private String idcs;

    private Date dateCreation;

    @ManyToOne
    @JoinColumn(name = "ID_COLLECTED", nullable = true)
    private CandidatCollected collected;



    @PrePersist
    public void beforeSave() {
        this.dateCreation = new Date();
    }
    public static CandidatCheckEventMassar2025 fromCollected(CandidatCollected in, String partenaire) {
        if (in == null) return null;
        CandidatCheckEventMassar2025 out = new CandidatCheckEventMassar2025();
        out.setIdCandidat(in.getIdCandidat());
        out.setCin(in.getCin());
        out.setIdcs(in.getIdcs());
        out.setPartenaire(partenaire);
        out.setCollected(in);
        return out;
    }
}
