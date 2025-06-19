package ma.cdgp.af.entity.rsu;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.cdgp.af.dto.af.rsu.DetailsDemandeRsuDto;
import ma.cdgp.af.entity.tgr.LotNotifTgr;
import ma.cdgp.af.utils.Utils;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "DETAILS_DEMANDE_RSU")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetailsDemandeRsu implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "IDCS")
    private String idcs;
    @Column(name = "CNIE")
    private String cnie;
    @Column(name = "CODE_RETOUR")
    private String codeRetour;
    @Column(name = "MSG_RETOUR")
    private String msgRetour;
    @Column(name = "NOM_FR")
    private String nomFr;
    @Column(name = "NOM_AR")
    private String nomAr;
    @Column(name = "PRENOM_FR")
    private String prenomFr;
    @Column(name = "PRENOM_AR")
    private String prenomAr;
    @Column(name = "NUM_TEL")
    private String numTel;
    @Column(name = "LIEN_PARENTE")
    private String lienParente;
    @Column(name = "PREFECTURE")
    private String prefecture;
    @Column(name = "COMMUNE")
    private String commune;
    @Column(name = "ANNEXE")
    private String annexe;
    @Column(name = "CODE_MENAGE")
    private String codeMenage;
    @Column(name = "SCORE")
    private String score;
    @Column(name = "DATE_SCORE")
    private Date dateScore;
    @Column(name = "SEUIL")
    private String  seuil;
    @Column(name = "STATUT_AD")
    private String statutAd;
    @Column(name = "ADRESSE")
    private String adresse;
    @Column(name = "ETAT_MATRIMONIAL")
    private String etatMatrimonial;
    @Column(name = "TAILLE_MENAGE")
    private Long tailleMenage;
    @Column(name = "TYPE_PIECE_IDENTITE")
    private Long typePieceIdentite;


    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_LOT", nullable = true)
    private LotDemandeRsu lot;



    public static DetailsDemandeRsu from (DetailsDemandeRsuDto in) {
        if (in == null)
            return null;

        DetailsDemandeRsu out = new DetailsDemandeRsu();
        out.setCnie(in.getCnie());
        out.setIdcs(String.valueOf(in.getIdcs()));
        out.setCodeRetour(in.getCodeRetour());
        out.setMsgRetour(in.getMsgRetour());
        out.setNomAr(in.getNomAr());
        out.setNomFr(in.getNomFr());
        out.setPrenomAr(in.getPrenomAr());
        out.setPrenomFr(in.getPrenomFr());
        out.setNumTel(in.getNumTel());
        out.setLienParente(in.getLienParente());
        out.setPrefecture(in.getPrefecture());
        out.setCommune(in.getCommune());
        out.setAnnexe(in.getAnnexe());
        out.setCodeMenage(in.getCodeMenage());
        out.setScore(String.valueOf(in.getScore()));
        out.setDateScore(Utils.stringToDate(in.getDateScore()));
        out.setSeuil(String.valueOf(in.getSeuil()));
        out.setStatutAd(in.getStatutAd());
        out.setAdresse(in.getAdresse());
        out.setEtatMatrimonial(in.getEtatMatrimonial());
        out.setTailleMenage(in.getTailleMenage());
        out.setTypePieceIdentite(in.getTypePieceIdentite());
        return out;
    }


}
