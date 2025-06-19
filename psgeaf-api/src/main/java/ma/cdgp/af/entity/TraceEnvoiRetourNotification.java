package ma.cdgp.af.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.sql.Clob;
import java.util.Date;

@Entity
@Table(name = "NOTIF_TRACE_ENVOI_RETOUR")
public class TraceEnvoiRetourNotification implements Serializable {

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


    public TraceEnvoiRetourNotification() {
        super();
    }

    public TraceEnvoiRetourNotification(Long id, Clob envoiCdg, Clob retourPartenaire, Date dateCdg, Date datePartenaire, Long idRetour, String paretenaire, String typeLot) {
        this.id = id;
        this.envoiCdg = envoiCdg;
        this.retourPartenaire = retourPartenaire;
        this.dateCdg = dateCdg;
        this.datePartenaire = datePartenaire;
        this.idRetour = idRetour;
        this.paretenaire = paretenaire;
        this.typeLot = typeLot;
    }

    public TraceEnvoiRetourNotification(Long id, Clob envoiCdg, Clob retourPartenaire, Date dateCdg, Date datePartenaire, String paretenaire) {
        super();
        this.id = id;
        this.envoiCdg = envoiCdg;
        this.retourPartenaire = retourPartenaire;
        this.dateCdg = dateCdg;
        this.datePartenaire = datePartenaire;
        this.paretenaire = paretenaire;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Clob getEnvoiCdg() {
        return envoiCdg;
    }

    public void setEnvoiCdg(Clob envoiCdg) {
        this.envoiCdg = envoiCdg;
    }

    public Clob getRetourPartenaire() {
        return retourPartenaire;
    }

    public void setRetourPartenaire(Clob retourPartenaire) {
        this.retourPartenaire = retourPartenaire;
    }

    public Date getDateCdg() {
        return dateCdg;
    }

    public void setDateCdg(Date dateCdg) {
        this.dateCdg = dateCdg;
    }

    public Date getDatePartenaire() {
        return datePartenaire;
    }

    public void setDatePartenaire(Date datePartenaire) {
        this.datePartenaire = datePartenaire;
    }

    public Long getIdRetour() {
        return idRetour;
    }

    public void setIdRetour(Long idRetour) {
        this.idRetour = idRetour;
    }

    public String getParetenaire() {
        return paretenaire;
    }

    public void setParetenaire(String paretenaire) {
        this.paretenaire = paretenaire;
    }

    public String getTypeLot() {
        return typeLot;
    }

    public void setTypeLot(String typeLot) {
        this.typeLot = typeLot;
    }


    @Override
    public String toString() {
        return "TraceEnvoiRetourNotification{" +
                "id=" + id +
                ", envoiCdg=" + envoiCdg +
                ", retourPartenaire=" + retourPartenaire +
                ", dateCdg=" + dateCdg +
                ", datePartenaire=" + datePartenaire +
                ", idRetour=" + idRetour +
                ", paretenaire='" + paretenaire + '\'' +
                ", typeLot='" + typeLot + '\'' +
                '}';
    }


}
