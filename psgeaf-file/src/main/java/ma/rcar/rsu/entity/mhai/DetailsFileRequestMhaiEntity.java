package ma.rcar.rsu.entity.mhai;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.rcar.rsu.dto.mhai.DetailsFileRequestMhaiDto;
import org.apache.commons.beanutils.BeanUtils;

import jakarta.persistence.*;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;



/**
 * @author BAKHALED Ibrahim.
 *
 */



@Entity(name = "DETAILS_FILE_REQUEST_MHAI")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetailsFileRequestMhaiEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "ID_ETUDIANT")
    private String idEtudiant;

    @Column(name = "NUM_MASSAR")
    private String numMassar;

    @Column(name = "CNIE")
    private String cnie;

    @Column(name = "DATE_NAISSANCE")
    private String dateNaissance;

    @Column(name ="NATIONALITE")
    private String nationalite;

    @Column(name = "NOM_FR")
    private String nomFr;

    @Column(name = "PRENOM_FR")
    private String prenomFr;

    @Column(name = "NOM_AR")
    private String nomAr;

    @Column(name = "PRENOM_AR")
    private String prenomAr;

    @Column(name = "ANNEE_SCOLAIRE")
    private String anneeScolaire;

    @Column(name = "SCOLARISE")
    private Boolean scolarise;

    @Column(name = "BOURSIER")
    private Boolean boursier;


    @Column(name= "MONTANT")
    private String montant;


    @Column(name = "ETAT")
    private String etat;

    @Column(name = "MOTIF")
    private String motif;

    @ManyToOne
    @JoinColumn(name = "FILE_REQUEST_MHAI_ID", nullable = false)
    private FileRequestMhaiEntity fileRequest;

    @Column(name = "RANG")
    private Integer rang;

    @Column(name = "CODE_MOTIF")
    private String codeMotif;


    public static DetailsFileRequestMhaiEntity from(DetailsFileRequestMhaiDto in) {
        if (in == null)
            return null;
        DetailsFileRequestMhaiEntity out = new DetailsFileRequestMhaiEntity();
        try {
            BeanUtils.copyProperties(out, in);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return out;
    }
}
