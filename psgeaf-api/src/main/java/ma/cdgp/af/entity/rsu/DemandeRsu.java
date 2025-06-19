package ma.cdgp.af.entity.rsu;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.dto.af.rsu.DemandeRsuDto;
import ma.cdgp.af.dto.af.rsu.LotDemandeRsuDto;
import ma.cdgp.af.entity.CandidatCheckEvent;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "DMD_DEMANDES_RSU")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DemandeRsu implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "IDCS")
    private String idcs;

    @Column(name = "GENRE")
    private String genre;

    @Column(name = "DATE_NAISSANCE")
    private String dateNaissance;

    @Column(name = "CHEF_MENAGE")
    private String chefMenage;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_LOT", nullable = true)
    private LotDemandeRsu lot;



    public static DemandeRsu from(DemandeRsuDto in) {
        if (in == null)
            return null;


        DemandeRsu out = new DemandeRsu();

        out.setIdcs(in.getIdcs());
        out.setGenre(in.getGenre());
        out.setDateNaissance(in.getDateNaissance());
        out.setChefMenage(String.valueOf(in.getChefMenage()));
        return out;
    }


}
