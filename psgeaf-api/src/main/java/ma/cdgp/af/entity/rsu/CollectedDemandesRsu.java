package ma.cdgp.af.entity.rsu;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.entity.tgr.LotNotifTgr;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "COLLECTED_DEMANDES_RSU")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CollectedDemandesRsu {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CIN")
    private String cin;

    @Column(name = "IDCS")
    private Long idcs;

    @Column(name = "CHEF_MENAGE")
    private Boolean chefMenage;

    @Column(name = "GENRE")
    private String genre;

    @Column(name = "DATE_NAISSANCE")
    private Date dateNaissance;


    @Column(name = "ETAT")
    private String etat;

    @Column(name = "PARTENAIRE")
    private String partenaire;

    @Column(name = "MOTIF")
    private String motif;

    @Column(name = "FLAG")
    private Boolean flag;


}
