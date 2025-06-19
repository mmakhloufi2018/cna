package ma.cdgp.af.entity.rsu;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Clob;
import java.util.Date;

@Entity
@Table(name = "DMD_RSU_TRACE_ENVOI_RETOUR")
@Getter
@Setter
public class TraceEnvoiRetourDemandeRsu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(name = "ENVOI_CDG")
    @Lob
    private Clob envoiCdg;

    @Column(name = "RETOUR_PARTENAIRE")
    @Lob
    private Clob retourPartenaire;

    @Column(name = "DATE_CDG")
    private Date dateCdg;

    @Column(name = "DATE_PARTENAIRE")
    private Date datePartenaire;

    @Column(name = "ID_LOT")
    private Long idRetour;

    @Column(name = "PARTENAIRE")
    private String paretenaire;

    @Column(name = "TYPE_LOT")
    private String typeLot;


    public TraceEnvoiRetourDemandeRsu(){super();}
    public TraceEnvoiRetourDemandeRsu(Long id, Clob envoiCdg, Clob retourPartenaire, Date dateCdg, Date datePartenaire, String paretenaire) {
        super();
        this.id = id;
        this.envoiCdg = envoiCdg;
        this.retourPartenaire = retourPartenaire;
        this.dateCdg = dateCdg;
        this.datePartenaire = datePartenaire;
        this.paretenaire = paretenaire;
    }
}
