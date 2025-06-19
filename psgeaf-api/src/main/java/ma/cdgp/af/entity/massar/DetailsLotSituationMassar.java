package ma.cdgp.af.entity.massar;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ma.cdgp.af.entity.cmr.LotSituationCmr;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "DETAILS_LOT_SITUATION_MASSAR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DetailsLotSituationMassar  implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;
    @Column(name = "CODE_ELEVE")
    private String CodeEleve;
    @Column(name = "NOM_ELEVE_FR")
    private String nomEleveFr;
    @Column(name = "NOM_ELEVE_AR")
    private String nomEleveAr;
    @Column(name = "PRENOM_ELEVE_FR")
    private String prenomEleveFr;
    @Column(name = "PRENOM_ELEVE_AR")
    private String prenomEleveAr;
    @Column(name = "DATE_NAISSANCE_ELEVE")
    private String dateNaisEleve;
    @Column(name = "CODE_NIVEAU")
    private String CodeNiveau;
    @Column(name = "CIN_TUTEUR")
    private String CinTuteur;
    @Column(name = "CIN_PERE")
    private String CinPere;
    @Column(name = "CIN_MERE")
    private String CinMere;
    @Column(name = "SITUATION_SCOLARISATION")
    private String SituationScolarisation;
    @Column(name = "ANNEE_SCOLAIRE")
    private String AnneeScolaire;
    @Column(name = "LIEU_NAISSANCE")
    private String lieuNaissance;
    @Column(name = "LAST_SITUATION_SCOLARISATION")
    private String lastSituationScolarisation;
    @Column(name = "MOYENNE_GENERALE")
    private String moyenneGenerale;
    @Column(name = "RESULTAT_FIN_ANNEE")
    private String resultatFinAnnee;
    @Column(name = "CODE_ETABLISSEMENT")
    private String codeEtablissement;
    @Column(name = "CODE_PROVINCE")
    private String codeProvince;
    @Column(name = "CIN_ELEVE")
    private String cinEleve;
    @Column(name = "GENRE")
    private String genre;
    @Column(name = "CODE_NATIONALITE")
    private String codeNationalite;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "TYPE_CANDIDAT")
    private String typeCandidat;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_LOT", nullable = true)
    private LotSituationMassar lot;

}
